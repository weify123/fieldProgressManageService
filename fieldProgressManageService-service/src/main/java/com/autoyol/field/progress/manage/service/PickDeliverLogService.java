package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.mapper.*;
import com.autoyol.field.progress.manage.mem.MemberService;
import com.autoyol.field.progress.manage.request.tackback.PickDeliverLogPageReqVo;
import com.autoyol.field.progress.manage.request.tackback.TackBackBatchDispatchSupplierReqVo;
import com.autoyol.field.progress.manage.response.tackback.PickDeliverLogVo;
import com.autoyol.field.progress.manage.response.tackback.PickDeliverPageRespVo;
import com.autoyol.field.progress.manage.util.ConvertBeanUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.mq.MQConstraint.*;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.convertToDate;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.dateToStringFormat;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@Service
public class PickDeliverLogService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    PickDeliverOrderInfoMapper pickDeliverOrderInfoMapper;

    @Resource
    PickDeliverLogMapper pickDeliverLogMapper;

    @Resource
    PickDeliverPickInfoMapper pickDeliverPickInfoMapper;

    @Resource
    PickDeliverPickInfoLogMapper pickDeliverPickInfoLogMapper;

    @Resource
    PickDeliverDeliverInfoMapper pickDeliverDeliverInfoMapper;

    @Resource
    PickDeliverDeliverInfoLogMapper pickDeliverDeliverInfoLogMapper;

    @Resource
    TransOrderInfoMapper transOrderInfoMapper;

    @Resource
    TransVehicleInfoMapper transVehicleInfoMapper;

    @Resource
    TransRentUserInfoMapper transRentUserInfoMapper;

    @Resource
    TransOrderInfoLogMapper transOrderInfoLogMapper;

    @Resource
    PickDeliverFeeLogMapper pickDeliverFeeLogMapper;

    @Resource
    FeeRecordMapper feeRecordMapper;

    @Resource
    FeeRecordLogMapper feeRecordLogMapper;

    @Resource
    PickDeliverFileMapper pickDeliverFileMapper;

    @Resource
    PickDeliverScheduleInfoMapper pickDeliverScheduleInfoMapper;

    @Resource
    PickDeliverScheduleInfoLogMapper pickDeliverScheduleInfoLogMapper;

    @Resource
    SupplierAccountMapper supplierAccountMapper;

    @Resource
    private AppUserInfoMapper appUserInfoMapper;

    @Resource
    PickDeliverTrxService pickDeliverTrxService;

    @Resource
    MemberService memberService;

    @Resource
    MsgService msgService;

    @Resource
    DictCache dictCache;

    @Value("${renter.back.msg.scene.source:2,6,7,9,10,15,16,30,34}")
    private String renterBackSceneSource;

    @Value("${progress.feedback.to.trans.queue}")
    private String progressFeedbackToTransQueue;

    @Value("${db.renyun.switch}")
    private String dbRenYunSwitch;

    public PickDeliverPageRespVo queryChangeLog(PickDeliverLogPageReqVo reqVo) {
        PickDeliverPageRespVo pageListRespVo = new PickDeliverPageRespVo();
        pageListRespVo.setPageNo(reqVo.getPageNo());
        pageListRespVo.setPageSize(reqVo.getPageSize());

        PickDeliverLogEntity pickDeliverLogEntity = new PickDeliverLogEntity();
        pickDeliverLogEntity.setServiceOrderNo(reqVo.getOrderNo());
        pickDeliverLogEntity.setModuleType(reqVo.getModuleType());
        pickDeliverLogEntity.setStart((reqVo.getPageNo() - 1) * reqVo.getPageSize());
        pickDeliverLogEntity.setCount(reqVo.getPageSize());
        int count = pickDeliverLogMapper.queryCountByCond(pickDeliverLogEntity);
        logger.info("***PRO*** 共{}条数据", count);
        pageListRespVo.setTotal(count);
        if (count <= 0) {
            return pageListRespVo;
        }
        List<PickDeliverLogEntity> entityList = pickDeliverLogMapper.queryByCond(pickDeliverLogEntity);
        pageListRespVo.setList(entityList.stream().map(entity -> {
            PickDeliverLogVo pickDeliverLogVo = new PickDeliverLogVo();
            return convertBean(pickDeliverLogVo, entity);
        }).filter(Objects::nonNull).collect(Collectors.toList()));
        return pageListRespVo;
    }

    @Transactional
    public int addScheduleLog(String handleUser, String pickDeliverNo, PickDeliverOrderInfoEntity tackBackOrder) throws Exception {
        List<SysDictEntity> dictEntityList = dictCache.getDictByTypeNameFromCache(PICK_DELIVER_SCHEDULE_STATUS);

        PickDeliverScheduleInfoEntity schedule = new PickDeliverScheduleInfoEntity();
        schedule.setPickDeliverOrderNo(pickDeliverNo);
        schedule.setServiceType(tackBackOrder.getServiceType());
        PickDeliverScheduleInfoEntity scheduleInfoEntity = pickDeliverScheduleInfoMapper.selectByCond(schedule);

        PickDeliverOrderInfoEntity update = new PickDeliverOrderInfoEntity();
        update.setPickDeliverOrderNo(tackBackOrder.getPickDeliverOrderNo());
        update.setPickTime(tackBackOrder.getPickTime());
        update.setScheduleStatus(scheduleInfoEntity.getScheduleStatus());
        update.setFieldAppStatus(scheduleInfoEntity.getFieldAppStatus());
        pickDeliverOrderInfoMapper.updateSelectById(update);

        PickDeliverScheduleInfoLogEntity logEntity = new PickDeliverScheduleInfoEntity();
        ConvertBeanUtil.copyProperties(logEntity, scheduleInfoEntity);
        pickDeliverScheduleInfoLogMapper.insertSelective(logEntity);

        /*boolean isWaitSupplier = getList(getLabel1ByCode(dictEntityList, String.valueOf(INT_FIVE)), SPLIT_COMMA, Integer::parseInt)
                .contains(scheduleInfoEntity.getScheduleStatus());
        if (isWaitSupplier) {
            scheduleSendMail(tackBackOrder.getPickDeliverOrderNo(), tackBackOrder.getPickTime(), scheduleInfoEntity.getSupplierCompanyId(), handleUser);
        } else {
            sendMsg(tackBackOrder, scheduleInfoEntity);

            // TODO 进度反馈
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("messageId", UUID.randomUUID().toString().replaceAll("-", ""));
            paramMap.put("orderNo", tackBackOrder.getOrderNo());
            paramMap.put("serviceType", getServiceType(tackBackOrder.getServiceType()));
            paramMap.put("description", "订单回退");
            paramMap.put("userType", dateToStringFormat(tackBackOrder.getPickTime(), DATE_FULL));
            paramMap.put("handleTime", dateToStringFormat(convertToDate(LocalDateTime.now()), DATE_FULL_NO_CONTACT_1));
            paramMap.put("proId", "7");
            paramMap.put("nodeState", "sssss");
            paramMap.put("headName", scheduleInfoEntity.getUserName());
            paramMap.put("headPhone", scheduleInfoEntity.getUserPhone());
            msgService.sendMq(progressFeedbackToTransQueue, progressFeedbackToTransQueue, paramMap);
        }*/

        StringBuffer buffer = buildScheduleOprContent(scheduleInfoEntity);
        PickDeliverLogEntity deliverLogEntity = new PickDeliverLogEntity();
        deliverLogEntity.setServiceOrderNo(pickDeliverNo);
        deliverLogEntity.setModuleType(INT_ONE);
        deliverLogEntity.setOprContent(buffer.toString());
        deliverLogEntity.setCreateOp(handleUser);
        return pickDeliverLogMapper.insertSelective(deliverLogEntity);
    }

    private void sendMsg(PickDeliverOrderInfoEntity tackBackOrder, PickDeliverScheduleInfoEntity scheduleInfoEntity) {
        try {
            String carButler = BACK_WAIT_RECEIPT;
            String carAdmin = ADMIN_BACK_WAIT_RECEIPT;
            TransOrderInfoEntity orderInfo = transOrderInfoMapper.queryByOrderNo(tackBackOrder.getOrderNo());
            String renter = null;
            String owner = OWNER_BACK_WAIT_RECEIPT;
            if (tackBackOrder.getServiceType() == INT_FOUR && StringUtils.isEmpty(orderInfo.getPartner()) &&
                    !objEquals(orderInfo.getSceneSource(), INT_THIRTY_ONE, Integer::equals)) {
                renter = RENTER_BACK_WAIT_RECEIPT_1;
            }
            if (tackBackOrder.getServiceType() == INT_FOUR && (StringUtils.isNotEmpty(orderInfo.getPartner()) ||
                    objEquals(orderInfo.getSceneSource(), INT_THIRTY_ONE, Integer::equals))) {
                renter = RENTER_BACK_WAIT_RECEIPT_3;
            }

            if (tackBackOrder.getServiceType() == INT_FOUR && !objEquals(orderInfo.getOfflineOrderType(), INT_NINE, Integer::equals) &&
                    getList(renterBackSceneSource, SPLIT_COMMA, Integer::parseInt).contains(orderInfo.getSceneSource())) {
                renter = RENTER_BACK_WAIT_RECEIPT_2;
            }

            if (tackBackOrder.getServiceType() == INT_THREE) {
                carButler = PICK_WAIT_RECEIPT;
                carAdmin = ADMIN_PICK_WAIT_RECEIPT;
                boolean b = !objEquals(orderInfo.getSceneSource(), INT_THIRTY_ONE, Integer::equals) &&
                        !objEquals(orderInfo.getOfflineOrderType(), INT_EIGHT, Integer::equals);
                renter = !b ? RENTER_PICK_WAIT_RECEIPT_1 : RENTER_PICK_WAIT_RECEIPT_2;
                owner = OWNER_PICK_WAIT_RECEIPT;
            }

            List<SysDictEntity> dictEntityList = dictCache.getDictByTypeNameFromCache(STAR_LEVEL);

            PickDeliverPickInfoEntity pickInfo = new PickDeliverPickInfoEntity();
            pickInfo.setPickDeliverOrderNo(tackBackOrder.getPickDeliverOrderNo());
            pickInfo.setServiceType(tackBackOrder.getServiceType());
            PickDeliverPickInfoLogEntity pickInfoLog = pickDeliverPickInfoLogMapper.selectByCond(pickInfo);

            PickDeliverDeliverInfoLogEntity deliverInfo = new PickDeliverDeliverInfoLogEntity();
            deliverInfo.setPickDeliverOrderNo(tackBackOrder.getPickDeliverOrderNo());
            deliverInfo.setServiceType(tackBackOrder.getServiceType());
            PickDeliverDeliverInfoLogEntity deliverInfoLog = pickDeliverDeliverInfoLogMapper.selectByCond(deliverInfo);
            TransVehicleInfoEntity vehicleInfoEntity = transVehicleInfoMapper.queryByOrderNo(tackBackOrder.getOrderNo());
            TransRentUserInfoEntity rentUserInfoEntity = transRentUserInfoMapper.selectByOrderNo(tackBackOrder.getOrderNo());
            String pickAddr = Optional.ofNullable(pickInfoLog).filter(pick -> StringUtils.isNotEmpty(pick.getChangePickAddress()))
                    .map(PickDeliverPickInfoLogEntity::getChangePickAddress)
                    .orElse(tackBackOrder.getPickAddr());
            Date deliverTime = Optional.ofNullable(deliverInfoLog).filter(l -> Objects.nonNull(l.getDelayDeliverTime()))
                    .map(PickDeliverDeliverInfoLogEntity::getDelayDeliverTime)
                    .orElse(tackBackOrder.getDeliverTime());
            String deliverAddr = Optional.ofNullable(deliverInfoLog).filter(deliver -> StringUtils.isNotEmpty(deliver.getChangeDeliverAddress()))
                    .map(PickDeliverDeliverInfoLogEntity::getChangeDeliverAddress)
                    .orElse(tackBackOrder.getDeliverAddr());
            AppUserInfoEntity userInfoEntity = appUserInfoMapper.selectByUserId(scheduleInfoEntity.getUserId());
            deliverPersonMsg(tackBackOrder.getPickDeliverOrderNo(), tackBackOrder.getPickTime(), scheduleInfoEntity,
                    carButler, vehicleInfoEntity, pickAddr, deliverTime, deliverAddr);
            carAdminMsg(tackBackOrder.getPickTime(), vehicleInfoEntity.getEscrowAdminPhone(), carAdmin, dictEntityList,
                    vehicleInfoEntity, pickAddr, deliverAddr, userInfoEntity);
            String renterPhone = Optional.ofNullable(rentUserInfoEntity).map(TransRentUserInfoEntity::getRenterPhone).orElse(null);
            renterMsg(renterPhone, orderInfo, renter, dictEntityList, vehicleInfoEntity, pickAddr, deliverAddr, userInfoEntity);
            String ownerPhone = Optional.ofNullable(rentUserInfoEntity).map(TransRentUserInfoEntity::getOwnerPhone).orElse(null);
            ownerMsg(tackBackOrder, owner, dictEntityList, pickAddr, deliverAddr, userInfoEntity, ownerPhone);

        } catch (Exception e) {
            logger.error("***PRO*** 短信发送异常 e={}", e);
        }
    }

    private void ownerMsg(PickDeliverOrderInfoEntity tackBackOrder, String owner, List<SysDictEntity> dictEntityList, String pickAddr,
                          String deliverAddr, AppUserInfoEntity userInfoEntity, String ownerPhone) {
        Map<String, Object> paramMapOwner = msgService.buildMap(owner, ownerPhone, OWNER_BACK_WAIT_RECEIPT_MSG);
        paramMapOwner.put("star", getLabelByCode(dictEntityList, String.valueOf(userInfoEntity.getStar())));
        paramMapOwner.put("contactName", userInfoEntity.getContactName());
        paramMapOwner.put("contactPhone", userInfoEntity.getContactMobile());
        paramMapOwner.put("pickTime", dateToStringFormat(tackBackOrder.getPickTime(), DATE_FULL));
        paramMapOwner.put("deliverTime", dateToStringFormat(tackBackOrder.getDeliverTime(), DATE_FULL));
        paramMapOwner.put("pickAddr", pickAddr);
        paramMapOwner.put("deliverAddr", deliverAddr);
        msgService.sendMq(SEND_SMS_NORMAL_CHANNEL_QUEUE, SEND_SMS_NORMAL_CHANNEL_QUEUE, NEG_ZERO, paramMapOwner);
    }

    private void renterMsg(String renterPhone, TransOrderInfoEntity orderInfo, String renter, List<SysDictEntity> dictEntityList,
                           TransVehicleInfoEntity vehicleInfoEntity, String pickAddr, String deliverAddr, AppUserInfoEntity userInfoEntity) {
        if (Objects.nonNull(renter)) {
            Map<String, Object> paramMapRenter = msgService.buildMap(renter, renterPhone, RENTER_BACK_WAIT_RECEIPT_MSG);
            paramMapRenter.put("star", getLabelByCode(dictEntityList, String.valueOf(userInfoEntity.getStar())));
            paramMapRenter.put("contactName", userInfoEntity.getContactName());
            paramMapRenter.put("contactPhone", userInfoEntity.getContactMobile());
            paramMapRenter.put("rentStartTime", dateToStringFormat(orderInfo.getRentStartTime(), DATE_FULL));
            paramMapRenter.put("rentEndTime", dateToStringFormat(orderInfo.getRentEntTime(), DATE_FULL));
            // TODO 车辆号
            paramMapRenter.put("vehicleNo", vehicleInfoEntity.getVehicleNo());
            paramMapRenter.put("vehicleModel", vehicleInfoEntity.getVehicleModel());
            paramMapRenter.put("pickAddr", pickAddr);
            paramMapRenter.put("deliverAddr", deliverAddr);
            msgService.sendMq(SEND_SMS_NORMAL_CHANNEL_QUEUE, SEND_SMS_NORMAL_CHANNEL_QUEUE, NEG_ZERO, paramMapRenter);
        }
    }

    private void carAdminMsg(Date pickTime, String adminPhone, String carAdmin, List<SysDictEntity> dictEntityList,
                             TransVehicleInfoEntity vehicleInfoEntity, String pickAddr, String deliverAddr, AppUserInfoEntity userInfoEntity) {
        Map<String, Object> paramMapAdmin = msgService.buildMap(carAdmin, adminPhone, ADMIN_BACK_WAIT_RECEIPT_MSG);
        paramMapAdmin.put("star", getLabelByCode(dictEntityList, String.valueOf(userInfoEntity.getStar())));
        paramMapAdmin.put("contactName", userInfoEntity.getContactName());
        paramMapAdmin.put("contactPhone", userInfoEntity.getContactMobile());
        paramMapAdmin.put("pickTime", dateToStringFormat(pickTime, DATE_FULL));
        // TODO 车辆号
        paramMapAdmin.put("vehicleNo", vehicleInfoEntity.getVehicleNo());
        paramMapAdmin.put("pickAddr", pickAddr);
        paramMapAdmin.put("deliverAddr", deliverAddr);
        msgService.sendMq(SEND_SMS_NORMAL_CHANNEL_QUEUE, SEND_SMS_NORMAL_CHANNEL_QUEUE, NEG_ZERO, paramMapAdmin);
    }

    private void deliverPersonMsg(String tackBackOrderNo, Date pickTime, PickDeliverScheduleInfoEntity scheduleInfoEntity,
                                  String carButler, TransVehicleInfoEntity vehicleInfoEntity, String pickAddr, Date deliverTime, String deliverAddr) {
        Map<String, Object> paramMap = msgService.buildMap(carButler, scheduleInfoEntity.getUserPhone(), BACK_WAIT_RECEIPT_MSG);
        paramMap.put("tackBackorderNo", tackBackOrderNo);
        paramMap.put("pickTime", dateToStringFormat(pickTime, DATE_FULL));
        paramMap.put("deliverTime", dateToStringFormat(deliverTime, DATE_FULL));
        // TODO 车辆号
        paramMap.put("vehicleNo", vehicleInfoEntity.getVehicleNo());
        paramMap.put("vehicleModel", vehicleInfoEntity.getVehicleModel());
        paramMap.put("dayMileage", vehicleInfoEntity.getDayMileage());
        paramMap.put("pickAddr", pickAddr);
        paramMap.put("deliverAddr", deliverAddr);
        msgService.sendMq(SEND_SMS_NORMAL_CHANNEL_QUEUE, SEND_SMS_NORMAL_CHANNEL_QUEUE, NEG_ZERO, paramMap);
    }

    public void scheduleSendMail(String tackBackOrderNo, Date pickTime, Integer supplierCompanyId, TackBackBatchDispatchSupplierReqVo reqVo) {
        SupplierAccountEntity accountEntity = new SupplierAccountEntity();
        accountEntity.setCompanyId(supplierCompanyId);
        accountEntity.setIsEnable(NEG_ZERO);
        List<SupplierAccountEntity> supplierList = supplierAccountMapper.queryCond(accountEntity);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(supplierList)) {
            PickDeliverOrderInfoEntity record = new PickDeliverOrderInfoEntity();
            record.setPickDeliverOrderNo(tackBackOrderNo);
            record.setServiceType(reqVo.getServerTypeKey());
            record.setPickTime(pickTime);
            List<PickDeliverOrderInfoEntity> orderInfoList = pickDeliverOrderInfoMapper.selectByCond(record);
            if (CollectionUtils.isEmpty(orderInfoList)) {
                return;
            }
            PickDeliverOrderInfoEntity orderInfoEntity = orderInfoList.get(0);
            String msgName = orderInfoEntity.getServiceType() == 3 ? TACK_CHANGE : BACK_CHANGE;
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("tackBackorderNo", tackBackOrderNo);

            PickDeliverPickInfoEntity pickInfo = new PickDeliverPickInfoEntity();
            pickInfo.setPickDeliverOrderNo(tackBackOrderNo);
            pickInfo.setServiceType(reqVo.getServerTypeKey());
            PickDeliverPickInfoLogEntity pickInfoLog = pickDeliverPickInfoLogMapper.selectByCond(pickInfo);
            paramMap.put("pickTime", dateToStringFormat(orderInfoEntity.getPickTime(), DATE_FULL));
            paramMap.put("pickAddr", Optional.ofNullable(pickInfoLog)
                    .filter(pick -> StringUtils.isNotEmpty(pick.getChangePickAddress()))
                    .map(PickDeliverPickInfoLogEntity::getChangePickAddress)
                    .orElse(orderInfoEntity.getPickAddr()));
            supplierList.forEach(supplier -> {
                msgService.sendMail(msgName, supplier.getId().toString(), supplier.getEmail(), reqVo.getHandleUser(), paramMap);
            });
        }
    }

    private StringBuffer buildScheduleOprContent(PickDeliverScheduleInfoEntity scheduleInfoEntity) throws Exception {
        List<SysDictEntity> dictEntityList = dictCache.getDictByTypeNameFromCache(DISPATCH_TYPE);
        StringBuffer buffer = new StringBuffer();
        setTackBackLog("服务公司：", Optional.ofNullable(scheduleInfoEntity.getSupplierCompanyName()).orElse(EMPTY), buffer);
        setTackBackLog("取送车人员：", Optional.ofNullable(scheduleInfoEntity.getUserName()).orElse(EMPTY), buffer);
        setTackBackLog("电话：", Optional.ofNullable(scheduleInfoEntity.getUserPhone()).orElse(EMPTY), buffer);
        setTackBackLog("派单类别：", Optional.ofNullable(scheduleInfoEntity.getDistributeType())
                .map(type -> getLabelByCode(dictEntityList, String.valueOf(type))).orElse(EMPTY), buffer);
        return buffer;
    }

    @Transactional
    public int addFeeInfoLog(String handleUser, PickDeliverOrderInfoEntity tackBackOrder, PickDeliverFeeWithMemoEntity feeEntity,
                             List<AttrLabelEntity> labelEntities) throws Exception {
        PickDeliverFeeLogWithMemoEntity feeLog = new PickDeliverFeeLogWithMemoEntity();
        feeLog.setPickDeliverOrderNo(tackBackOrder.getPickDeliverOrderNo());
        feeLog.setServiceType(tackBackOrder.getServiceType());
        PickDeliverFeeLogWithMemoEntity log = pickDeliverFeeLogMapper.selectByCond(feeLog);
        StringBuffer buffer = buildFeeInfoOprContent(tackBackOrder, feeEntity, log);

        PickDeliverFeeLogWithMemoEntity logWithEntity = new PickDeliverFeeLogWithMemoEntity();
        ConvertBeanUtil.copyProperties(logWithEntity, feeEntity);
        pickDeliverFeeLogMapper.insertSelective(logWithEntity);

        FeeRecordEntity feeRecordEntity = new FeeRecordEntity();
        feeRecordEntity.setPickDeliverFeeId(feeEntity.getId());
        feeRecordEntity.setUpdateOp(handleUser);
        feeRecordEntity.setEditStatus(INT_ONE);
        feeRecordMapper.batchSubmit(feeRecordEntity);

        List<FeeRecordLogEntity> lastRecord = feeRecordLogMapper.selectByPickDeliverId(feeEntity.getId());
        List<FeeRecordEntity> feeRecordEntityList = feeRecordMapper.queryByPickFeeId(feeRecordEntity);
        if (!CollectionUtils.isEmpty(feeRecordEntityList)) {
            feeRecordLogMapper.batchInsertLog(feeRecordEntityList.stream().map(fee -> {
                FeeRecordLogEntity recordLogEntity = new FeeRecordEntity();
                return convertBean(recordLogEntity, fee);
            }).filter(Objects::nonNull).collect(Collectors.toList()));
            buildFeeValueOprContent(tackBackOrder.getServiceType(), buffer, lastRecord, feeRecordEntityList, labelEntities);
        }
        PickDeliverLogEntity deliverLogEntity = new PickDeliverLogEntity();
        deliverLogEntity.setServiceOrderNo(tackBackOrder.getPickDeliverOrderNo());
        deliverLogEntity.setServiceType(tackBackOrder.getServiceType());
        deliverLogEntity.setModuleType(INT_FIVE);
        deliverLogEntity.setOprContent(buffer.toString());
        deliverLogEntity.setCreateOp(handleUser);
        return pickDeliverLogMapper.insertSelective(deliverLogEntity);
    }

    private void buildFeeValueOprContent(int serviceType, StringBuffer buffer, List<FeeRecordLogEntity> lastRecordList,
                                         List<FeeRecordEntity> feeRecordEntityList,
                                         List<AttrLabelEntity> labelEntities) {
        feeRecordEntityList.forEach(fee -> {
            FeeRecordLogEntity recordLogEntity = Optional.ofNullable(lastRecordList)
                    .flatMap(lastList -> lastList.stream().filter(last -> objEquals(last.getFeeLabelId(), fee.getFeeLabelId(), Integer::equals))
                            .findFirst()).orElse(null);
            if (serviceType == INT_FOUR) {
                if (StringUtils.equalsIgnoreCase(fee.getAttrCode(), "ownerReAdjust")) {
                    if (Objects.isNull(recordLogEntity) || isChange(recordLogEntity.getFeeValue(), fee.getFeeValue(), BigDecimal::equals)) {
                        setTackBackLog("给车主调价", getLastVal(recordLogEntity, log -> Objects.nonNull(log.getFeeValue()), log -> log.getFeeValue().intValue(), EMPTY),
                                fee.getFeeValue().intValue(), buffer);
                    }
                }
                if (StringUtils.equalsIgnoreCase(fee.getAttrCode(), "renterReAjust")) {
                    if (Objects.isNull(recordLogEntity) || isChange(recordLogEntity.getFeeValue(), fee.getFeeValue(), BigDecimal::equals)) {
                        setTackBackLog("给租客调价", getLastVal(recordLogEntity, log -> Objects.nonNull(log.getFeeValue()), log -> log.getFeeValue().intValue(), EMPTY),
                                fee.getFeeValue().intValue(), buffer);
                    }
                }
            }
            AttrLabelEntity attrLabelEntity = Optional.ofNullable(labelEntities)
                    .flatMap(attrList -> attrList.stream().filter(attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), fee.getAttrCode()))
                            .findFirst()).orElse(null);
            if (Objects.nonNull(attrLabelEntity) && (Objects.isNull(recordLogEntity) || isChange(recordLogEntity.getFeeValue(), fee.getFeeValue(), BigDecimal::equals))) {
                setTackBackLog(attrLabelEntity.getAttrName(), getLastVal(recordLogEntity, log -> Objects.nonNull(log.getFeeValue()), log -> log.getFeeValue().intValue(), EMPTY),
                        fee.getFeeValue().intValue(), buffer);
            }

        });
    }

    private StringBuffer buildFeeInfoOprContent(PickDeliverOrderInfoEntity tackBackOrder, PickDeliverFeeWithMemoEntity feeWithMemoEntity,
                                                PickDeliverFeeLogWithMemoEntity logWithMemoEntity) throws Exception {
        List<String> typeList = buildTypeList(DELAY_REASON);
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);
        StringBuffer buffer = new StringBuffer();
        if (tackBackOrder.getServiceType() == INT_FOUR) {
            if (Objects.isNull(logWithMemoEntity) || isChange(logWithMemoEntity.getAdjustPriceOwnerReasonType(), feeWithMemoEntity.getAdjustPriceOwnerReasonType(), Integer::equals)) {
                setTackBackLog("给车主调价理由", getLastVal(logWithMemoEntity, log -> Objects.nonNull(log.getAdjustPriceOwnerReasonType()),
                        log -> getLabelByCode(dictMap.get(DELAY_REASON), String.valueOf(log.getAdjustPriceOwnerReasonType())), EMPTY),
                        getLabelByCode(dictMap.get(DELAY_REASON), String.valueOf(feeWithMemoEntity.getAdjustPriceOwnerReasonType())), buffer);
            }
            if (Objects.isNull(logWithMemoEntity) || isChange(logWithMemoEntity.getAdjustPriceTenantReasonType(), feeWithMemoEntity.getAdjustPriceTenantReasonType(), Integer::equals)) {
                setTackBackLog("给租客调价理由", getLastVal(logWithMemoEntity, log -> Objects.nonNull(log.getAdjustPriceTenantReasonType()),
                        log -> getLabelByCode(dictMap.get(DELAY_REASON), String.valueOf(log.getAdjustPriceTenantReasonType())), EMPTY),
                        getLabelByCode(dictMap.get(DELAY_REASON), String.valueOf(feeWithMemoEntity.getAdjustPriceTenantReasonType())), buffer);
            }
        }
        if (Objects.isNull(logWithMemoEntity) || isChange(logWithMemoEntity.getFeeMemo(), feeWithMemoEntity.getFeeMemo(), String::equals)) {
            setTackBackLog("费用备注", getLastVal(logWithMemoEntity, log -> StringUtils.isNotEmpty(log.getFeeMemo()), PickDeliverFeeLogWithMemoEntity::getFeeMemo, EMPTY),
                    feeWithMemoEntity.getFeeMemo(), buffer);
        }
        if (Objects.isNull(logWithMemoEntity) || isChange(logWithMemoEntity.getReportMemo(), feeWithMemoEntity.getReportMemo(), String::equals)) {
            setTackBackLog("报备备注", getLastVal(logWithMemoEntity, log -> StringUtils.isNotEmpty(log.getReportMemo()), PickDeliverFeeLogWithMemoEntity::getReportMemo, EMPTY),
                    feeWithMemoEntity.getReportMemo(), buffer);
        }
        if (Objects.isNull(logWithMemoEntity) || isChange(logWithMemoEntity.getFineMemo(), feeWithMemoEntity.getFineMemo(), String::equals)) {
            setTackBackLog("罚款备注", getLastVal(logWithMemoEntity, log -> StringUtils.isNotEmpty(log.getFineMemo()), PickDeliverFeeLogWithMemoEntity::getFineMemo, EMPTY),
                    feeWithMemoEntity.getFineMemo(), buffer);
        }
        return buffer;
    }

    @Transactional
    public int addTransOrderInfoLog(String handleUser, TransOrderInfoLogEntity logEntity) {
        TransOrderInfoEntity infoEntity = transOrderInfoMapper.queryByOrderNo(logEntity.getOrderNo());
        transOrderInfoLogMapper.insertSelective(logEntity);
        StringBuffer buffer = buildTransOrderOprContent(infoEntity, logEntity);
        PickDeliverLogEntity deliverLogEntity = new PickDeliverLogEntity();
        deliverLogEntity.setServiceOrderNo(logEntity.getOrderNo());
        deliverLogEntity.setModuleType(INT_FOUR);
        deliverLogEntity.setOprContent(buffer.toString());
        deliverLogEntity.setCreateOp(handleUser);
        return pickDeliverLogMapper.insertSelective(deliverLogEntity);
    }

    private StringBuffer buildTransOrderOprContent(TransOrderInfoEntity transOrderInfoEntity, TransOrderInfoLogEntity logEntity) {
        StringBuffer buffer = new StringBuffer();
        if (Objects.isNull(logEntity) || isChange(logEntity.getRenewOrderNo(), transOrderInfoEntity.getRenewOrderNo(), String::equals)) {
            setTackBackLog("续租订单编号", getLastVal(logEntity, log -> Objects.nonNull(log.getRenewOrderNo()), TransOrderInfoLogEntity::getRenewOrderNo, EMPTY),
                    transOrderInfoEntity.getRenewOrderNo(), buffer);
        }
        return buffer;
    }

    @Transactional
    public int addDeliverInfoLog(String handleUser, PickDeliverDeliverInfoEntity deliverInfoEntity, Date pickTime) throws Exception {
        PickDeliverDeliverInfoEntity deliverInfo = new PickDeliverDeliverInfoEntity();
        deliverInfo.setPickDeliverOrderNo(deliverInfoEntity.getPickDeliverOrderNo());
        deliverInfo.setServiceType(deliverInfoEntity.getServiceType());
        PickDeliverDeliverInfoLogEntity logEntity = pickDeliverDeliverInfoLogMapper.selectByCond(deliverInfo);

        PickDeliverDeliverInfoEntity newDeliver = pickDeliverDeliverInfoMapper.selectByCond(deliverInfo);

        PickDeliverDeliverInfoLogEntity newLog = new PickDeliverDeliverInfoEntity();
        ConvertBeanUtil.copyProperties(newLog, newDeliver);
        pickDeliverDeliverInfoLogMapper.insertSelective(newLog);

        if (Objects.isNull(logEntity) || !StringUtils.equalsIgnoreCase(logEntity.getChangeDeliverAddress(), newDeliver.getChangeDeliverAddress())
                || isChange(logEntity.getDelayDeliverTime(), newDeliver.getDelayDeliverTime(), Date::equals)) {
            backWaitDispatch(handleUser, deliverInfoEntity.getPickDeliverOrderNo(), deliverInfoEntity.getServiceType(), pickTime);
        }

        PickDeliverFileEntity fileEntity = buildSubmitFile(handleUser, deliverInfoEntity.getPickDeliverOrderNo(), deliverInfoEntity.getServiceType());
        int updateCount = pickDeliverFileMapper.batchSubmit(fileEntity);
        List<String> typeList = buildTypeList(DELAY_REASON);
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);
        StringBuffer buffer = buildDeliverOprContent(updateCount, newDeliver, logEntity, dictMap);
        PickDeliverLogEntity deliverLogEntity = new PickDeliverLogEntity();
        deliverLogEntity.setServiceOrderNo(deliverInfoEntity.getPickDeliverOrderNo());
        deliverLogEntity.setServiceType(deliverInfoEntity.getServiceType());
        deliverLogEntity.setModuleType(INT_THREE);
        deliverLogEntity.setOprContent(buffer.toString());
        deliverLogEntity.setCreateOp(handleUser);
        return pickDeliverLogMapper.insertSelective(deliverLogEntity);
    }

    private void backWaitDispatch(String handleUser, String pickDeliverNo, Integer serviceType, Date pickTime) {
        PickDeliverOrderInfoEntity updateObj = new PickDeliverOrderInfoEntity();
        updateObj.setPickTime(pickTime);
        updateObj.setPickDeliverOrderNo(pickDeliverNo);
        updateObj.setServiceType(serviceType);
        updateObj.setScheduleStatus(INT_TOW);
        updateObj.setOprType(INT_ONE);
        updateObj.setUpdateOp(handleUser);

        PickDeliverScheduleStatusLogEntity scheduleStatusLogEntity = new PickDeliverScheduleStatusLogEntity();
        scheduleStatusLogEntity.setPickDeliverOrderNo(pickDeliverNo);
        scheduleStatusLogEntity.setServiceType(serviceType);
        scheduleStatusLogEntity.setScheduleStatus(INT_TOW);
        scheduleStatusLogEntity.setReason(TACK_BACK_ORDER_CHANGE);
        scheduleStatusLogEntity.setCreateOp(handleUser);

        pickDeliverTrxService.updateTackBackScheduleStatusTrx(updateObj, scheduleStatusLogEntity);
        PickDeliverScheduleInfoEntity schedule = new PickDeliverScheduleInfoEntity();
        schedule.setPickDeliverOrderNo(pickDeliverNo);
        schedule.setServiceType(serviceType);
        PickDeliverScheduleInfoEntity scheduleInfoEntity = pickDeliverScheduleInfoMapper.selectByCond(schedule);
        if (Objects.nonNull(scheduleInfoEntity)) {
            pickDeliverScheduleInfoLogMapper.insertSelective(scheduleInfoEntity);
        }
    }

    private PickDeliverFileEntity buildSubmitFile(String handleUser, String pickDeliverOrderNo, Integer serviceType) {
        PickDeliverFileEntity fileEntity = new PickDeliverFileEntity();
        fileEntity.setPickDeliverOrderNo(pickDeliverOrderNo);
        fileEntity.setServiceType(serviceType);
        fileEntity.setPickDeliverType(NEG_ZERO);
        fileEntity.setEditStatus(INT_ONE);
        fileEntity.setUpdateOp(handleUser);
        return fileEntity;
    }

    private StringBuffer buildDeliverOprContent(int updateCount, PickDeliverDeliverInfoEntity deliverInfoEntity, PickDeliverDeliverInfoLogEntity logEntity, Map<String, List<SysDictEntity>> dictMap) {
        StringBuffer buffer = new StringBuffer();
        if (Objects.isNull(logEntity) || isChange(logEntity.getDeliverOil(), deliverInfoEntity.getDeliverOil(), BigDecimal::equals)) {
            setTackBackLog("送达油量", getLastVal(logEntity, log -> Objects.nonNull(log.getDeliverOil()), log -> log.getDeliverOil().intValue(), EMPTY),
                    deliverInfoEntity.getDeliverOil(), buffer);
        }
        if (Objects.isNull(logEntity) || isChange(logEntity.getDeliverKilo(), deliverInfoEntity.getDeliverKilo(), BigDecimal::equals)) {
            setTackBackLog("送达公里数", getLastVal(logEntity, log -> Objects.nonNull(log.getDeliverKilo()), log -> String.valueOf(log.getDeliverKilo()), EMPTY),
                    deliverInfoEntity.getDeliverKilo(), buffer);
        }
        if (Objects.isNull(logEntity) || isChange(logEntity.getChangeDeliverAddress(), deliverInfoEntity.getChangeDeliverAddress(), String::equals)) {
            setTackBackLog("变更取车地址", getLastVal(logEntity, log -> Objects.nonNull(log.getChangeDeliverAddress()), PickDeliverDeliverInfoLogEntity::getChangeDeliverAddress, EMPTY),
                    deliverInfoEntity.getChangeDeliverAddress(), buffer);
        }
        if (Objects.isNull(logEntity) || isChange(logEntity.getDelayDeliverTime(), deliverInfoEntity.getDelayDeliverTime(), Date::equals)) {
            setTackBackLog("延期送达时间", getLastVal(logEntity, log -> Objects.nonNull(log.getDelayDeliverTime()),
                    log -> dateToStringFormat(log.getDelayDeliverTime(), DATE_NO_SECOND), EMPTY),
                    dateToStringFormat(deliverInfoEntity.getDelayDeliverTime(), DATE_NO_SECOND), buffer);
        }
        if (Objects.isNull(logEntity) || isChange(logEntity.getDelaySendReason(), deliverInfoEntity.getDelaySendReason(), Integer::equals)) {
            setTackBackLog("送达迟到原因", getLastVal(logEntity,
                    log -> Objects.nonNull(log.getDelaySendReason()), log -> getLabelByCode(dictMap.get(DELAY_REASON), String.valueOf(deliverInfoEntity.getDelaySendReason())), EMPTY),
                    getLabelByCode(dictMap.get(DELAY_REASON), String.valueOf(deliverInfoEntity.getDelaySendReason())), buffer);
        }
        if (updateCount > 0) {
            buffer.append("更改了取车照片");
        }
        return buffer;
    }

    @Transactional
    public int addPickInfoLog(String handleUser, PickDeliverPickInfoEntity pickDeliverPickInfoEntity, Date pickTime) throws Exception {
        PickDeliverPickInfoEntity pickInfo = new PickDeliverPickInfoEntity();
        pickInfo.setPickDeliverOrderNo(pickDeliverPickInfoEntity.getPickDeliverOrderNo());
        pickInfo.setServiceType(pickDeliverPickInfoEntity.getServiceType());
        PickDeliverPickInfoLogEntity logEntity = pickDeliverPickInfoLogMapper.selectByCond(pickInfo);
        PickDeliverPickInfoEntity newPick = pickDeliverPickInfoMapper.selectByCond(pickInfo);
        PickDeliverPickInfoLogEntity newLog = new PickDeliverPickInfoEntity();
        ConvertBeanUtil.copyProperties(newLog, newPick);
        pickDeliverPickInfoLogMapper.insertSelective(newLog);

        if (Objects.isNull(logEntity) || !StringUtils.equalsIgnoreCase(logEntity.getChangePickAddress(), newPick.getChangePickAddress())) {
            backWaitDispatch(handleUser, pickDeliverPickInfoEntity.getPickDeliverOrderNo(), pickDeliverPickInfoEntity.getServiceType(), pickTime);
        }

        PickDeliverFileEntity fileEntity = buildSubmitFile(handleUser, pickDeliverPickInfoEntity.getPickDeliverOrderNo(), pickDeliverPickInfoEntity.getServiceType());
        int updateCount = pickDeliverFileMapper.batchSubmit(fileEntity);
        List<String> typeList = buildTypeList(VEHICLE_STATUS);
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);
        StringBuffer buffer = buildOprContent(updateCount, newPick, logEntity, dictMap);
        PickDeliverLogEntity deliverLogEntity = new PickDeliverLogEntity();
        deliverLogEntity.setServiceOrderNo(pickDeliverPickInfoEntity.getPickDeliverOrderNo());
        deliverLogEntity.setServiceType(pickDeliverPickInfoEntity.getServiceType());
        deliverLogEntity.setModuleType(INT_TOW);
        deliverLogEntity.setOprContent(buffer.toString());
        deliverLogEntity.setCreateOp(handleUser);
        return pickDeliverLogMapper.insertSelective(deliverLogEntity);
    }

    private StringBuffer buildOprContent(int updateCount, PickDeliverPickInfoEntity pickDeliverPickInfoEntity, PickDeliverPickInfoLogEntity logEntity, Map<String, List<SysDictEntity>> dictMap) {
        StringBuffer buffer = new StringBuffer();

        if (Objects.isNull(logEntity) || isChange(logEntity.getPickOil(), pickDeliverPickInfoEntity.getPickOil(), BigDecimal::equals)) {
            setTackBackLog("取车油量", getLastVal(logEntity, log -> Objects.nonNull(log.getPickOil()), log -> log.getPickOil().intValue(), EMPTY), pickDeliverPickInfoEntity.getPickOil(), buffer);
        }
        if (Objects.isNull(logEntity) || isChange(logEntity.getPickKilo(), pickDeliverPickInfoEntity.getPickKilo(), Integer::equals)) {
            setTackBackLog("取车公里数", getLastVal(logEntity, log -> Objects.nonNull(log.getPickKilo()), log -> String.valueOf(log.getPickKilo()), EMPTY), pickDeliverPickInfoEntity.getPickKilo(), buffer);
        }
        if (Objects.isNull(logEntity) || isChange(logEntity.getVehicleStatus(), pickDeliverPickInfoEntity.getVehicleStatus(), Integer::equals)) {
            setTackBackLog("车辆状况", getLastVal(logEntity, log -> Objects.nonNull(log.getVehicleStatus()),
                    log -> getLabelByCode(dictMap.get(VEHICLE_STATUS), String.valueOf(log.getVehicleStatus())), EMPTY),
                    getLabelByCode(dictMap.get(VEHICLE_STATUS), String.valueOf(pickDeliverPickInfoEntity.getVehicleStatus())), buffer);
        }
        if (Objects.isNull(logEntity) || isChange(logEntity.getChangePickAddress(), pickDeliverPickInfoEntity.getChangePickAddress(), String::equals)) {
            setTackBackLog("变更取车地址", getLastVal(logEntity, log -> Objects.nonNull(log.getChangePickAddress()), PickDeliverPickInfoLogEntity::getChangePickAddress, EMPTY),
                    pickDeliverPickInfoEntity.getChangePickAddress(), buffer);
        }
        if (updateCount > 0) {
            buffer.append("更改了取车照片");
        }
        return buffer;
    }

}
