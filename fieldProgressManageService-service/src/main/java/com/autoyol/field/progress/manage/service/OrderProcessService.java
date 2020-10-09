package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.AttrLabelCache;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.constraint.FieldErrorCode;
import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.exception.OrderException;
import com.autoyol.field.progress.manage.mapper.*;
import com.autoyol.field.progress.manage.request.order.AddPickDeliverReqVo;
import com.autoyol.field.progress.manage.request.order.CancelTransOrderReqVo;
import com.autoyol.field.progress.manage.request.order.UpdateTransOrderReqVo;
import com.autoyol.field.progress.manage.util.ConvertBeanUtil;
import com.dianping.cat.Cat;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.mq.MQConstraint.*;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;
import static java.util.stream.IntStream.range;

@Service
public class OrderProcessService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    TransOrderInfoMapper transOrderInfoMapper;

    @Resource
    TransOrderInfoLogMapper transOrderInfoLogMapper;

    @Resource
    TransVehicleInfoMapper transVehicleInfoMapper;

    @Resource
    TransRentUserInfoMapper transRentUserInfoMapper;

    @Resource
    PickDeliverOrderInfoMapper pickDeliverOrderInfoMapper;

    @Resource
    PickDeliverScheduleInfoMapper pickDeliverScheduleInfoMapper;

    @Resource
    PickDeliverScheduleStatusLogMapper pickDeliverScheduleStatusLogMapper;

    @Resource
    PickDeliverFeeMapper pickDeliverFeeMapper;

    @Resource
    PickDeliverFeeLogMapper pickDeliverFeeLogMapper;

    @Resource
    FeeRecordMapper feeRecordMapper;

    @Resource
    FeeRecordLogMapper feeRecordLogMapper;

    @Resource
    PickDeliverTrxService pickDeliverTrxService;

    @Resource
    DictCache dictCache;

    @Resource
    AttrLabelCache attrLabelCache;

    @Resource
    private RedisOprService redisOprService;

    @Resource
    MsgService msgService;

    @Value("${pick.back.order.id.length:5}")
    private int pickBackOrderIdLength;

    @Value("${oil.unit.price.L}")
    private String oilUnitPriceL;

    @Value("${oil.unit.price.T}")
    private String oilUnitPriceT;

    public int updateFlowOrder(UpdateTransOrderReqVo reqVo) throws Exception {
        TransOrderInfoEntity transOrderInfoEntity = transOrderInfoMapper.queryByOrderNo(reqVo.getOrdernumber());
        if (Objects.isNull(transOrderInfoEntity)) {
            return NEG_ONE;
        }

        PickDeliverOrderInfoEntity pickDeliverOrderInfoEntity = new PickDeliverOrderInfoEntity();
        pickDeliverOrderInfoEntity.setOrderNo(transOrderInfoEntity.getOrderNo());
        pickDeliverOrderInfoEntity.setPickTime(transOrderInfoEntity.getPickTime());
        pickDeliverOrderInfoEntity.setServiceType(getServiceType(reqVo.getServicetype()));
        List<PickDeliverOrderInfoEntity> tackBackList = pickDeliverOrderInfoMapper.selectByCond(pickDeliverOrderInfoEntity);
        if (CollectionUtils.isEmpty(tackBackList)) {
            return NEG_ZERO;
        }
        int type = getServiceType(reqVo.getServicetype());
        List<PickDeliverOrderInfoEntity> tackBackOrderList = tackBackList.stream().filter(tackBack -> type == NEG_ZERO || objEquals(type, tackBack.getServiceType(), Integer::equals))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tackBackOrderList)) {
            return NEG_ZERO;
        }

        List<PickDeliverOrderInfoEntity> preUpdateList = tackBackOrderList.stream()
                .map(tackBack -> convertReqToUpdatePickDel(reqVo, tackBack)).filter(Objects::nonNull).collect(Collectors.toList());
        TransOrderInfoEntity transOrderInfo = null;
        if (StringUtils.isNotEmpty(reqVo.getNewtermtime()) || StringUtils.isNotEmpty(reqVo.getNewreturntime())) {
            transOrderInfo = new TransOrderInfoEntity();
            transOrderInfo.setOrderNo(transOrderInfoEntity.getOrderNo());
            transOrderInfo.setRentStartTime(parse(reqVo.getNewtermtime(), DATE_NO_SECOND));
            transOrderInfo.setRentEntTime(parse(reqVo.getNewreturntime(), DATE_NO_SECOND));

        }

        List<String> pickBackNoList = preUpdateList.stream()
                .map(PickDeliverOrderInfoEntity::getPickDeliverOrderNo).collect(Collectors.toList());
        List<PickDeliverScheduleInfoEntity> scheduleInfoList = pickDeliverScheduleInfoMapper.queryByPickBackNo(pickBackNoList);

        List<PickDeliverScheduleStatusLogEntity> scheduleStatusLogList = pickBackNoList.stream().map(no -> {
            PickDeliverScheduleStatusLogEntity scheduleStatusLogEntity = new PickDeliverScheduleStatusLogEntity();
            scheduleStatusLogEntity.setPickDeliverOrderNo(no);
            scheduleStatusLogEntity.setScheduleStatus(INT_TOW);
            scheduleStatusLogEntity.setCreateOp(SYS);
            return scheduleStatusLogEntity;
        }).collect(Collectors.toList());

        List<PickDeliverScheduleInfoLogEntity> scheduleInfoLogList = scheduleInfoList.stream().map(this::buildScheduleLog).collect(Collectors.toList());

        List<PickDeliverLogEntity> deliverLogList = preUpdateList.stream().map(x -> buildDeliverLog(tackBackOrderList, x)).collect(Collectors.toList());

        pickDeliverTrxService.changeOrder(deliverLogList, preUpdateList, pickBackNoList, scheduleInfoLogList, scheduleStatusLogList,
                transOrderInfo, transOrderInfoEntity);
        return 1;
    }

    private PickDeliverLogEntity buildDeliverLog(List<PickDeliverOrderInfoEntity> tackBackOrderList, PickDeliverOrderInfoEntity x) {
        PickDeliverOrderInfoEntity logEntity = tackBackOrderList.stream()
                .filter(y -> x.getPickDeliverOrderNo().equals(y.getPickDeliverOrderNo())).findFirst().orElse(new PickDeliverOrderInfoEntity());
        StringBuffer buffer = buildPickOrderOprContent(x, logEntity);
        PickDeliverLogEntity deliverLogEntity = new PickDeliverLogEntity();
        deliverLogEntity.setServiceOrderNo(logEntity.getOrderNo());
        deliverLogEntity.setModuleType(NEG_ZERO);
        deliverLogEntity.setOprContent(buffer.toString());
        deliverLogEntity.setCreateOp(SYS);
        return deliverLogEntity;
    }

    private StringBuffer buildPickOrderOprContent(PickDeliverOrderInfoEntity logEntity, PickDeliverOrderInfoEntity pickDeliverOrder) {
        StringBuffer buffer = new StringBuffer();
        if (isChange(logEntity.getPickTime(), pickDeliverOrder.getPickTime(), Date::equals)) {
            setTackBackLog("取车时间", getLastVal(logEntity, log -> Objects.nonNull(log.getPickTime()), PickDeliverOrderInfoEntity::getPickTime, EMPTY),
                    pickDeliverOrder.getPickTime(), buffer);
        }
        if (isChange(logEntity.getDeliverTime(), pickDeliverOrder.getDeliverTime(), Date::equals)) {
            setTackBackLog("送达时间", getLastVal(logEntity, log -> Objects.nonNull(log.getDeliverTime()), PickDeliverOrderInfoEntity::getDeliverTime, EMPTY),
                    pickDeliverOrder.getDeliverTime(), buffer);
        }
        if (isChange(logEntity.getPickAddr(), pickDeliverOrder.getPickAddr(), String::equals)) {
            setTackBackLog("取车地址", getLastVal(logEntity, log -> Objects.nonNull(log.getPickAddr()), PickDeliverOrderInfoEntity::getPickAddr, EMPTY),
                    pickDeliverOrder.getPickAddr(), buffer);
        }
        if (isChange(logEntity.getDeliverAddr(), pickDeliverOrder.getDeliverAddr(), String::equals)) {
            setTackBackLog("送达地址", getLastVal(logEntity, log -> Objects.nonNull(log.getDeliverAddr()), PickDeliverOrderInfoEntity::getDeliverAddr, EMPTY),
                    pickDeliverOrder.getDeliverAddr(), buffer);
        }
        return buffer;
    }

    private PickDeliverScheduleInfoLogEntity buildScheduleLog(PickDeliverScheduleInfoEntity scheduleInfo) {
        PickDeliverScheduleInfoLogEntity logEntity = new PickDeliverScheduleInfoEntity();
        logEntity.setPickDeliverOrderNo(scheduleInfo.getPickDeliverOrderNo());
        logEntity.setScheduleStatus(scheduleInfo.getScheduleStatus());
        logEntity.setFieldAppStatus(scheduleInfo.getFieldAppStatus());
        logEntity.setIsSupplierDistribute(scheduleInfo.getIsSupplierDistribute());
        logEntity.setSupplierCompanyId(scheduleInfo.getSupplierCompanyId());
        logEntity.setSupplierCompanyName(scheduleInfo.getSupplierCompanyName());
        logEntity.setUserId(scheduleInfo.getUserId());
        logEntity.setUserName(scheduleInfo.getUserName());
        logEntity.setUserPhone(scheduleInfo.getUserPhone());
        logEntity.setIsOwnPerson(scheduleInfo.getIsOwnPerson());
        logEntity.setDistributeType(scheduleInfo.getDistributeType());
        logEntity.setFlightNo(scheduleInfo.getFlightNo());
        logEntity.setScheduleMemo(scheduleInfo.getScheduleMemo());
        logEntity.setIsDelete(scheduleInfo.getIsDelete());
        logEntity.setCreateOp(scheduleInfo.getCreateOp());
        logEntity.setCreateTime(scheduleInfo.getCreateTime());
        logEntity.setUpdateOp(scheduleInfo.getUpdateOp());
        logEntity.setUpdateTime(scheduleInfo.getUpdateTime());
        return logEntity;
    }

    private PickDeliverOrderInfoEntity convertReqToUpdatePickDel(UpdateTransOrderReqVo reqVo, PickDeliverOrderInfoEntity tackBack) {
        try {
            PickDeliverOrderInfoEntity pickDeliverOrder = new PickDeliverOrderInfoEntity();
            pickDeliverOrder.setPickDeliverOrderNo(tackBack.getPickDeliverOrderNo());
            pickDeliverOrder.setOldPickTime(tackBack.getPickTime());
            pickDeliverOrder.setPickTime(parse(Optional.ofNullable(reqVo.getNewtermtime()).filter(StringUtils::isNotEmpty).orElse(reqVo.getNewbeforeTime()), DATE_NO_SECOND));
            pickDeliverOrder.setDeliverTime(parse(Optional.ofNullable(reqVo.getNewreturntime()).filter(StringUtils::isNotEmpty).orElse(reqVo.getNewafterTime()), DATE_NO_SECOND));

            pickDeliverOrder.setPickAddr(Optional.ofNullable(reqVo.getNewpickupcaraddr()).filter(StringUtils::isNotEmpty).orElse(reqVo.getOwnerGetAddr()));
            pickDeliverOrder.setPickLong(new BigDecimal(Optional.ofNullable(reqVo.getRealGetCarLon()).orElse(reqVo.getOwnerGetLon())));
            pickDeliverOrder.setPickLat(new BigDecimal(Optional.ofNullable(reqVo.getRealGetCarLat()).orElse(reqVo.getOwnerGetLat())));

            pickDeliverOrder.setDeliverAddr(Optional.ofNullable(reqVo.getNewalsocaraddr()).filter(StringUtils::isNotEmpty).orElse(reqVo.getOwnerReturnAddr()));
            pickDeliverOrder.setDeliverLong(new BigDecimal(Optional.ofNullable(reqVo.getRealReturnCarLon()).orElse(reqVo.getOwnerReturnLon())));
            pickDeliverOrder.setDeliverLat(new BigDecimal(Optional.ofNullable(reqVo.getRealReturnCarLat()).orElse(reqVo.getOwnerReturnLat())));
            pickDeliverOrder.setUpdateOp(SYS);
            return pickDeliverOrder;
        } catch (Exception e) {
            logger.error("订单变更失败 req：{}，e：{}", reqVo, e);
            Cat.logError("订单变更失败 异常 {}", e);
            return null;
        }
    }

    public int cancelFlowOrder(CancelTransOrderReqVo reqVo) {
        TransOrderInfoEntity transOrderInfoEntity = transOrderInfoMapper.queryByOrderNo(reqVo.getOrdernumber());
        if (Objects.isNull(transOrderInfoEntity)) {
            return NEG_ONE;
        }

        PickDeliverOrderInfoEntity pickDeliverOrderInfoEntity = new PickDeliverOrderInfoEntity();
        pickDeliverOrderInfoEntity.setOrderNo(transOrderInfoEntity.getOrderNo());
        pickDeliverOrderInfoEntity.setPickTime(transOrderInfoEntity.getPickTime());
        List<PickDeliverOrderInfoEntity> tackBackList = pickDeliverOrderInfoMapper.selectByCond(pickDeliverOrderInfoEntity);
        if (CollectionUtils.isEmpty(tackBackList)) {
            return NEG_ZERO;
        }
        int type = getServiceType(reqVo.getServicetype());
        tackBackList = tackBackList.stream().filter(tackBack -> type == NEG_ZERO ||
                objEquals(type, tackBack.getServiceType(), Integer::equals))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tackBackList)) {
            return NEG_ZERO;
        }

        List<PickDeliverScheduleStatusLogEntity> scheduleStatusLogList = tackBackList.stream().map(tackBack -> {
            PickDeliverScheduleStatusLogEntity statusLogEntity = new PickDeliverScheduleStatusLogEntity();
            statusLogEntity.setPickDeliverOrderNo(tackBack.getPickDeliverOrderNo());
            statusLogEntity.setScheduleStatus(INT_EIGHTEEN);
            return statusLogEntity;
        }).collect(Collectors.toList());

        List<String> tackBackOrderNoList = tackBackList.stream()
                .map(PickDeliverOrderInfoEntity::getPickDeliverOrderNo)
                .collect(Collectors.toList());

        pickDeliverTrxService.cancelOrder(tackBackList, scheduleStatusLogList, tackBackOrderNoList);
        return 1;
    }

    @Transactional
    public int addOrderProcess(AddPickDeliverReqVo reqVo) throws Exception {
        List<String> typeList = buildTypeList(TRANS_SOURCE, OFFLINE_ORDER_TYPE, DETECT_STATUS, FLOW_SERVER_TYPE, VEHICLE_TYPE, ENGINE_TYPE, TRANS_SCENE_SOURCE);
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);

        TransOrderInfoEntity transOrder = transOrderInfoMapper.queryByOrderNo(reqVo.getOrdernumber());
        TransOrderInfoEntity transOrderInfoEntity = null;
        if (Objects.isNull(transOrder)) {
            // 保存交易信息
            transOrderInfoEntity = convertTransOrderInfoEntity(reqVo, dictMap);
            transOrderInfoMapper.insertSelective(transOrderInfoEntity);
            transOrderInfoLogMapper.insertSelective(transOrderInfoEntity);
            transVehicleInfoMapper.insertSelective(convertTransVehicleInfoEntity(reqVo, dictMap));
            transRentUserInfoMapper.insertSelective(convertTransRentUserInfoEntity(reqVo));
        }

        // 生成取送车订单号
        PickDeliverOrderInfoEntity pickDeliverOrderInfoEntity = convertPickDeliverOrderInfoEntity(reqVo, dictMap);

        List<PickDeliverOrderInfoEntity> pickOrder = pickDeliverOrderInfoMapper.selectByCond(pickDeliverOrderInfoEntity);
        if (!CollectionUtils.isEmpty(pickOrder)) {
            redisOprService.decrement(PICK_DELIVER_CURRENT_INCR_KEY);
            throw new OrderException(FieldErrorCode.TACK_BACK_ORDER_EXIST.getCode(), FieldErrorCode.TACK_BACK_ORDER_EXIST.getText());
        }

        pickDeliverOrderInfoMapper.insertSelective(pickDeliverOrderInfoEntity);
        pickDeliverScheduleStatusLogMapper.insertSelective(
                convertScheduleStatusLogEntity(pickDeliverOrderInfoEntity.getPickDeliverOrderNo(), NEG_ZERO, null));

        /*PickDeliverOrderInfoEntity updatePickOrder = new PickDeliverOrderInfoEntity();
        updatePickOrder.setPickDeliverOrderNo(pickDeliverOrderInfoEntity.getPickDeliverOrderNo());
        updatePickOrder.setOrderNo(pickDeliverOrderInfoEntity.getOrderNo());
        updatePickOrder.setPickTime(pickDeliverOrderInfoEntity.getPickTime());
        updatePickOrder.setScheduleStatus(INT_ONE);
        updatePickOrder.setUpdateOp(SYS);
        pickDeliverOrderInfoMapper.updateByCond(updatePickOrder);*/
        pickDeliverScheduleStatusLogMapper.insertSelective(
                convertScheduleStatusLogEntity(pickDeliverOrderInfoEntity.getPickDeliverOrderNo(), INT_ONE, null));

        PickDeliverFeeWithMemoEntity feeWithMemoEntity = new PickDeliverFeeWithMemoEntity();
        feeWithMemoEntity.setPickDeliverOrderNo(pickDeliverOrderInfoEntity.getPickDeliverOrderNo());
        pickDeliverFeeMapper.insertSelective(feeWithMemoEntity);
        PickDeliverFeeLogWithMemoEntity logWithMemoEntity = new PickDeliverFeeLogWithMemoEntity();
        logWithMemoEntity.setPickDeliverOrderNo(pickDeliverOrderInfoEntity.getPickDeliverOrderNo());
        pickDeliverFeeLogMapper.insertSelective(logWithMemoEntity);

        List<AttrLabelEntity> feeLabelList = attrLabelCache.getAttrListByBelongSysFromCache(pickDeliverOrderInfoEntity.getServiceType());
        if (CollectionUtils.isEmpty(feeLabelList)) {
            redisOprService.decrement(PICK_DELIVER_CURRENT_INCR_KEY);
            throw new OrderException(FieldErrorCode.TACK_BACK_FEE_NOT_EXIST.getCode(), FieldErrorCode.TACK_BACK_FEE_NOT_EXIST.getText());
        }

        List<FeeRecordLogEntity> feeRecordLogEntityList = Lists.newArrayList();
        List<FeeRecordEntity> feeRecordList = feeLabelList.stream().map(feeLabel -> {
            FeeRecordEntity feeRecordEntity = new FeeRecordEntity();
            feeRecordEntity.setPickDeliverFeeId(feeWithMemoEntity.getId());
            feeRecordEntity.setFeeLabelId(feeLabel.getId());
            feeRecordEntity.setAttrCode(feeLabel.getAttrCode());
            if (StringUtils.equalsIgnoreCase(feeLabel.getAttrCode(), FUEL_ATTR)) {
                feeRecordEntity.setExpenseType(EXPENSE_FUEL);
            }
            if (StringUtils.equalsIgnoreCase(feeLabel.getAttrCode(), PARK_ATTR)) {
                feeRecordEntity.setExpenseType(EXPENSE_PARK);
            }
            if (StringUtils.equalsIgnoreCase(feeLabel.getAttrCode(), TRAFFIC_ATTR)) {
                feeRecordEntity.setExpenseType(EXPENSE_TRAFFIC);
            }
            FeeRecordLogEntity feeRecordLogEntity = new FeeRecordEntity();
            try {
                ConvertBeanUtil.copyProperties(feeRecordLogEntity, feeRecordEntity);
            } catch (Exception e) {
                logger.error("费用日志信息设置失败 e={}", e);
                Cat.logError("费用日志信息设置 异常 {}", e);
            }
            feeRecordLogEntityList.add(feeRecordLogEntity);
            return feeRecordEntity;
        }).collect(Collectors.toList());
        feeRecordMapper.batchInsert(feeRecordList);
        feeRecordLogMapper.batchInsert(feeRecordLogEntityList);

        sendMsg(reqVo, getObjOrDefault(transOrderInfoEntity, Function.identity(), transOrder), pickDeliverOrderInfoEntity);
        return 1;
    }

    private void sendMsg(AddPickDeliverReqVo reqVo, TransOrderInfoEntity transOrderInfoEntity,
                         PickDeliverOrderInfoEntity pickDeliverOrderInfoEntity) {
        try {
            if (!StringUtils.equalsIgnoreCase(reqVo.getOfflineOrderType(), "同程订单")) {

                LocalDate pickTimeDate = convertToLocalDate(pickDeliverOrderInfoEntity.getPickTime());
                LocalDate rentTimeDate = convertToLocalDate(transOrderInfoEntity.getRentEntTime());
                LocalTime localTime = convertToLocalTime(pickDeliverOrderInfoEntity.getPickTime());
                int day = getInterDay(pickTimeDate, convertToLocalDate(transOrderInfoEntity.getDepositPayTime()));
                String msgName = pickDeliverOrderInfoEntity.getServiceType() == 3 ? TACK_ADD_WAIT_DISPATCH : BACK_ADD_WAIT_DISPATCH;
                Map<String, Object> paramMap = msgService.buildMap(msgName, reqVo.getTenantphone(), WAIT_ORDER_PROCEDURE_MSG);
                int month = 0;
                int dayMsg = 0;
                String hour = null;
                LocalTime twentyTwo = LocalTime.of(INT_TWENTY_TWO, NEG_ZERO);
                if (day > 0) {
                    month = rentTimeDate.getMonth().getValue();
                    dayMsg = rentTimeDate.minusDays(INT_ONE).getDayOfMonth();
                    hour = formatLocalTime(twentyTwo, DATE_TIME_NO_SECOND);
                } else {
                    LocalTime nineHalf = LocalTime.of(INT_NINE, INT_THIRTY);
                    if (localTime.isBefore(nineHalf)) {
                        month = pickTimeDate.getMonth().getValue();
                        dayMsg = pickTimeDate.minusDays(INT_ONE).getDayOfMonth();
                        hour = formatLocalTime(twentyTwo, DATE_TIME_NO_SECOND);
                    } else if (localTime.isAfter(LocalTime.of(INT_ELEVEN, INT_THIRTY))) {
                        month = pickTimeDate.getMonth().getValue();
                        dayMsg = pickTimeDate.getDayOfMonth();
                        hour = formatLocalTime(localTime.minusHours(INT_TOW), DATE_TIME_NO_SECOND);
                    } else {
                        month = pickTimeDate.getMonth().getValue();
                        dayMsg = pickTimeDate.getDayOfMonth();
                        hour = formatLocalTime(nineHalf, DATE_TIME_NO_SECOND);
                    }
                }
                paramMap.put("month", month);
                paramMap.put("day", dayMsg);
                paramMap.put("hour", hour);
                msgService.sendMq(SEND_SMS_NORMAL_CHANNEL_QUEUE, SEND_SMS_NORMAL_CHANNEL_QUEUE, NEG_ZERO, paramMap);

            }

        } catch (Exception e) {
            logger.error("***PRO*** 获取会员信息发送短信 error:{}", e);
            Cat.logError("获取会员信息发送短信 异常 {}", e);
        }
    }

    private PickDeliverScheduleStatusLogEntity convertScheduleStatusLogEntity(String pickDeliverOrderNo, int scheduleStatus, String reason) {
        PickDeliverScheduleStatusLogEntity pickDeliverScheduleStatusLogEntity = new PickDeliverScheduleStatusLogEntity();
        pickDeliverScheduleStatusLogEntity.setPickDeliverOrderNo(pickDeliverOrderNo);
        pickDeliverScheduleStatusLogEntity.setScheduleStatus(scheduleStatus);
        pickDeliverScheduleStatusLogEntity.setReason(reason);
        return pickDeliverScheduleStatusLogEntity;
    }

    private PickDeliverOrderInfoEntity convertPickDeliverOrderInfoEntity(AddPickDeliverReqVo reqVo, Map<String, List<SysDictEntity>> dictMap) throws Exception {
        PickDeliverOrderInfoEntity pickDeliverOrderInfoEntity = new PickDeliverOrderInfoEntity();
        pickDeliverOrderInfoEntity.setOrderNo(reqVo.getOrdernumber());
        pickDeliverOrderInfoEntity.setServiceType(
                Optional.ofNullable(reqVo.getServicetype()).filter(serviceType -> StringUtils.equalsIgnoreCase(BACK, serviceType)).map(s -> INT_FOUR)
                        .orElse(INT_THREE));
        String pickBackId = getPickBackOrderId(getLabel1ByCode(dictMap.get(FLOW_SERVER_TYPE), String.valueOf(pickDeliverOrderInfoEntity.getServiceType())));
        pickDeliverOrderInfoEntity.setPickDeliverOrderNo(pickBackId);
        pickDeliverOrderInfoEntity.setIsSupply(NEG_ZERO);
        pickDeliverOrderInfoEntity.setOprType(NEG_ZERO);
        pickDeliverOrderInfoEntity.setScheduleStatus(INT_ONE);
        pickDeliverOrderInfoEntity.setPickTime(parse(reqVo.getBeforeTime(), DATE_NO_SECOND_1));
        if (pickDeliverOrderInfoEntity.getServiceType() == INT_THREE) {
            pickDeliverOrderInfoEntity.setPickAddr(reqVo.getOwnerGetAddr());
            pickDeliverOrderInfoEntity.setPickLong(new BigDecimal(reqVo.getOwnerGetLon()));
            pickDeliverOrderInfoEntity.setPickLat(new BigDecimal(reqVo.getOwnerGetLat()));
            pickDeliverOrderInfoEntity.setDeliverAddr(reqVo.getPickupcaraddr());
            pickDeliverOrderInfoEntity.setDeliverLong(new BigDecimal(reqVo.getRealGetCarLon()));
            pickDeliverOrderInfoEntity.setDeliverLat(new BigDecimal(reqVo.getRealGetCarLat()));
        } else {
            pickDeliverOrderInfoEntity.setPickAddr(reqVo.getAlsocaraddr());
            pickDeliverOrderInfoEntity.setPickLong(new BigDecimal(reqVo.getRealReturnCarLon()));
            pickDeliverOrderInfoEntity.setPickLat(new BigDecimal(reqVo.getRealReturnCarLat()));
            pickDeliverOrderInfoEntity.setDeliverAddr(reqVo.getOwnerReturnAddr());
            pickDeliverOrderInfoEntity.setDeliverLong(new BigDecimal(reqVo.getOwnerReturnLon()));
            pickDeliverOrderInfoEntity.setDeliverLat(new BigDecimal(reqVo.getOwnerReturnLat()));
        }
        pickDeliverOrderInfoEntity.setDeliverTime(parse(reqVo.getTermtime(), DATE_NO_SECOND_1));
        pickDeliverOrderInfoEntity.setTakeNote(reqVo.getTakeNote());
        return pickDeliverOrderInfoEntity;
    }

    private String getPickBackOrderId(String prefix) {
        long count = redisOprService.incrementAndGet(PICK_DELIVER_CURRENT_INCR_KEY, convertToDate(todayMaxTime()));
        int lessZero = Math.abs(pickBackOrderIdLength - String.valueOf(count).length());

        StringBuilder sb = new StringBuilder(prefix);
        sb.append(formatter(LocalDate.now(), DATE_YYYY_MM_DD_CONTACT));
        Optional.of(lessZero).filter(less -> less > NEG_ZERO).ifPresent(less -> {
            range(NEG_ZERO, less).forEach(i -> sb.append(NEG_ZERO));
        });
        sb.append(count);
        return sb.toString();
    }

    private TransRentUserInfoEntity convertTransRentUserInfoEntity(AddPickDeliverReqVo reqVo) {
        TransRentUserInfoEntity transRentUserInfoEntity = new TransRentUserInfoEntity();
        transRentUserInfoEntity.setOrderNo(reqVo.getOrdernumber());
        transRentUserInfoEntity.setOwnerNo(reqVo.getOwnerNo());
        transRentUserInfoEntity.setRenterNo(reqVo.getRenterNo());
        transRentUserInfoEntity.setOwnerName(reqVo.getOwnername());
        transRentUserInfoEntity.setRenterName(reqVo.getTenantname());
        transRentUserInfoEntity.setOwnerPhone(reqVo.getOwnerphone());
        transRentUserInfoEntity.setRenterPhone(reqVo.getTenantphone());
        transRentUserInfoEntity.setOwnerLevel(reqVo.getOwnerLevel());
        transRentUserInfoEntity.setRenterLevel(reqVo.getTenantLevel());
        transRentUserInfoEntity.setOwnerLabel(reqVo.getOwnerLables());
        transRentUserInfoEntity.setRenterLabel(reqVo.getTenantLables());
        transRentUserInfoEntity.setMemFlag(reqVo.getRenterMemberFlag());
        //transRentUserInfoEntity.setRenterUseTakeBackTime(reqVo.getTenantturnoverno());
        Integer count = transRentUserInfoMapper.queryMaxTackBackTimeByRenterNo(reqVo.getRenterNo());
        transRentUserInfoEntity.setRenterUseTakeBackTime(Optional.ofNullable(count).map(String::valueOf).orElse(String.valueOf(NEG_ZERO)));
        return transRentUserInfoEntity;
    }

    private TransVehicleInfoEntity convertTransVehicleInfoEntity(AddPickDeliverReqVo reqVo, Map<String, List<SysDictEntity>> dictMap) {
        TransVehicleInfoEntity transVehicleInfoEntity = new TransVehicleInfoEntity();
        transVehicleInfoEntity.setOrderNo(reqVo.getOrdernumber());
        transVehicleInfoEntity.setVehicleType(getCodeByLabel(dictMap.get(VEHICLE_TYPE), reqVo.getVehicletype()));
        transVehicleInfoEntity.setEscrowAdmin(reqVo.getDelegaAdmin());
        transVehicleInfoEntity.setEscrowAdminPhone(reqVo.getDelegaAdminPhone());
        transVehicleInfoEntity.setVehicleModel(reqVo.getVehiclemodel());
        transVehicleInfoEntity.setVehicleNo(reqVo.getCarno());
        transVehicleInfoEntity.setTankCapacity(reqVo.getTankCapacity());
        transVehicleInfoEntity.setDisplacement(reqVo.getDisplacement());
        transVehicleInfoEntity.setOilScaleDenominator(reqVo.getOilScaleDenominator());
        transVehicleInfoEntity.setEngineType(getCodeByLabel(dictMap.get(ENGINE_TYPE), reqVo.getEngineType()));
        transVehicleInfoEntity.setOilPrice(new BigDecimal(reqVo.getOilPrice()));
        transVehicleInfoEntity.setOilUnitPrice(getOilUnitPrice(reqVo.getDisplacement()).toString());
        transVehicleInfoEntity.setDayMileage(reqVo.getDayMileage());
        transVehicleInfoEntity.setGuideDayPrice(new BigDecimal(reqVo.getGuideDayPrice()));
        transVehicleInfoEntity.setDetectStatus(getCodeByLabel(dictMap.get(DETECT_STATUS), reqVo.getDetectStatus()));
        return transVehicleInfoEntity;
    }

    private Number getOilUnitPrice(String disPlacement) {

        return Optional.ofNullable(disPlacement).map(disPlace -> {
            float disPlaceF = Float.parseFloat(disPlace.substring(NEG_ZERO, disPlace.length() - 1));
            if (StringUtils.containsIgnoreCase(disPlace, CHAR_L)) {
                return getOilUnitPrice(oilUnitPriceL, String.valueOf(disPlaceF));
            }
            if (StringUtils.containsIgnoreCase(disPlace, CHAR_T)) {
                return getOilUnitPrice(oilUnitPriceT, String.valueOf(disPlaceF));
            }
            return 0;
        }).orElse(NEG_ZERO);
    }

    private static float getOilUnitPrice(String str, String disPlacement) {
        String oilUnitPrice = null;
        String[] kv = str.split(SPLIT_AND);
        String[] valArr = kv[1].split(SPLIT_COMMA);

        String[] condZone = kv[0].split(SPLIT_DAWN);
        for (int i = 0; i < condZone.length; i++) {
            String zone = condZone[i].substring(1, condZone[i].length() - 1);

            String[] zoneVal = zone.split(SPLIT_COMMA);
            int compare = Float.compare(Float.parseFloat(disPlacement), Float.parseFloat(zoneVal[0]));
            if (i == 0 && compare <= 0) {
                oilUnitPrice = valArr[i];
                break;
            }
            if (i == condZone.length - 1 && compare >= 0) {
                oilUnitPrice = valArr[i];
                break;
            }
            if (i > 0 && compare >= 0 && Float.compare(Float.parseFloat(disPlacement), Float.parseFloat(zoneVal[1])) <= 0) {
                oilUnitPrice = valArr[i];
                break;
            }
        }
        return Float.parseFloat(oilUnitPrice);
    }

    private TransOrderInfoEntity convertTransOrderInfoEntity(AddPickDeliverReqVo reqVo, Map<String, List<SysDictEntity>> dictMap) throws Exception {
        TransOrderInfoEntity transOrderInfoEntity = new TransOrderInfoEntity();
        transOrderInfoEntity.setOrderNo(reqVo.getOrdernumber());
        transOrderInfoEntity.setOrderType(reqVo.getOrderType());
        transOrderInfoEntity.setBelongCity(reqVo.getDeliverycarcity());
        transOrderInfoEntity.setSource(getCodeByLabel(dictMap.get(TRANS_SOURCE), reqVo.getSource()));
        transOrderInfoEntity.setSceneSource(getCodeByLabel(dictMap.get(TRANS_SCENE_SOURCE), reqVo.getSceneName()));
        transOrderInfoEntity.setOfflineOrderType(getCodeByLabel(dictMap.get(OFFLINE_ORDER_TYPE), String.valueOf(reqVo.getOfflineOrderType())));
        transOrderInfoEntity.setPickTime(parse(reqVo.getBeforeTime(), DATE_NO_SECOND_1));
        transOrderInfoEntity.setRentStartTime(parse(reqVo.getTermtime(), DATE_FULL));
        transOrderInfoEntity.setRentEntTime(parse(reqVo.getReturntime(), DATE_FULL));
        transOrderInfoEntity.setRentAmt(new BigDecimal(reqVo.getRentAmt()));
        transOrderInfoEntity.setPricePerDay(new BigDecimal(reqVo.getHolidayAverage()));
        transOrderInfoEntity.setDepositPayTime(parse(reqVo.getDepositPayTime(), DATE_FULL_NO_CONTACT));
        transOrderInfoEntity.setSuperSuppleRisk(reqVo.getSsaRisks());
        transOrderInfoEntity.setPartner(reqVo.getPartner());
        transOrderInfoEntity.setRiskControlAuditState(reqVo.getRiskControlAuditState());
        // TODO
        transOrderInfoEntity.setCustNote("");
        transOrderInfoEntity.setEditStatus(INT_ONE);
        return transOrderInfoEntity;
    }
}
