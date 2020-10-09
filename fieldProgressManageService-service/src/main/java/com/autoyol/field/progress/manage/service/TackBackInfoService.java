package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.AttrLabelCache;
import com.autoyol.field.progress.manage.cache.CityCache;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.convertsqlserver.TackBackConvertEntity;
import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.entity.sqlserver.*;
import com.autoyol.field.progress.manage.exception.TackBackException;
import com.autoyol.field.progress.manage.mapper.*;
import com.autoyol.field.progress.manage.request.tackback.*;
import com.autoyol.field.progress.manage.response.tackback.TackBackCanOprStatusRespVo;
import com.autoyol.field.progress.manage.response.tackback.TackBackFileRespVo;
import com.autoyol.field.progress.manage.response.tackback.TackBackFileVo;
import com.autoyol.field.progress.manage.request.tackback.TackBackPickInfoReqVo;
import com.autoyol.field.progress.manage.service.sqlserver.SqlServerTackBackService;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
import com.dianping.cat.Cat;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.ConvertBeanUtil.copyProperties;
import static com.autoyol.field.progress.manage.util.OprUtil.*;
import static com.autoyol.field.progress.manage.constraint.FieldErrorCode.*;
import static java.util.stream.IntStream.range;

@Service
public class TackBackInfoService implements SyncService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    PickDeliverOrderInfoMapper pickDeliverOrderInfoMapper;

    @Resource
    PickDeliverPickInfoMapper pickDeliverPickInfoMapper;

    @Resource
    TransOrderInfoMapper transOrderInfoMapper;

    @Resource
    TransVehicleInfoMapper transVehicleInfoMapper;

    @Resource
    TransRentUserInfoMapper transRentUserInfoMapper;

    @Resource
    PickDeliverDeliverInfoMapper pickDeliverDeliverInfoMapper;

    @Resource
    PickDeliverFileMapper pickDeliverFileMapper;

    @Resource
    PickDeliverFeeMapper pickDeliverFeeMapper;

    @Resource
    FeeRecordMapper feeRecordMapper;

    @Resource
    SupplierAccountMapper supplierAccountMapper;

    @Resource
    PickDeliverScheduleInfoMapper pickDeliverScheduleInfoMapper;

    @Resource
    TransOrderInfoLogMapper transOrderInfoLogMapper;

    @Resource
    CommonService commonService;

    @Resource
    PickDeliverLogService pickDeliverLogService;

    @Resource
    PickDeliverTrxService pickDeliverTrxService;

    @Resource
    AttrLabelCache attrLabelCache;

    @Resource
    EmployCompanyMapper employCompanyMapper;

    @Resource
    AppUserMapper appUserMapper;

    @Resource
    CityCache cityCache;

    @Resource
    DictCache dictCache;

    @Resource
    SqlServerTackBackService sqlServerTackBackService;

    @Resource
    MsgService msgService;

    @Value("${tack.file.limit:50}")
    private String tackFileLimit;

    @Value("${pickInfo.can.edit:9|10|11|12|13|14}")
    private String pickInfoCanEdit;

    @Value("${pickInfo.can.submit:9|10|11|12|13|14}")
    private String pickInfoCanSubmit;

    @Value("${deliverInfo.can.edit:9|10|11|12|13|14}")
    private String deliverInfoCanEdit;

    @Value("${deliverInfo.can.submit:9|10|11|12|13|14}")
    private String deliverInfoCanSubmit;

    @Value("${trans.can.edit:0|1|2|5|8}")
    private String transOrderCanEdit;

    @Value("${trans.can.submit:0|1|2|5|8}")
    private String transOrderCanSubmit;

    @Value("${feeInfo.can.edit:9|10|11|12|13|14|15|16}")
    private String feeInfoCanEdit;

    @Value("${feeInfo.can.submit:9|10|11|12|13|14|15|16}")
    private String feeInfoCanSubmit;

    @Value("${scheduleInfo.can.edit:1|2|5|8}")
    private String scheduleInfoCanEdit;

    @Value("${scheduleInfo.can.submit:1|2|5|8}")
    private String scheduleInfoCanSubmit;

    @Value("${wait.dispatch.code:1|2|5|8}")
    private String waitDispatchCode;

    @Value("${pick.order.can.cancel:0|1|2|3|4|5|6|7|8|9|10|11|12}")
    private String pickOrderCanCancel;

    @Value("${pick.order.can.back:3|4|6|7|9|10|11|12}")
    private String pickOrderCanBack;

    @Value("${tack.back.can.see:17|19}")
    private String tackBackCanSee;

    @Value("${db.renyun.switch}")
    private String dbRenYunSwitch;

    CountDownLatch latch = new CountDownLatch(5);
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void syncOrder() throws Exception {
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);

        IntStream.range(2015, 2020).forEach(x -> {
            executorService.execute(() -> {
                logger.info("准备导入" + x);
                LocalDate localDate = LocalDate.ofYearDay(x, 1);
                Date pickTime = LocalDateUtil.convertToDate(localDate);
                int count = sqlServerTackBackService.selectHisCountByCond(pickTime);
                range(NEG_ZERO, countBatch(count)).forEach(i -> {
                    List<AotuInfor005Entity> entityList = sqlServerTackBackService.selectHisListByCond(pickTime, i, 2 * INT_THOUSAND);
                    entityList.forEach(entity -> {
                        try {
                            TransOrderInfoEntity transOrderInfoEntity = TackBackConvertEntity.convertTransOrder(entity, sqlServerTackBackService, dictMap);
                            TransVehicleInfoEntity transVehicleInfoEntity = TackBackConvertEntity.convertTransVehicle(entity, sqlServerTackBackService, dictMap);
                            TransRentUserInfoEntity transUserInfoEntity = TackBackConvertEntity.convertTransUser(entity);

                            PickDeliverOrderInfoEntity tackBackOrder = TackBackConvertEntity.convertPickOrderInfo(entity, sqlServerTackBackService, dictMap);
                            PickDeliverScheduleInfoEntity scheduleInfoEntity = TackBackConvertEntity.convertScheduleInfo(entity, sqlServerTackBackService,
                                    dictMap, tackBackOrder, employCompanyMapper);
                            PickDeliverPickInfoEntity pickInfo = TackBackConvertEntity.convertPickInfo(entity, sqlServerTackBackService, dictMap);
                            PickDeliverDeliverInfoEntity deliverInfo = TackBackConvertEntity.convertDeliverInfo(entity, sqlServerTackBackService, dictMap);

                            PickDeliverFeeWithMemoEntity feeMemoInfo = TackBackConvertEntity.convertFeeInfoMemo(entity, sqlServerTackBackService, dictMap);
                            scheduleInfoEntity.setServiceType(tackBackOrder.getServiceType());
                            pickInfo.setServiceType(tackBackOrder.getServiceType());
                            deliverInfo.setServiceType(tackBackOrder.getServiceType());
                            feeMemoInfo.setServiceType(tackBackOrder.getServiceType());
                            sqlServerTackBackService.syncOrder(transOrderInfoEntity, transVehicleInfoEntity, transUserInfoEntity,
                                    tackBackOrder, scheduleInfoEntity,
                                    pickInfo, deliverInfo, feeMemoInfo, entity, dictMap);
                        } catch (Exception e) {
                            logger.error("导入失败entity = {}, e ={}", entity, e);
                        }
                    });
                });
                logger.info("导入{}完毕", x);
                latch.countDown();
            });
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        logger.info("导入历史完毕");
        executorService.shutdown();
    }

    public TackBackCanOprStatusRespVo queryCanOprStatus() throws Exception {
        List<SysDictEntity> dictEntityList = dictCache.getDictByTypeNameFromCache(PICK_DELIVER_SCHEDULE_STATUS);
        TackBackCanOprStatusRespVo respVo = new TackBackCanOprStatusRespVo();
        respVo.setTackFileLimit(Integer.parseInt(tackFileLimit));
        respVo.setPickInfoCanEdit(pickInfoCanEdit);
        respVo.setPickInfoCanSubmit(pickInfoCanSubmit);
        respVo.setDeliverInfoCanEdit(deliverInfoCanEdit);
        respVo.setDeliverInfoCanSubmit(deliverInfoCanSubmit);
        respVo.setTransOrderCanEdit(transOrderCanEdit);
        respVo.setTransOrderCanSubmit(transOrderCanSubmit);
        respVo.setFeeInfoCanEdit(feeInfoCanEdit);
        respVo.setFeeInfoCanSubmit(feeInfoCanSubmit);
        respVo.setScheduleInfoCanEdit(scheduleInfoCanEdit);
        respVo.setScheduleInfoCanSubmit(scheduleInfoCanSubmit);
        respVo.setWaitDispatchCode(waitDispatchCode);
        respVo.setPickOrderCanCancel(pickOrderCanCancel);
        respVo.setPickOrderCanBack(pickOrderCanBack);
        respVo.setTackBackSee(tackBackCanSee);
        respVo.setTackBackEdit(getTackBackCanEdit(pickInfoCanEdit, deliverInfoCanEdit, transOrderCanEdit
                , feeInfoCanEdit, scheduleInfoCanEdit));
        respVo.setSupplierCanReject(getList(getLabel1ByCode(dictEntityList, String.valueOf(INT_SEVEN)), SPLIT_COMMA, Integer::parseInt)
                .stream().map(String::valueOf).reduce((x, y) -> x + SPLIT_LINE + y).orElse(null));
        return respVo;
    }

    private static String getTackBackCanEdit(String... strArray) {
        List<Integer> list = Lists.newArrayList();
        Stream.of(strArray).forEach(str -> list.addAll(getList(str, SPLIT_LINE, Integer::parseInt)));
        return list.stream().distinct().sorted().map(String::valueOf).reduce((x, y) -> x + SPLIT_LINE + y).orElse(null);
    }

    public void scheduleInfoUpdate(TackBackScheduleInfoReqVo reqVo, Date pickTime) throws Exception {
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);

        PickDeliverOrderInfoEntity tackBackOrder;
        TransOrderInfoEntity transOrderInfoEntity;
        PickDeliverScheduleInfoEntity scheduleInfoEntity;
        Integer cityId = null;
        Long id = null;
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            String serverType = getLabelByCode(dictMap.get(FLOW_SERVER_TYPE), String.valueOf(reqVo.getServerTypeKey()));
            AotuInfor005Entity entity = sqlServerTackBackService.selectSingleByCond(reqVo.getPickDeliverOrderNo(),
                    serverType, null, pickTime, null);
            if (Objects.isNull(entity)) {
                throw new TackBackException(TACK_BACK_NOT_EXIST);
            }
            id = entity.getAutoInf05Id();
            transOrderInfoEntity = TackBackConvertEntity.convertTransOrder(entity, sqlServerTackBackService, dictMap);
            cityId = getObjOrDefault(sqlServerTackBackService.getAutoDictDataEntities(sqlServer_city_code, transOrderInfoEntity.getBelongCity()),
                    x -> x.get(0).getId().intValue(), null);

            syncLocal(dictMap, transOrderInfoEntity, entity, sqlServerTackBackService, employCompanyMapper);
        }
        tackBackOrder = getTackBackOrderInfoEntity(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), pickTime);
        transOrderInfoEntity = transOrderInfoMapper.queryByOrderNo(tackBackOrder.getOrderNo());
        PickDeliverScheduleInfoEntity schedule = new PickDeliverScheduleInfoEntity();
        schedule.setPickDeliverOrderNo(reqVo.getPickDeliverOrderNo());
        schedule.setServiceType(reqVo.getServerTypeKey());
        scheduleInfoEntity = pickDeliverScheduleInfoMapper.selectByCond(schedule);
        cityId = Optional.ofNullable(cityId).orElse(cityCache.getCityIdByCityNameFromCache(transOrderInfoEntity.getBelongCity()));

        if (validCanSave(reqVo, scheduleInfoEntity)) {
            throw new TackBackException(EDIT_FAIL);
        }

        if (commonService.checkSupplierData(reqVo.getHandleUserNo(), tackBackOrder.getPickDeliverOrderNo(),
                tackBackOrder.getServiceType(), tackBackOrder.getOrderNo())) {
            throw new TackBackException(OPERATE_NO_PERMIT);
        }
        if (Objects.isNull(transOrderInfoEntity)) {
            throw new TackBackException(TACK_BACK_NOT_EXIST);
        }

        if (Objects.nonNull(scheduleInfoEntity) && isRepeatSubmit(scheduleInfoEntity.getEditStatus(), reqVo.getEditStatus())) {
            throw new TackBackException(OPERATE_NO_PERMIT);
        }

        SupplierAccountEntity accountEntity = supplierAccountMapper.selectSupplierByUserId(String.valueOf(reqVo.getHandleUserNo()));

        PickDeliverScheduleInfoEntity scheduleInfo = new PickDeliverScheduleInfoEntity();
        scheduleInfo.setPickDeliverOrderNo(reqVo.getPickDeliverOrderNo());
        int scheduleStatus = INT_SIX;
        if (Objects.isNull(accountEntity)) {

            checkCanEditAndSubmit(reqVo.getEditStatus(), tackBackOrder.getScheduleStatus(),
                    scheduleInfoCanEdit, scheduleInfoCanSubmit);

            if (objEquals(reqVo.getIsSupplierDistributeKey(), INT_ONE, Integer::equals)) {
                scheduleInfo.setScheduleStatus(INT_THREE);
            }
            if (Objects.nonNull(scheduleInfoEntity)) {
                reqVo.setSupplierCompanyId(getObjOrDefault(reqVo.getSupplierCompanyId(), Function.identity(), scheduleInfoEntity.getSupplierCompanyId()));
            }
            setCompany(reqVo.getSupplierCompanyId(), scheduleInfo);
            if (objEquals(reqVo.getIsSupplierDistributeKey(), NEG_ZERO, Integer::equals) && checkCompany(reqVo.getSupplierCompanyId())) {
                throw new TackBackException(COMPANY_ID_NOT_EXIST);
            }

        } else {
            if (Objects.isNull(scheduleInfoEntity)) {
                throw new TackBackException(OPERATE_NO_PERMIT);
            }
            setCompany(scheduleInfoEntity.getSupplierCompanyId(), scheduleInfo);
            scheduleStatus = INT_SEVEN;
        }
        if (Objects.nonNull(reqVo.getUserId())) {
            checkUserCityAndSetUser(scheduleStatus, reqVo.getUserId(), cityId, scheduleInfo);
        }
        setScheduleInfo(reqVo, scheduleInfo);

        pickDeliverTrxService.setTackBackScheduleInfo(reqVo.getHandleUser(), Objects.isNull(scheduleInfoEntity), scheduleInfo);

        if (objEquals(reqVo.getEditStatus(), INT_ONE, Integer::equals)) {
            pickDeliverLogService.addScheduleLog(reqVo.getHandleUser(), scheduleInfoEntity.getPickDeliverOrderNo(), tackBackOrder);
        }

        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            String serviceType = getLabelByCode(dictMap.get(FLOW_SERVER_TYPE), String.valueOf(tackBackOrder.getServiceType()));
            Map<String, Object> map = buildScheduleMap(id, serviceType, reqVo.getEditStatus(), reqVo, dictMap, scheduleInfo, msgService, employCompanyMapper);
            msgService.sendMq(sqlServer_mq_exchange, sqlServer_mq_routing_key, INT_TOW, map);
        }

    }

    private boolean validCanSave(TackBackScheduleInfoReqVo reqVo, PickDeliverScheduleInfoEntity scheduleInfoEntity) {
        if (objEquals(reqVo.getEditStatus(), NEG_ZERO, Integer::equals)) {
            if (objEquals(reqVo.getIsSupplierDistributeKey(), NEG_ZERO, Integer::equals)) {
                return Objects.isNull(reqVo.getSupplierCompanyId()) || Objects.isNull(reqVo.getUserId()) || Objects.isNull(reqVo.getDispatchTypeKey());
            } else {
                return Objects.isNull(reqVo.getSupplierCompanyId()) || Objects.isNull(reqVo.getDispatchTypeKey());
            }
        } else {
            if (objEquals(scheduleInfoEntity.getIsSupplierDistribute(), NEG_ZERO, Integer::equals)) {
                return Objects.isNull(scheduleInfoEntity.getIsSupplierDistribute()) || Objects.isNull(reqVo.getUserId()) || Objects.isNull(scheduleInfoEntity.getDistributeType());
            } else {
                return Objects.isNull(scheduleInfoEntity.getIsSupplierDistribute()) || Objects.isNull(scheduleInfoEntity.getDistributeType());
            }
        }
    }

    private boolean checkCompany(Integer companyId) throws Exception {
        if (Objects.nonNull(companyId)) {
            if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
                AutoCompinfoEntity compinfoEntity = sqlServerTackBackService.queryCompanyById(companyId.longValue());
                return objEquals(compinfoEntity.getAutoCpinfoComp1(), "p_cl1_003", String::equals) ||
                        objEquals(compinfoEntity.getAutoCpinfoComp1(), "p_cl1_004", String::equals);
            } else {
                EmployCompanyEntity companyEntity = employCompanyMapper.queryCompanyById(companyId);
                List<SysDictEntity> dictEntityList = dictCache.getDictByTypeNameFromCache(COMPANY_FIRST_TYPE);
                return getCodeByLabel(dictEntityList, companyEntity.getFirstCategory()) >= INT_TOW;
            }
        }
        return false;
    }

    private void setCompany(Integer companyId, PickDeliverScheduleInfoEntity scheduleInfo) throws Exception {
        EmployCompanyEntity companyEntity = employCompanyMapper.queryCompanyById(companyId);
        scheduleInfo.setSupplierCompanyId(companyId);
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            scheduleInfo.setSupplierCompanyId(companyEntity.getRenYunId());
            AutoCompinfoEntity compinfoEntity = sqlServerTackBackService.queryCompanyById(scheduleInfo.getSupplierCompanyId().longValue());
            if (Objects.nonNull(compinfoEntity)) {
                scheduleInfo.setSupplierCompanyName(getObjOrDefault(sqlServerTackBackService.getDictData(sqlServer_company, compinfoEntity.getAutoCpinfoComp2()),
                        AutoDictDataEntity::getName, null));
            }
        } else {
            scheduleInfo.setSupplierCompanyName(getObjOrDefault(companyEntity,
                    EmployCompanyEntity::getSecondCategory, null));
        }
    }

    private void setScheduleInfo(TackBackScheduleInfoReqVo reqVo, PickDeliverScheduleInfoEntity scheduleInfo) {
        scheduleInfo.setServiceType(reqVo.getServerTypeKey());
        scheduleInfo.setDistributeType(reqVo.getDispatchTypeKey());
        scheduleInfo.setIsOwnPerson(reqVo.getIsOwnUserKey());
        scheduleInfo.setScheduleMemo(reqVo.getScheduleMemo());
        scheduleInfo.setFlightNo(reqVo.getFlightNo());
        scheduleInfo.setEditStatus(reqVo.getEditStatus());
        scheduleInfo.setIsSupplierDistribute(reqVo.getIsSupplierDistributeKey());
    }

    private void checkUserCityAndSetUser(Integer scheduleStatus, Integer userId, Integer cityId, PickDeliverScheduleInfoEntity scheduleInfo) throws Exception {
        if (Objects.isNull(userId)) {
            throw new TackBackException(APP_USER_IS_EMPTY);
        }
        AppUserEntity userEntity = appUserMapper.selectByPrimaryKey(userId);
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            AutoOther001Entity other001Entity = sqlServerTackBackService.getTackBackPerson(userId.longValue());
            userEntity = new AppUserEntity();
            AutoDictDataEntity cityDict = sqlServerTackBackService.getDictData(sqlServer_city_code, other001Entity.getAutoOt01C007());
            String cityName = getObjOrDefault(cityDict, AutoDictDataEntity::getName, null);
            if (Objects.nonNull(cityName)) {
                // userEntity.setCityId(cityCache.getCityIdByCityNameFromCache(cityName));
                userEntity.setCityId(getObjOrDefault(cityDict, x -> x.getId().intValue(), null));
            }
            userEntity.setUserName(other001Entity.getAutoOt01C002());
            userEntity.setMobile(other001Entity.getAutoOt01C003());
        }
        if (Objects.isNull(userEntity)) {
            throw new TackBackException(APP_USER_NOT_EXIST);
        }
        if (!objEquals(cityId, userEntity.getCityId(), Integer::equals)) {
            throw new TackBackException(ORDER_CITY_NOT_USER_CITY);
        }
        scheduleInfo.setUserId(userId);
        scheduleInfo.setScheduleStatus(scheduleStatus);
        scheduleInfo.setFieldAppStatus(NEG_ZERO);
        scheduleInfo.setUserName(userEntity.getUserName());
        scheduleInfo.setUserPhone(userEntity.getMobile());
    }

    private void checkCanEditAndSubmit(Integer editStatus, Integer scheduleStatus, String canEdit, String canSubmit) {

        if (editStatus == 0 && !getList(canEdit, SPLIT_LINE, Integer::parseInt).contains(scheduleStatus)) {
            throw new TackBackException(SCHEDULE_NO_PERMIT_EDIT);
        }
        if (editStatus == 1 && !getList(canSubmit, SPLIT_LINE, Integer::parseInt).contains(scheduleStatus)) {
            throw new TackBackException(SCHEDULE_NO_PERMIT_SUBMIT);
        }
    }

    public void feeInfoUpdate(TackBackFeeInfoReqVo reqVo, Date pickTime) throws Exception {
        PickDeliverOrderInfoEntity tackBackOrder = getTackBackOrderInfoEntity(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), pickTime);
        if (Objects.isNull(tackBackOrder)) {
            throw new TackBackException(TACK_BACK_NOT_EXIST);
        }

        if (commonService.checkSupplierData(reqVo.getHandleUserNo(), tackBackOrder.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), tackBackOrder.getOrderNo())) {
            throw new TackBackException(OPERATE_NO_PERMIT);
        }

        checkCanEditAndSubmit(reqVo.getEditStatus(), tackBackOrder.getScheduleStatus(),
                feeInfoCanEdit, feeInfoCanSubmit);

        PickDeliverFeeWithMemoEntity record = new PickDeliverFeeWithMemoEntity();
        record.setPickDeliverOrderNo(tackBackOrder.getPickDeliverOrderNo());
        record.setServiceType(tackBackOrder.getServiceType());
        PickDeliverFeeWithMemoEntity feeWithMemoEntity = pickDeliverFeeMapper.selectByCond(record);
        if (Objects.isNull(feeWithMemoEntity)) {
            throw new TackBackException(TACK_BACK_NOT_EXIST);
        }

        FeeRecordEntity feeRecordEntity = new FeeRecordEntity();
        feeRecordEntity.setPickDeliverFeeId(feeWithMemoEntity.getId());
        feeRecordEntity.setEditStatus(NEG_ZERO);
        List<FeeRecordEntity> unSubmitList = feeRecordMapper.queryByPickFeeId(feeRecordEntity);
        if (isRepeatSubmit(feeWithMemoEntity.getEditStatus(), reqVo.getEditStatus()) && CollectionUtils.isEmpty(unSubmitList)) {
            throw new TackBackException(REPEAT_NO_PERMIT);
        }

        copyProperties(feeWithMemoEntity, reqVo);
        feeWithMemoEntity.setAdjustPriceOwnerReasonType(reqVo.getAdjPriceOwnerReasonTypeKey());
        feeWithMemoEntity.setAdjustPriceTenantReasonType(reqVo.getAdjPriceTenantReasonTypeKey());
        feeWithMemoEntity.setBearType(reqVo.getBearTypeKey());
        feeWithMemoEntity.setFeeMemo(reqVo.getFeeMemo());
        feeWithMemoEntity.setFineMemo(reqVo.getFineMemo());
        feeWithMemoEntity.setReportMemo(reqVo.getReportMemo());
        feeWithMemoEntity.setEditStatus(reqVo.getEditStatus());
        feeWithMemoEntity.setUpdateOp(reqVo.getHandleUser());

        List<FeeRecordEntity> feeRecordEntityList = null;
        List<AttrLabelEntity> labelEntities = null;
        if (CollectionUtils.isNotEmpty(reqVo.getFeeList())) {
            labelEntities = attrLabelCache.getAttrListByBelongSysFromCache(tackBackOrder.getServiceType());
            if (CollectionUtils.isEmpty(labelEntities)) {
                throw new TackBackException(CONF_NOT_COMPLETE);
            }
            feeRecordEntityList = buildFeeRecord(reqVo, feeWithMemoEntity.getId(), labelEntities);
        }
        pickDeliverTrxService.updateTackBackFeeInfo(feeWithMemoEntity, feeRecordEntityList);

        if (reqVo.getEditStatus() == INT_ONE) {
            PickDeliverFeeWithMemoEntity feeEntity = pickDeliverFeeMapper.selectByCond(record);
            pickDeliverLogService.addFeeInfoLog(reqVo.getHandleUser(), tackBackOrder, feeEntity, labelEntities);
        }
    }

    private List<FeeRecordEntity> buildFeeRecord(TackBackFeeInfoReqVo reqVo, Integer pickDeliverFeeId,
                                                 List<AttrLabelEntity> labelEntities) {
        return reqVo.getFeeList().stream().map(feeVo -> {
            AttrLabelEntity attrLabelEntity = labelEntities.stream()
                    .filter(attr -> objEquals(attr.getAttrCode(), feeVo.getAttrCode(), String::equalsIgnoreCase))
                    .findFirst().orElse(null);
            if (Objects.isNull(attrLabelEntity)) {
                return null;
            }
            FeeRecordEntity feeRecordEntity = new FeeRecordEntity();
            feeRecordEntity.setFeeLabelId(attrLabelEntity.getId());
            feeRecordEntity.setAttrCode(attrLabelEntity.getAttrCode());
            feeRecordEntity.setPickDeliverFeeId(pickDeliverFeeId);
            feeRecordEntity.setExpenseType(feeVo.getExpenseType());
            feeRecordEntity.setFeeType(feeVo.getFeeTypeKey());
            feeRecordEntity.setFeeValue(feeVo.getFeeValue());
            feeRecordEntity.setEditStatus(NEG_ZERO);
            feeRecordEntity.setUpdateOp(reqVo.getHandleUser());
            if (reqVo.getEditStatus() == INT_ONE) {
                FeeRecordLogEntity feeRecordLogEntity = new FeeRecordEntity();
                try {
                    copyProperties(feeRecordLogEntity, feeRecordEntity);
                } catch (Exception e) {
                    logger.error("费用日志信息设置失败 e={}", e);
                    Cat.logError("费用日志信息设置 异常 {}", e);
                }
            }
            return feeRecordEntity;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void orderInfoUpdate(TransOrderReqVo reqVo, Date pickTime) throws Exception {

        List<TransOrderInfoEntity> transOrderEntityList = transOrderInfoMapper.queryByOrderNoList(Arrays.asList(reqVo.getRenewOrderNo(), reqVo.getTransOrderNo()));
        if (CollectionUtils.isEmpty(transOrderEntityList)) {
            throw new TackBackException(TACK_BACK_NOT_EXIST);
        }

        TransOrderInfoEntity transOrderInfoEntity = transOrderEntityList.stream()
                .filter(trans -> trans.getOrderNo().equals(reqVo.getTransOrderNo()))
                .findFirst().orElse(null);
        if (Objects.isNull(transOrderInfoEntity)) {
            throw new TackBackException(TACK_BACK_NOT_EXIST);
        }

        if (isRepeatSubmit(transOrderInfoEntity.getEditStatus(), reqVo.getEditStatus())) {
            throw new TackBackException(REPEAT_NO_PERMIT);
        }

        PickDeliverOrderInfoEntity tackBackOrder = getTackBackOrderInfoEntity(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), pickTime);
        if (Objects.isNull(tackBackOrder)) {
            throw new TackBackException(TACK_BACK_NOT_EXIST);
        }

        if (tackBackOrder.getServiceType() != INT_FOUR || commonService.checkSupplierData(reqVo.getHandleUserNo(), tackBackOrder.getPickDeliverOrderNo(),
                reqVo.getServerTypeKey(), tackBackOrder.getOrderNo())) {
            throw new TackBackException(OPERATE_NO_PERMIT);
        }

        checkCanEditAndSubmit(reqVo.getEditStatus(), tackBackOrder.getScheduleStatus(),
                transOrderCanEdit, transOrderCanSubmit);

        List<TransVehicleInfoEntity> vehicleInfoEntityList = transVehicleInfoMapper.queryByOrderNoList(Arrays.asList(reqVo.getRenewOrderNo(),
                reqVo.getTransOrderNo()));
        if (CollectionUtils.isEmpty(vehicleInfoEntityList) || vehicleInfoEntityList.size() < 2) {
            throw new TackBackException(TACK_BACK_NOT_EXIST);
        }
        if (!StringUtils.equalsIgnoreCase(vehicleInfoEntityList.get(0).getVehicleNo(), vehicleInfoEntityList.get(1).getVehicleNo())) {
            throw new TackBackException(NOT_ONE_VEHICLE);
        }

        List<TransRentUserInfoEntity> rentUserInfoEntityList = transRentUserInfoMapper.queryByOrderNoList(Arrays.asList(reqVo.getRenewOrderNo(),
                reqVo.getTransOrderNo()));
        if (CollectionUtils.isEmpty(rentUserInfoEntityList) || rentUserInfoEntityList.size() < 2) {
            throw new TackBackException(TACK_BACK_NOT_EXIST);
        }
        if (!StringUtils.equalsIgnoreCase(rentUserInfoEntityList.get(0).getRenterNo(), rentUserInfoEntityList.get(0).getRenterNo())) {
            throw new TackBackException(NOT_ONE_RENTER);
        }

        pickDeliverTrxService.updateTransOrderInfo(reqVo);

        if (reqVo.getEditStatus() == INT_ONE) {
            TransOrderInfoLogEntity logEntity = transOrderInfoLogMapper.queryByOrderNo(transOrderInfoEntity.getOrderNo());
            pickDeliverLogService.addTransOrderInfoLog(reqVo.getHandleUser(), logEntity);
        }

    }

    public TackBackFileRespVo queryPickDeliverPic(PickDeliverPicReqVo reqVo) {
        TackBackFileRespVo fileRespVo = new TackBackFileRespVo();
        List<PickDeliverFileEntity> list = queryPickPic(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), reqVo.getPickDeliverType(), null, NEG_ZERO);
        if (CollectionUtils.isEmpty(list)) {
            return fileRespVo;
        }

        List<PickDeliverFileEntity> unSubmit = list.stream()
                .filter(file -> objEquals(file.getEditStatus(), NEG_ZERO, Integer::equals)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(unSubmit)) {
            list = unSubmit;
        }

        fileRespVo.setList(list.stream().map(entity -> {
            TackBackFileVo tackBackFileVo = new TackBackFileVo();
            tackBackFileVo.setId(entity.getId());
            tackBackFileVo.setFilePath(entity.getFilePath());
            return tackBackFileVo;
        }).collect(Collectors.toList()));
        return fileRespVo;
    }

    public TackBackFileRespVo queryPickDeliverRecord(PickDeliverPicReqVo reqVo) throws Exception {
        TackBackFileRespVo fileRespVo = new TackBackFileRespVo();
        List<PickDeliverFileEntity> list = queryPickPic(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), reqVo.getPickDeliverType(), null, INT_TOW);
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            List<SysDictEntity> dictEntityList = dictCache.getDictByTypeNameFromCache(FLOW_SERVER_TYPE);
            List<AutoRecordInfoEntity> recordList = sqlServerTackBackService.selectByCond(reqVo.getPickDeliverOrderNo(),
                    getObjOrDefault(dictEntityList, d -> d.get(0).getLabel(), null));
            if (CollectionUtils.isEmpty(recordList)) {
                return fileRespVo;
            }
            fileRespVo.setList(recordList.stream().map(entity -> {
                TackBackFileVo tackBackFileVo = new TackBackFileVo();
                tackBackFileVo.setId(entity.getAutoRedId().intValue());
                tackBackFileVo.setFilePath(entity.getAutoRedFile());
                tackBackFileVo.setRecordDesc(entity.getAutoRedRemark());
                tackBackFileVo.setRecordTime(entity.getAutoRedTime());
                tackBackFileVo.setRecordUploadTime(entity.getPubCrttime());
                return tackBackFileVo;
            }).collect(Collectors.toList()));
            return fileRespVo;
        }
        if (CollectionUtils.isEmpty(list)) {
            return fileRespVo;
        }

        fileRespVo.setList(list.stream().map(entity -> {
            TackBackFileVo tackBackFileVo = new TackBackFileVo();
            tackBackFileVo.setId(entity.getId());
            tackBackFileVo.setFilePath(entity.getFilePath());
            tackBackFileVo.setRecordDesc(entity.getRecordDesc());
            tackBackFileVo.setRecordTime(entity.getRecordTime());
            tackBackFileVo.setRecordUploadTime(entity.getCreateTime());
            return tackBackFileVo;
        }).collect(Collectors.toList()));
        return fileRespVo;
    }

    public void deliverInfoUpdate(TackBackDeliverInfoReqVo reqVo, Date pickTime, Date delayDeliverTime) throws Exception {
        PickDeliverOrderInfoEntity tackBackOrder = getTackBackOrderInfoEntity(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), pickTime);
        if (Objects.isNull(tackBackOrder)) {
            throw new TackBackException(TACK_BACK_NOT_EXIST);
        }

        if (commonService.checkSupplierData(reqVo.getHandleUserNo(), tackBackOrder.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), tackBackOrder.getOrderNo())) {
            throw new TackBackException(OPERATE_NO_PERMIT);
        }

        checkOil(reqVo.getDeliverOil(), tackBackOrder.getOrderNo());

        checkCanEditAndSubmit(reqVo.getEditStatus(), tackBackOrder.getScheduleStatus(),
                deliverInfoCanEdit, deliverInfoCanSubmit);

        PickDeliverDeliverInfoEntity deliver = new PickDeliverDeliverInfoEntity();
        deliver.setPickDeliverOrderNo(reqVo.getPickDeliverOrderNo());
        deliver.setServiceType(reqVo.getServerTypeKey());
        PickDeliverDeliverInfoEntity deliverInfoEntity = pickDeliverDeliverInfoMapper.selectByCond(deliver);
        if (Objects.isNull(deliverInfoEntity)) {
            if (reqVo.getEditStatus() == NEG_ZERO) {
                PickDeliverDeliverInfoEntity deliverDeliverInfoEntity = buildDeliverInfoEntity(reqVo, delayDeliverTime);
                deliverDeliverInfoEntity.setCreateOp(reqVo.getHandleUser());

                List<PickDeliverFileEntity> list = Lists.newArrayList();
                List<PickDeliverFileEntity> updateList = Lists.newArrayList();
                setList(reqVo.getFileList(), reqVo.getServerTypeKey(), reqVo.getPickDeliverOrderNo(), reqVo.getHandleUser(), list, updateList, INT_ONE);
                if (fileCountIsLimit(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), INT_ONE, NEG_ZERO, list, updateList)) {
                    throw new TackBackException(FILE_LIMITED);
                }

                pickDeliverTrxService.insertTackBackDeliverInfoAndFile(deliverDeliverInfoEntity, list, updateList);
                return;
            }
            throw new TackBackException(NO_PERMIT_SUBMIT);
        }

        if (reqVo.getEditStatus() == NEG_ZERO) {
            PickDeliverDeliverInfoEntity deliverDeliverInfoEntity = buildDeliverInfoEntity(reqVo, delayDeliverTime);
            deliverDeliverInfoEntity.setUpdateOp(reqVo.getHandleUser());

            List<PickDeliverFileEntity> list = Lists.newArrayList();
            List<PickDeliverFileEntity> updateList = Lists.newArrayList();
            setList(reqVo.getFileList(), reqVo.getServerTypeKey(), reqVo.getPickDeliverOrderNo(), reqVo.getHandleUser(), list, updateList, INT_ONE);
            if (fileCountIsLimit(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), INT_ONE, NEG_ZERO, list, updateList)) {
                throw new TackBackException(FILE_LIMITED);
            }

            pickDeliverTrxService.updateTackBackDeliverInfo(deliverDeliverInfoEntity, list, updateList);
            return;
        }

        List<PickDeliverFileEntity> unSubmitList = queryPickPic(deliverInfoEntity.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), INT_ONE, NEG_ZERO, NEG_ZERO);
        if (isRepeatSubmit(deliverInfoEntity.getEditStatus(), reqVo.getEditStatus()) && CollectionUtils.isEmpty(unSubmitList)) {
            throw new TackBackException(REPEAT_NO_PERMIT);
        }

        deliverInfoEntity.setUpdateOp(reqVo.getHandleUser());
        deliverInfoEntity.setEditStatus(INT_ONE);

        List<PickDeliverFileEntity> list = Lists.newArrayList();
        List<PickDeliverFileEntity> updateList = Lists.newArrayList();
        setList(reqVo.getFileList(), reqVo.getServerTypeKey(), reqVo.getPickDeliverOrderNo(), reqVo.getHandleUser(), list, updateList, INT_ONE);
        if (fileCountIsLimit(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), INT_ONE, INT_ONE, list, updateList)) {
            throw new TackBackException(FILE_LIMITED);
        }

        pickDeliverTrxService.updateTackBackDeliverInfo(deliverInfoEntity, list, updateList);
        pickDeliverLogService.addDeliverInfoLog(reqVo.getHandleUser(), deliverInfoEntity, pickTime);

    }

    private boolean isRepeatSubmit(Integer editStatus, Integer editStatus2) {
        return Objects.nonNull(editStatus) && editStatus == INT_ONE && editStatus2 == INT_ONE;
    }

    private PickDeliverDeliverInfoEntity buildDeliverInfoEntity(TackBackDeliverInfoReqVo reqVo, Date delayDeliverTime) throws InvocationTargetException, IllegalAccessException {
        PickDeliverDeliverInfoEntity deliverDeliverInfoEntity = new PickDeliverDeliverInfoEntity();
        copyProperties(deliverDeliverInfoEntity, reqVo);
        deliverDeliverInfoEntity.setServiceType(reqVo.getServerTypeKey());
        deliverDeliverInfoEntity.setDeliverOil(reqVo.getDeliverOil());
        deliverDeliverInfoEntity.setDeliverKilo(reqVo.getDeliverKilo());
        deliverDeliverInfoEntity.setChangeDeliverAddress(reqVo.getChangeDeliverAddress());
        deliverDeliverInfoEntity.setChangeDeliverLong(reqVo.getChangeDeliverLong());
        deliverDeliverInfoEntity.setChangeDeliverLat(reqVo.getChangeDeliverLat());
        deliverDeliverInfoEntity.setDelaySendReason(reqVo.getDelayReasonKey());
        deliverDeliverInfoEntity.setDelayDeliverTime(delayDeliverTime);
        return deliverDeliverInfoEntity;
    }

    public void pickInfoUpdate(TackBackPickInfoReqVo reqVo, Date pickTime) throws Exception {
        PickDeliverOrderInfoEntity tackBackOrder = getTackBackOrderInfoEntity(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), pickTime);
        if (Objects.isNull(tackBackOrder)) {
            throw new TackBackException(TACK_BACK_NOT_EXIST);
        }

        if (commonService.checkSupplierData(reqVo.getHandleUserNo(), tackBackOrder.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), tackBackOrder.getOrderNo())) {
            throw new TackBackException(OPERATE_NO_PERMIT);
        }
        checkOil(reqVo.getPickOil(), tackBackOrder.getOrderNo());
        checkCanEditAndSubmit(reqVo.getEditStatus(), tackBackOrder.getScheduleStatus(),
                pickInfoCanEdit, pickInfoCanSubmit);

        PickDeliverPickInfoEntity pick = new PickDeliverPickInfoEntity();
        pick.setPickDeliverOrderNo(reqVo.getPickDeliverOrderNo());
        pick.setServiceType(reqVo.getServerTypeKey());
        PickDeliverPickInfoEntity pickInfoEntity = pickDeliverPickInfoMapper.selectByCond(pick);
        if (Objects.isNull(pickInfoEntity)) {
            if (reqVo.getEditStatus() == NEG_ZERO) {
                PickDeliverPickInfoEntity pickDeliverPickInfoEntity = convertPickInfoVoToEntity(reqVo);
                pickDeliverPickInfoEntity.setCreateOp(reqVo.getHandleUser());

                List<PickDeliverFileEntity> list = Lists.newArrayList();
                List<PickDeliverFileEntity> updateList = Lists.newArrayList();
                setList(reqVo.getFileList(), reqVo.getServerTypeKey(), reqVo.getPickDeliverOrderNo(), reqVo.getHandleUser(), list, updateList, NEG_ZERO);

                if (fileCountIsLimit(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), NEG_ZERO, NEG_ZERO, list, updateList)) {
                    throw new TackBackException(FILE_LIMITED);
                }

                pickDeliverTrxService.insertTackBackPickInfoAndFile(pickDeliverPickInfoEntity, list, updateList);
                return;
            }
            throw new TackBackException(NO_PERMIT_SUBMIT);
        }
        if (reqVo.getEditStatus() == NEG_ZERO) {
            PickDeliverPickInfoEntity pickDeliverPickInfoEntity = convertPickInfoVoToEntity(reqVo);
            pickDeliverPickInfoEntity.setUpdateOp(reqVo.getHandleUser());

            List<PickDeliverFileEntity> list = Lists.newArrayList();
            List<PickDeliverFileEntity> updateList = Lists.newArrayList();
            setList(reqVo.getFileList(), reqVo.getServerTypeKey(), reqVo.getPickDeliverOrderNo(), reqVo.getHandleUser(), list, updateList, NEG_ZERO);

            if (fileCountIsLimit(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), NEG_ZERO, NEG_ZERO, list, updateList)) {
                throw new TackBackException(FILE_LIMITED);
            }

            pickDeliverTrxService.updateTackBackPickInfo(pickDeliverPickInfoEntity, list, updateList);
            return;
        }

        List<PickDeliverFileEntity> unSubmitList = queryPickPic(pickInfoEntity.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), NEG_ZERO, NEG_ZERO, NEG_ZERO);
        if (isRepeatSubmit(pickInfoEntity.getEditStatus(), reqVo.getEditStatus()) && CollectionUtils.isEmpty(unSubmitList)) {
            throw new TackBackException(REPEAT_NO_PERMIT);
        }

        pickInfoEntity.setUpdateOp(reqVo.getHandleUser());
        pickInfoEntity.setEditStatus(INT_ONE);

        List<PickDeliverFileEntity> list = Lists.newArrayList();
        List<PickDeliverFileEntity> updateList = Lists.newArrayList();
        setList(reqVo.getFileList(), reqVo.getServerTypeKey(), reqVo.getPickDeliverOrderNo(), reqVo.getHandleUser(), list, updateList, NEG_ZERO);

        if (fileCountIsLimit(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), NEG_ZERO, INT_ONE, list, updateList)) {
            throw new TackBackException(FILE_LIMITED);
        }

        pickDeliverTrxService.updateTackBackPickInfo(pickInfoEntity, list, updateList);
        pickDeliverLogService.addPickInfoLog(reqVo.getHandleUser(), pickInfoEntity, pickTime);

    }

    private List<PickDeliverFileEntity> queryPickPic(String tackBackOrderNo, Integer serviceType, Integer pickDeliverType, Integer editStatus, Integer fileType) {
        PickDeliverFileEntity fileEntity = new PickDeliverFileEntity();
        fileEntity.setPickDeliverOrderNo(tackBackOrderNo);
        fileEntity.setServiceType(serviceType);
        fileEntity.setPickDeliverType(pickDeliverType);
        fileEntity.setFileType(fileType);
        fileEntity.setEditStatus(editStatus);
        fileEntity.setIsDelete(NEG_ZERO);
        return pickDeliverFileMapper.queryPickDeliverPic(fileEntity);
    }

    private boolean fileCountIsLimit(String tackBackOrderNo, Integer serviceType, int pickDeliverType, int editStatus, List<PickDeliverFileEntity> list, List<PickDeliverFileEntity> updateList) {
        List<PickDeliverFileEntity> fileEntityList = queryPickPic(tackBackOrderNo, serviceType, pickDeliverType, editStatus, NEG_ZERO);
        return CollectionUtils.isNotEmpty(fileEntityList) && fileEntityList.size() - updateList.size() + list.size() > Integer.parseInt(tackFileLimit);
    }

    private PickDeliverOrderInfoEntity getTackBackOrderInfoEntity(String tackBackOrderNo, Integer serviceType, Date pickTime) {
        PickDeliverOrderInfoEntity pickDeliverOrderInfoEntity = new PickDeliverOrderInfoEntity();
        pickDeliverOrderInfoEntity.setPickDeliverOrderNo(tackBackOrderNo);
        pickDeliverOrderInfoEntity.setServiceType(serviceType);
        pickDeliverOrderInfoEntity.setPickTime(pickTime);
        return Optional.ofNullable(pickDeliverOrderInfoMapper.selectByCond(pickDeliverOrderInfoEntity)).filter(CollectionUtils::isNotEmpty).map(x -> x.get(0)).orElse(null);
    }

    private void checkOil(BigDecimal oil, String orderNo) {
        if (Objects.nonNull(oil)) {
            TransVehicleInfoEntity transVehicleInfoEntity = transVehicleInfoMapper.queryByOrderNo(orderNo);
            if (Objects.isNull(transVehicleInfoEntity)) {
                throw new TackBackException(TACK_BACK_NOT_EXIST);
            }
            if (StringUtils.isNotEmpty(transVehicleInfoEntity.getOilScaleDenominator()) &&
                    oil.intValue() > Integer.parseInt(transVehicleInfoEntity.getOilScaleDenominator())) {
                throw new TackBackException(OIL_INPUT_ERROR);
            }
        }
    }

    private void setList(List<TackBackFileVo> fileList, Integer serviceType, String tackBackOrderNo, String userName,
                         List<PickDeliverFileEntity> list, List<PickDeliverFileEntity> updateList, int pickDeliverType) {
        if (CollectionUtils.isNotEmpty(fileList)) {
            fileList.forEach(file -> {
                PickDeliverFileEntity fileEntity = buildPickDeliverFileEntity(tackBackOrderNo, file.getFilePath(), serviceType, pickDeliverType);
                if (Objects.isNull(file.getId())) {
                    fileEntity.setCreateOp(userName);
                    list.add(fileEntity);
                } else {
                    fileEntity.setId(file.getId());
                    fileEntity.setUpdateOp(userName);
                    updateList.add(fileEntity);
                }
            });
        }
    }

    private PickDeliverFileEntity buildPickDeliverFileEntity(String tackBackOrderNo, String filePath, Integer serviceType, int pickDeliverType) {
        PickDeliverFileEntity fileEntity = new PickDeliverFileEntity();
        fileEntity.setPickDeliverOrderNo(tackBackOrderNo);
        fileEntity.setServiceType(serviceType);
        fileEntity.setPickDeliverType(pickDeliverType);
        fileEntity.setFileType(NEG_ZERO);
        fileEntity.setFilePath(filePath);
        fileEntity.setEditStatus(NEG_ZERO);
        return fileEntity;
    }

    private PickDeliverPickInfoEntity convertPickInfoVoToEntity(TackBackPickInfoReqVo reqVo) throws InvocationTargetException, IllegalAccessException {
        PickDeliverPickInfoEntity pickDeliverPickInfoEntity = new PickDeliverPickInfoEntity();
        copyProperties(pickDeliverPickInfoEntity, reqVo);
        pickDeliverPickInfoEntity.setServiceType(reqVo.getServerTypeKey());
        pickDeliverPickInfoEntity.setPickOil(reqVo.getPickOil());
        pickDeliverPickInfoEntity.setPickKilo(reqVo.getPickKilo());
        pickDeliverPickInfoEntity.setChangePickAddress(reqVo.getChangePickAddress());
        pickDeliverPickInfoEntity.setChangePickLong(reqVo.getChangePickLong());
        pickDeliverPickInfoEntity.setChangePickLat(reqVo.getChangePickLat());
        pickDeliverPickInfoEntity.setVehicleStatus(reqVo.getVehicleStatusKey());
        return pickDeliverPickInfoEntity;
    }
}
