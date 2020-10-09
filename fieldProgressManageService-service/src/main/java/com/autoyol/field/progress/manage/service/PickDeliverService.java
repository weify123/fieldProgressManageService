package com.autoyol.field.progress.manage.service;

import ch.qos.logback.core.CoreConstants;
import com.autoyol.field.progress.manage.cache.AttrLabelCache;
import com.autoyol.field.progress.manage.cache.CityCache;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.convert.ConvertAppUser;
import com.autoyol.field.progress.manage.convertsqlserver.TackBackConvert;
import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.entity.sqlserver.AotuInfor005Entity;
import com.autoyol.field.progress.manage.entity.sqlserver.AutoCompinfoEntity;
import com.autoyol.field.progress.manage.entity.sqlserver.AutoOther001Entity;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.exception.TackBackException;
import com.autoyol.field.progress.manage.mapper.*;
import com.autoyol.field.progress.manage.request.dict.DictReqVo;
import com.autoyol.field.progress.manage.request.tackback.*;
import com.autoyol.field.progress.manage.request.user.AppUserQueryReqVo;
import com.autoyol.field.progress.manage.response.SysDictRespVo;
import com.autoyol.field.progress.manage.response.tackback.*;
import com.autoyol.field.progress.manage.response.trans.TransOrderInfoRespVo;
import com.autoyol.field.progress.manage.response.trans.TransUserInfoRespVo;
import com.autoyol.field.progress.manage.response.trans.TransVehicleInfoRespVo;
import com.autoyol.field.progress.manage.service.sqlserver.SqlServerTackBackService;
import com.dianping.cat.Cat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.constraint.FieldErrorCode.*;
import static com.autoyol.field.progress.manage.mq.MQConstraint.BACK_CANCEL;
import static com.autoyol.field.progress.manage.mq.MQConstraint.TACK_CANCEL;
import static com.autoyol.field.progress.manage.util.Chinese2Letter.getFirstLetter;
import static com.autoyol.field.progress.manage.util.ConvertBeanUtil.copyProperties;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@Service
public class PickDeliverService implements SyncService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    PickDeliverOrderInfoMapper pickDeliverOrderInfoMapper;

    @Resource
    PickDeliverScheduleInfoMapper pickDeliverScheduleInfoMapper;

    @Resource
    PickDeliverScheduleInfoLogMapper pickDeliverScheduleInfoLogMapper;

    @Resource
    TransVehicleInfoMapper transVehicleInfoMapper;

    @Resource
    TransOrderInfoMapper transOrderInfoMapper;

    @Resource
    TransRentUserInfoMapper transRentUserInfoMapper;

    @Resource
    SupplierAccountMapper supplierAccountMapper;

    @Resource
    PickDeliverPickInfoMapper pickDeliverPickInfoMapper;

    @Resource
    PickDeliverPickInfoLogMapper pickDeliverPickInfoLogMapper;

    @Resource
    PickDeliverDeliverInfoMapper pickDeliverDeliverInfoMapper;

    @Resource
    PickDeliverFeeMapper pickDeliverFeeMapper;

    @Resource
    FeeRecordMapper feeRecordMapper;

    @Resource
    AttendanceMapper attendanceMapper;

    @Resource
    AppUserInfoMapper appUserInfoMapper;

    @Resource
    DictCache dictCache;

    @Resource
    EmployCompanyMapper employCompanyMapper;

    @Resource
    CityCache cityCache;

    @Resource
    AttrLabelCache attrLabelCache;

    @Resource
    PickDeliverTrxService pickDeliverTrxService;

    @Resource
    PickDeliverLogService pickDeliverLogService;

    @Resource
    CommonService commonService;

    @Resource
    MsgService msgService;

    @Resource
    DictService dictService;

    @Value("${wait.dispatch.code:1|2|5|8}")
    private String waitDispatchCode;

    @Value("${pick.order.can.cancel:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14}")
    private String pickOrderCanCancel;

    @Value("${pick.order.can.back:5|6|8|9|11|12|13|14}")
    private String pickOrderCanBack;

    /*@Value("pick.order.can.refuse:11,12,13,14,15,16,17,18")
    private String pickOrderCanRefuse;*/

    @Value("${db.renyun.switch}")
    private String dbRenYunSwitch;

    @Resource
    AppUserMapper appUserMapper;

    @Resource
    SqlServerTackBackService sqlServerTackBackService;

    public TackBackPersonRespVo queryTackBackPerson(TackBackPersonReqVo reqVo) throws Exception {
        TackBackPersonRespVo respVo = new TackBackPersonRespVo();
        respVo.setPageNo(reqVo.getPageNo());
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(STAR_LEVEL);
            AotuInfor005Entity entity = sqlServerTackBackService.selectTransByCond(reqVo.getTransOrderNo());
            if (Objects.isNull(entity)) {
                return respVo;
            }
            EmployCompanyEntity companyEntity = employCompanyMapper.queryCompanyById(reqVo.getCompanyId());
            if (Objects.isNull(companyEntity)){
                return respVo;
            }
            AutoCompinfoEntity compinfoEntity = sqlServerTackBackService.queryCompanyBySecondName(companyEntity.getSecondCategory());
            if (Objects.isNull(compinfoEntity)) {
                return respVo;
            }
            AutoOther001Entity other001Entity = new AutoOther001Entity();
            other001Entity.setAutoOt01C002(reqVo.getUserName());
            other001Entity.setAutoOt01Sercompany(compinfoEntity.getAutoCpinfoComp2());
            other001Entity.setAutoOt01C007(entity.getAutoInf05C008());
            other001Entity.setPageSize(reqVo.getPageSize());
            other001Entity.setPage(reqVo.getStart());
            int count = sqlServerTackBackService.countTackBackPersonByCond(other001Entity);
            respVo.setTotal(count);
            if (count <= 0) {
                return respVo;
            }
            List<AutoOther001Entity> personList = sqlServerTackBackService.getTackBackPersonByCond(other001Entity);
            respVo.setList(personList.stream()
                    .map(person -> TackBackConvert.convertPerson(person, compinfoEntity, sqlServerTackBackService, dictList))
                    .sorted(Comparator.comparing(TackBackPersonVo::getFirstLetter)).collect(Collectors.toList()));
            return respVo;
        }

        TransOrderInfoEntity transOrderInfoEntity = transOrderInfoMapper.queryByOrderNo(reqVo.getTransOrderNo());
        if (Objects.isNull(transOrderInfoEntity) || StringUtils.isEmpty(transOrderInfoEntity.getBelongCity())) {
            return respVo;
        }

        AppUserQueryReqVo userQueryReqVo = new AppUserQueryReqVo();
        userQueryReqVo.setCompanyId(reqVo.getCompanyId());
        Integer cityId = cityCache.getCityIdByCityNameFromCache(transOrderInfoEntity.getBelongCity());
        userQueryReqVo.setUserName(reqVo.getUserName());

        AppUserEntity userEntity = new AppUserEntity();
        userEntity.setUserName(reqVo.getUserName());
        userEntity.setEmploymentCompanyId(reqVo.getCompanyId());
        userEntity.setCityId(cityId);
        List<AppUserEntity> userEntities = appUserMapper.queryAppUserByCond(userEntity);
        if (CollectionUtils.isEmpty(userEntities)) {
            return respVo;
        }

        if (Objects.isNull(reqVo.getCompanyId())) {
            userEntities = userEntities.stream()
                    .filter(user -> {
                        try {
                            EmployCompanyEntity companyEntity = employCompanyMapper.queryCompanyById(user.getEmploymentCompanyId());
                            List<SysDictEntity> dictEntityList = dictCache.getDictByTypeNameFromCache(COMPANY_FIRST_TYPE);
                            return getCodeByLabel(dictEntityList, companyEntity.getFirstCategory()) != INT_TOW;
                        } catch (Exception e) {
                            logger.error("查询公司异常e={}", e);
                            return false;
                        }
                    }).collect(Collectors.toList());
        }

        DictReqVo dictReqVo = new DictReqVo();
        dictReqVo.setTypeNameStr(STAR_LEVEL + SPLIT_COMMA + STATION_TYPE);
        Map<String, List<SysDictRespVo>> map = dictService.findDictByName(dictReqVo);

        List<TackBackPersonVo> backPersonVoList = buildPersonByCond(userEntities, map);
        if (CollectionUtils.isEmpty(backPersonVoList)) {
            return respVo;
        }
        respVo.setList(backPersonVoList);
        respVo.setTotal(backPersonVoList.size());
        return respVo;
    }

    private List<TackBackPersonVo> buildPersonByCond(List<AppUserEntity> userEntities, Map<String, List<SysDictRespVo>> map) {
        return userEntities.stream()
                .filter(user -> StringUtils.isNotEmpty(user.getCurStatus()) && INT_TOW > Integer.parseInt(user.getCurStatus()))
                .filter(user -> {
                    List<AttendanceEntity> attendanceEntities = attendanceMapper.selectTodayByUserId(user.getId());
                    if (CollectionUtils.isEmpty(attendanceEntities)) {
                        return false;
                    }
                    long attendanceCount = attendanceEntities.stream()
                            .filter(attendanceEntity -> objEquals(attendanceEntity.getMark(), NEG_ZERO, Integer::equals))
                            .count();
                    return attendanceCount > 0L;
                })
                .filter(user -> {
                    AppUserInfoEntity userInfoEntity = appUserInfoMapper.selectByUserId(user.getId());
                    return Objects.nonNull(userInfoEntity) && objEquals(userInfoEntity.getDistributable(), INT_ONE, Integer::equals);
                })
                .map(user -> {
                    TackBackPersonVo personVo = new TackBackPersonVo();
                    personVo.setUserId(user.getId());
                    personVo.setUserName(user.getUserName());
                    personVo.setFirstLetter(getFirstLetter(user.getUserName()));
                    personVo.setUserPhone(user.getMobile());
                    personVo.setCompanyId(user.getEmploymentCompanyId());
                    try {
                        personVo.setCompanyName(getObjOrDefault(employCompanyMapper.queryCompanyById(user.getEmploymentCompanyId()),
                                EmployCompanyEntity::getSecondCategory, null));
                    } catch (Exception e) {
                        logger.error("公司名称查询失败 e={}", e);
                        Cat.logError("公司名称查询 异常 {}", e);
                    }
                    AppUserInfoEntity userInfoEntity = appUserInfoMapper.selectByUserId(user.getId());
                    personVo.setAddress(userInfoEntity.getAddress());
                    personVo.setStarVal(getTypeValue(userInfoEntity.getStar(), STAR_LEVEL, map));
                    personVo.setStationVal(getStationStr(map, userInfoEntity.getStation()));
                    return personVo;
                })
                .sorted(Comparator.comparing(TackBackPersonVo::getFirstLetter))
                .collect(Collectors.toList());
    }

    public PickBackFeeRespVo selectTackBackFeeInfo(String tackBackOrderNo, Integer serverTypeKey, Date pickTime, Integer newUserId) throws Exception {
        PickBackFeeRespVo pickBackFeeRespVo = new PickBackFeeRespVo();

        List<String> typeList = buildTypeList(BEAR_TYPE, SUBSIDY_OWNER_REASON_TYPE, SUBSIDY_RENT_REASON_TYPE, ADJ_PRICE_OWNER_REASON_TYPE,
                ADJ_PRICE_RENT_REASON_TYPE, EXPENSE_FUEL, EXPENSE_PARK, EXPENSE_TRAFFIC, FLOW_SERVER_TYPE);
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);

        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            String serverType = getLabelByCode(dictMap.get(FLOW_SERVER_TYPE), String.valueOf(serverTypeKey));
            AotuInfor005Entity aotuInfor005Entity = sqlServerTackBackService.selectSingleByCond(tackBackOrderNo, serverType, null,
                    pickTime, Optional.ofNullable(newUserId).map(String::valueOf).orElse(null));
            if (Objects.isNull(aotuInfor005Entity)) {
                return pickBackFeeRespVo;
            }
            return TackBackConvert.convertFee(aotuInfor005Entity, sqlServerTackBackService, dictMap);
        }

        PickDeliverFeeWithMemoEntity record = new PickDeliverFeeWithMemoEntity();
        record.setPickDeliverOrderNo(tackBackOrderNo);
        record.setServiceType(serverTypeKey);
        PickDeliverFeeWithMemoEntity feeWithMemoEntity = pickDeliverFeeMapper.selectByCond(record);
        if (Objects.isNull(feeWithMemoEntity)) {
            return pickBackFeeRespVo;
        }

        copyProperties(pickBackFeeRespVo, feeWithMemoEntity);
        pickBackFeeRespVo.setBearTypeKey(feeWithMemoEntity.getBearType());
        if (Objects.nonNull(feeWithMemoEntity.getBearType())) {
            pickBackFeeRespVo.setBearTypeVal(getLabelByCode(dictMap.get(BEAR_TYPE), String.valueOf(feeWithMemoEntity.getBearType())));
        }

        PickDeliverOrderInfoEntity pickDeliverOrderInfoEntity = new PickDeliverOrderInfoEntity();
        pickDeliverOrderInfoEntity.setPickDeliverOrderNo(tackBackOrderNo);
        pickDeliverOrderInfoEntity.setPickTime(pickTime);
        pickDeliverOrderInfoEntity.setServiceType(serverTypeKey);
        List<PickDeliverOrderInfoEntity> tackBackOrderList = pickDeliverOrderInfoMapper.selectByCond(pickDeliverOrderInfoEntity);
        if (CollectionUtils.isEmpty(tackBackOrderList)) {
            return pickBackFeeRespVo;
        }

        if (commonService.checkSupplierData(newUserId, pickDeliverOrderInfoEntity.getPickDeliverOrderNo(), serverTypeKey, pickDeliverOrderInfoEntity.getOrderNo())) {
            return pickBackFeeRespVo;
        }
        PickDeliverOrderInfoEntity tackBackOrder = tackBackOrderList.get(0);
        setReason(pickBackFeeRespVo, tackBackOrder, feeWithMemoEntity, dictMap);

        FeeRecordEntity recordEntity = new FeeRecordEntity();
        recordEntity.setPickDeliverFeeId(feeWithMemoEntity.getId());
        List<FeeRecordEntity> feeRecordEntityList = feeRecordMapper.queryByPickFeeId(recordEntity);
        Map<String, FeeRecordVo> feeRecordMap = Maps.newHashMap();
        feeRecordEntityList.forEach(feeRecordEntity -> {
            FeeRecordVo feeRecordVo = new FeeRecordVo();
            feeRecordVo.setExpenseType(feeRecordEntity.getExpenseType());
            feeRecordVo.setFeeTypeKey(feeRecordEntity.getFeeType());
            if (StringUtils.isNotEmpty(feeRecordEntity.getExpenseType()) && Objects.nonNull(feeRecordEntity.getFeeType())) {
                feeRecordVo.setFeeTypeVal(getLabelByCode(dictMap.get(feeRecordEntity.getExpenseType()), String.valueOf(feeRecordEntity.getFeeType())));
            }
            List<AttrLabelEntity> attrLabelEntities = null;
            try {
                attrLabelEntities = attrLabelCache.getAttrListByBelongSysFromCache(tackBackOrder.getServiceType());
            } catch (Exception e) {
                logger.error("查詢費用标签失败 e={}", e);
                Cat.logError("查詢費用标签 异常 {}", e);
            }
            AttrLabelEntity attrLabelEntity = Optional.ofNullable(attrLabelEntities).flatMap(attrLabels -> attrLabels.stream()
                    .filter(attr -> attr.getId().equals(feeRecordEntity.getFeeLabelId()))
                    .findFirst()).orElse(null);
            if (Objects.nonNull(attrLabelEntity)) {
                feeRecordVo.setAttrCode(attrLabelEntity.getAttrCode());
                feeRecordVo.setAttrName(attrLabelEntity.getAttrName());
            }
            feeRecordVo.setFeeValue(feeRecordEntity.getFeeValue());
            if (Objects.nonNull(feeRecordVo.getAttrCode())) {
                feeRecordMap.put(feeRecordVo.getAttrCode(), feeRecordVo);
            }
        });
        pickBackFeeRespVo.setFeeRecordMap(feeRecordMap);
        return pickBackFeeRespVo;
    }

    private void setReason(PickBackFeeRespVo pickBackFeeRespVo, PickDeliverOrderInfoEntity tackBackOrder,
                           PickDeliverFeeWithMemoEntity feeWithMemoEntity, Map<String, List<SysDictEntity>> dictMap) {

        if (Objects.nonNull(feeWithMemoEntity.getAllowanceOwnerReasonType())) {
            pickBackFeeRespVo.setAllowanceOwnerReasonTypeVal(getLabelByCodeAndLabel1(dictMap.get(SUBSIDY_OWNER_REASON_TYPE),
                    getList(feeWithMemoEntity.getAllowanceOwnerReasonType(), SPLIT_COMMA, Integer::parseInt),
                    String.valueOf(tackBackOrder.getServiceType())));
        }
        if (Objects.nonNull(feeWithMemoEntity.getAllowanceTenantReasonType())) {
            pickBackFeeRespVo.setAllowanceTenantReasonTypeVal(getLabelByCodeAndLabel1(dictMap.get(SUBSIDY_RENT_REASON_TYPE),
                    getList(feeWithMemoEntity.getAllowanceTenantReasonType(), SPLIT_COMMA, Integer::parseInt),
                    String.valueOf(tackBackOrder.getServiceType())));
        }
        if (Objects.nonNull(feeWithMemoEntity.getAdjustPriceOwnerReasonType())) {
            pickBackFeeRespVo.setAdjPriceOwnerReasonTypeKey(feeWithMemoEntity.getAdjustPriceOwnerReasonType());
            pickBackFeeRespVo.setAdjPriceOwnerReasonTypeVal(getLabelByCode(dictMap.get(ADJ_PRICE_OWNER_REASON_TYPE),
                    String.valueOf(feeWithMemoEntity.getAdjustPriceOwnerReasonType())));
        }
        if (Objects.nonNull(feeWithMemoEntity.getAdjustPriceTenantReasonType())) {
            pickBackFeeRespVo.setAdjPriceTenantReasonTypeKey(feeWithMemoEntity.getAdjustPriceTenantReasonType());
            pickBackFeeRespVo.setAdjPriceTenantReasonTypeVal(getLabelByCode(dictMap.get(ADJ_PRICE_RENT_REASON_TYPE),
                    String.valueOf(feeWithMemoEntity.getAdjustPriceTenantReasonType())));
        }

    }

    public TransUserInfoRespVo selectTransUserInfo(TransSelectOrderReqVo reqVo) throws Exception {
        TransUserInfoRespVo transUserInfoRespVo = new TransUserInfoRespVo();
        if (StringUtils.isEmpty(reqVo.getTransOrderNo())) {
            return transUserInfoRespVo;
        }

        List<SysDictEntity> serverDict = dictCache.getDictByTypeNameFromCache(FLOW_SERVER_TYPE);
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            String serverType = getLabelByCode(serverDict, String.valueOf(reqVo.getServerTypeKey()));
            AotuInfor005Entity aotuInfor005Entity = sqlServerTackBackService.selectSingleByCond(reqVo.getPickDeliverOrderNo(), serverType, reqVo.getTransOrderNo(), null, null);
            if (Objects.isNull(aotuInfor005Entity)) {
                return transUserInfoRespVo;
            }
            return TackBackConvert.convertTransUser(aotuInfor005Entity);
        }

        TransRentUserInfoEntity transUserInfoEntity = transRentUserInfoMapper.selectByOrderNo(reqVo.getTransOrderNo());
        if (Objects.isNull(transUserInfoEntity)) {
            return transUserInfoRespVo;
        }
        copyProperties(transUserInfoRespVo, transUserInfoEntity);
        return transUserInfoRespVo;
    }

    public TransVehicleInfoRespVo selectTransVehicleInfo(BaseTackBackReqVo reqVo) throws Exception {
        TransVehicleInfoRespVo transVehicleInfoRespVo = new TransVehicleInfoRespVo();
        if (StringUtils.isEmpty(reqVo.getPickDeliverOrderNo())) {
            return transVehicleInfoRespVo;
        }
        List<String> typeList = buildTypeList(VEHICLE_TYPE, ENGINE_TYPE, DETECT_STATUS, FLOW_SERVER_TYPE);
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);
        Date pickTime = parse(reqVo.getPickTime(), DATE_YYYY_MM_DD_CONTACT);
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            String serverType = getLabelByCode(dictMap.get(FLOW_SERVER_TYPE), String.valueOf(reqVo.getServerTypeKey()));
            AotuInfor005Entity aotuInfor005Entity = sqlServerTackBackService.selectSingleByCond(reqVo.getPickDeliverOrderNo(), serverType, null, pickTime, null);
            if (Objects.isNull(aotuInfor005Entity)) {
                return transVehicleInfoRespVo;
            }
            return TackBackConvert.convertTransVehicle(aotuInfor005Entity, sqlServerTackBackService, dictMap);
        }

        PickDeliverOrderInfoEntity pickDeliverOrderInfoEntity = new PickDeliverOrderInfoEntity();
        pickDeliverOrderInfoEntity.setPickDeliverOrderNo(reqVo.getPickDeliverOrderNo());
        pickDeliverOrderInfoEntity.setPickTime(pickTime);
        pickDeliverOrderInfoEntity.setServiceType(reqVo.getServerTypeKey());
        List<PickDeliverOrderInfoEntity> tackBackOrderList = pickDeliverOrderInfoMapper.selectByCond(pickDeliverOrderInfoEntity);
        if (CollectionUtils.isEmpty(tackBackOrderList)) {
            return transVehicleInfoRespVo;
        }
        PickDeliverOrderInfoEntity orderInfoEntity = tackBackOrderList.get(0);
        TransVehicleInfoEntity transVehicleInfoEntity = transVehicleInfoMapper.queryByOrderNo(orderInfoEntity.getOrderNo());
        transVehicleInfoRespVo.setTakeNote(orderInfoEntity.getTakeNote());
        buildTransVehicleVo(transVehicleInfoRespVo, transVehicleInfoEntity, dictMap);
        return transVehicleInfoRespVo;
    }

    private void buildTransVehicleVo(TransVehicleInfoRespVo transVehicleInfoRespVo, TransVehicleInfoEntity transVehicleInfoEntity, Map<String, List<SysDictEntity>> dictMap) throws Exception {
        copyProperties(transVehicleInfoRespVo, transVehicleInfoEntity);
        transVehicleInfoRespVo.setVehicleTypeKey(transVehicleInfoEntity.getVehicleType());
        transVehicleInfoRespVo.setVehicleTypeVal(getLabelByCode(dictMap.get(VEHICLE_TYPE), String.valueOf(transVehicleInfoEntity.getVehicleType())));
        transVehicleInfoRespVo.setEngineTypeKey(transVehicleInfoEntity.getEngineType());
        transVehicleInfoRespVo.setEngineTypeVal(getLabelByCode(dictMap.get(ENGINE_TYPE), String.valueOf(transVehicleInfoEntity.getEngineType())));
        transVehicleInfoRespVo.setDetectStatusKey(transVehicleInfoEntity.getDetectStatus());
        transVehicleInfoRespVo.setDetectStatusVal(getLabelByCode(dictMap.get(DETECT_STATUS), String.valueOf(transVehicleInfoEntity.getDetectStatus())));
        transVehicleInfoRespVo.setOilCapacity(transVehicleInfoEntity.getOilCapacity());
    }

    public TransOrderInfoRespVo selectTransOrderInfo(String tackBackOrderNo, Integer serverTypeKey, String transOrderNo) throws Exception {
        TransOrderInfoRespVo orderInfoRespVo = new TransOrderInfoRespVo();

        if (StringUtils.isEmpty(transOrderNo)) {
            return orderInfoRespVo;
        }
        List<String> typeList = buildTypeList(TRANS_SOURCE, TRANS_SCENE_SOURCE, OFFLINE_ORDER_TYPE, FLOW_SERVER_TYPE);
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);

        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            String serverType = getLabelByCode(dictMap.get(FLOW_SERVER_TYPE), String.valueOf(serverTypeKey));
            AotuInfor005Entity aotuInfor005Entity = sqlServerTackBackService.selectSingleByCond(tackBackOrderNo, serverType, transOrderNo, null, null);
            if (Objects.isNull(aotuInfor005Entity)) {
                return orderInfoRespVo;
            }
            return TackBackConvert.convertTransOrder(aotuInfor005Entity, sqlServerTackBackService, dictMap);
        }

        TransOrderInfoEntity transOrderInfoEntity = transOrderInfoMapper.queryByOrderNo(transOrderNo);
        if (Objects.isNull(transOrderInfoEntity)) {
            return orderInfoRespVo;
        }
        buildTransOrderVo(orderInfoRespVo, transOrderInfoEntity, dictMap);
        return orderInfoRespVo;
    }

    private void buildTransOrderVo(TransOrderInfoRespVo orderInfoRespVo, TransOrderInfoEntity transOrderInfoEntity, Map<String, List<SysDictEntity>> dictMap) throws Exception {
        copyProperties(orderInfoRespVo, transOrderInfoEntity);
        orderInfoRespVo.setSourceKey(transOrderInfoEntity.getSource());
        orderInfoRespVo.setSourceVal(getLabelByCode(dictMap.get(TRANS_SOURCE), String.valueOf(transOrderInfoEntity.getSource())));
        orderInfoRespVo.setSceneSourceKey(transOrderInfoEntity.getSceneSource());
        orderInfoRespVo.setSceneSourceVal(getLabelByCode(dictMap.get(TRANS_SCENE_SOURCE), String.valueOf(transOrderInfoEntity.getSceneSource())));
        orderInfoRespVo.setOfflineOrderTypeKey(transOrderInfoEntity.getOfflineOrderType());
        orderInfoRespVo.setOfflineOrderTypeVal(getLabelByCode(dictMap.get(OFFLINE_ORDER_TYPE), String.valueOf(transOrderInfoEntity.getOfflineOrderType())));
    }

    public PickBackSelectRespVo selectByCond(PickBackSelectReqVo reqVo, Date pickTime, Integer newUserId) throws Exception {
        List<String> typeList = buildTypeList(FLOW_SERVER_TYPE, PUBLISH_TYPE, PICK_BACK_OPR_TYPE, PICK_DELIVER_SCHEDULE_STATUS,
                DELAY_REASON, IS_OWN_USER, DISPATCH_TYPE, VEHICLE_STATUS, FLOW_NODE_NAME_TYPE, CHANNEL_TYPE, SERVICE_CLASS);
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);

        PickBackSelectRespVo pickBackSelectRespVo = new PickBackSelectRespVo();

        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            String serverType = getLabelByCode(dictMap.get(FLOW_SERVER_TYPE), String.valueOf(reqVo.getServerTypeKey()));
            AotuInfor005Entity aotuInfor005Entity = sqlServerTackBackService.selectSingleByCond(reqVo.getPickDeliverOrderNo(), serverType,
                    reqVo.getTransOrderNo(), pickTime,
                    Optional.ofNullable(newUserId).map(String::valueOf).orElse(null));
            if (Objects.isNull(aotuInfor005Entity)) {
                return pickBackSelectRespVo;
            }
            return TackBackConvert.convertSelect(aotuInfor005Entity, sqlServerTackBackService, dictMap);
        }

        PickDeliverOrderInfoEntity pickDeliverOrderInfoEntity = buildPickOrderEntity(reqVo);
        pickDeliverOrderInfoEntity.setOrderNo(reqVo.getTransOrderNo());
        List<PickDeliverOrderInfoEntity> pickBackOrderInfoEntity = pickDeliverOrderInfoMapper.selectByCond(pickDeliverOrderInfoEntity);
        if (CollectionUtils.isEmpty(pickBackOrderInfoEntity)) {
            return pickBackSelectRespVo;
        }

        if (commonService.checkSupplierData(newUserId, reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), reqVo.getTransOrderNo())) {
            return pickBackSelectRespVo;
        }

        pickBackSelectRespVo.setPickOrderInfo(convertPickOrderEntityToVo(pickBackOrderInfoEntity.get(0), dictMap));

        PickDeliverScheduleInfoEntity schedule = new PickDeliverScheduleInfoEntity();
        schedule.setPickDeliverOrderNo(reqVo.getPickDeliverOrderNo());
        schedule.setServiceType(reqVo.getServerTypeKey());
        PickDeliverScheduleInfoEntity scheduleInfoEntity = pickDeliverScheduleInfoMapper.selectByCond(schedule);
        if (Objects.nonNull(scheduleInfoEntity)) {
            pickBackSelectRespVo.setScheduleInfo(convertPickScheduleEntityToVo(dictMap, scheduleInfoEntity));
        }
        PickDeliverPickInfoEntity pickInfo = new PickDeliverPickInfoEntity();
        pickInfo.setPickDeliverOrderNo(reqVo.getPickDeliverOrderNo());
        pickInfo.setServiceType(reqVo.getServerTypeKey());
        PickDeliverPickInfoEntity pickInfoEntity = pickDeliverPickInfoMapper.selectByCond(pickInfo);
        if (Objects.nonNull(pickInfoEntity)) {
            TackPickInfoVo pickInfoVo = new TackPickInfoVo();
            copyProperties(pickInfoVo, pickInfoEntity);
            pickInfoVo.setVehicleStatusKey(pickInfoEntity.getVehicleStatus());
            pickInfoVo.setVehicleStatusVal(getLabelByCode(dictMap.get(PUBLISH_TYPE), String.valueOf(pickInfoEntity.getVehicleStatus())));
            pickBackSelectRespVo.setPickInfo(pickInfoVo);
        }

        PickDeliverDeliverInfoEntity deliverInfo = new PickDeliverDeliverInfoEntity();
        deliverInfo.setPickDeliverOrderNo(reqVo.getPickDeliverOrderNo());
        deliverInfo.setServiceType(reqVo.getServerTypeKey());
        PickDeliverDeliverInfoEntity deliverInfoEntity = pickDeliverDeliverInfoMapper.selectByCond(deliverInfo);
        if (Objects.nonNull(deliverInfoEntity)) {
            TackDeliverInfoVo deliverInfoVo = new TackDeliverInfoVo();
            copyProperties(deliverInfoVo, deliverInfoEntity);
            deliverInfoVo.setDelayReasonKey(deliverInfoEntity.getDelaySendReason());
            deliverInfoVo.setDelayReasonVal(getLabelByCode(dictMap.get(DELAY_REASON), String.valueOf(deliverInfoEntity.getDelaySendReason())));
            pickBackSelectRespVo.setDeliverInfo(deliverInfoVo);
        }
        return pickBackSelectRespVo;
    }

    private PickBackScheduleInfoVo convertPickScheduleEntityToVo(Map<String, List<SysDictEntity>> dictMap, PickDeliverScheduleInfoEntity scheduleInfoEntity) throws InvocationTargetException, IllegalAccessException {
        PickBackScheduleInfoVo scheduleInfoVo = new PickBackScheduleInfoVo();
        copyProperties(scheduleInfoVo, scheduleInfoEntity);
        scheduleInfoVo.setSupplierCompanyId(scheduleInfoEntity.getSupplierCompanyId());
        scheduleInfoVo.setUserId(scheduleInfoEntity.getUserId());
        scheduleInfoVo.setIsSupplierDistributeKey(scheduleInfoEntity.getIsSupplierDistribute());
        scheduleInfoVo.setIsSupplierDistributeVal(getLabelByCode(dictMap.get(PUBLISH_TYPE), String.valueOf(scheduleInfoEntity.getIsSupplierDistribute())));
        scheduleInfoVo.setIsOwnUserKey(scheduleInfoEntity.getIsOwnPerson());
        scheduleInfoVo.setIsOwnUserVal(getLabelByCode(dictMap.get(IS_OWN_USER), String.valueOf(scheduleInfoEntity.getIsOwnPerson())));
        scheduleInfoVo.setDispatchTypeKey(scheduleInfoEntity.getDistributeType());
        scheduleInfoVo.setDispatchTypeVal(getLabelByCode(dictMap.get(DISPATCH_TYPE), String.valueOf(scheduleInfoEntity.getDistributeType())));
        scheduleInfoVo.setScheduleMemo(scheduleInfoEntity.getScheduleMemo());
        return scheduleInfoVo;
    }

    private PickOrderInfoVo convertPickOrderEntityToVo(PickDeliverOrderInfoEntity pickBackOrderInfoEntity, Map<String, List<SysDictEntity>> dictMap) throws InvocationTargetException, IllegalAccessException {
        PickOrderInfoVo pickOrderInfoVo = new PickOrderInfoVo();
        copyProperties(pickOrderInfoVo, pickBackOrderInfoEntity);
        TransOrderInfoEntity transOrderInfoEntity = transOrderInfoMapper.queryByOrderNo(pickBackOrderInfoEntity.getOrderNo());
        pickOrderInfoVo.setBelongCity(Optional.ofNullable(transOrderInfoEntity).map(TransOrderInfoLogEntity::getBelongCity).orElse(STRING_EMPTY));
        pickOrderInfoVo.setOrderType(Optional.ofNullable(transOrderInfoEntity).map(TransOrderInfoLogEntity::getOrderType).orElse(CoreConstants.EMPTY_STRING));
        pickOrderInfoVo.setServerTypeKey(pickBackOrderInfoEntity.getServiceType());
        pickOrderInfoVo.setServerTypeVal(getLabelByCode(dictMap.get(FLOW_SERVER_TYPE), String.valueOf(pickBackOrderInfoEntity.getServiceType())));
        pickOrderInfoVo.setIsSupplyKey(pickBackOrderInfoEntity.getIsSupply());
        pickOrderInfoVo.setIsSupplyVal(getLabelByCode(dictMap.get(PUBLISH_TYPE), String.valueOf(pickBackOrderInfoEntity.getIsSupply())));
        pickOrderInfoVo.setOprTypeKey(pickBackOrderInfoEntity.getOprType());
        pickOrderInfoVo.setIsSupplyVal(getLabelByCode(dictMap.get(PICK_BACK_OPR_TYPE), String.valueOf(pickBackOrderInfoEntity.getOprType())));
        pickOrderInfoVo.setScheduleStatusKey(pickBackOrderInfoEntity.getScheduleStatus());
        pickOrderInfoVo.setScheduleStatusVal(getLabelByCode(dictMap.get(PICK_DELIVER_SCHEDULE_STATUS), String.valueOf(pickBackOrderInfoEntity.getScheduleStatus())));
        pickOrderInfoVo.setFieldAppStatusVal(getLabelByCode(dictMap.get(PICK_DELIVER_SCHEDULE_STATUS), String.valueOf(pickBackOrderInfoEntity.getFieldAppStatus())));
        return pickOrderInfoVo;
    }

    public void batchDispatchSupplier(TackBackBatchDispatchSupplierReqVo reqVo) throws Exception {

        if (Objects.nonNull(reqVo.getHandleUserNo())) {
            SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(String.valueOf(reqVo.getHandleUserNo()));
            if (Objects.nonNull(accountEntity)) {
                throw new TackBackException(OPERATE_NO_PERMIT);
            }
        }
        Date pickTime = parse(reqVo.getPickTime(), DATE_YYYY_MM_DD_CONTACT);

        List<AotuInfor005Entity> syncList = null;
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            String serverType = getLabelByCode(dictMap.get(FLOW_SERVER_TYPE), String.valueOf(reqVo.getServerTypeKey()));
            syncList = sqlServerTackBackService.selectListByCond(reqVo.getPickDeliverOrderNoList(),
                    serverType, null, pickTime, null);
            if (CollectionUtils.isEmpty(syncList)) {
                throw new TackBackException(TACK_BACK_NOT_EXIST);
            }
            syncListLocal(dictMap, syncList, sqlServerTackBackService, employCompanyMapper);
        }

        DispatchEntity dispatchEntity = new DispatchEntity();
        dispatchEntity.setPickDeliverOrderNoList(reqVo.getPickDeliverOrderNoList());
        dispatchEntity.setServerTypeKey(reqVo.getServerTypeKey());
        dispatchEntity.setPickTime(pickTime);
        List<PickDeliverOrderInfoEntity> tackBackOrderList = pickDeliverOrderInfoMapper.queryWaitDispatch(dispatchEntity);
        if (CollectionUtils.isEmpty(tackBackOrderList)) {
            throw new TackBackException(TACK_BACK_NOT_EXIST);
        }

        long count = tackBackOrderList.stream()
                .filter(tackOrder -> !getList(waitDispatchCode, SPLIT_LINE, Integer::parseInt).contains(tackOrder.getScheduleStatus()))
                .count();
        if (count > 0L) {
            throw new TackBackException(NOT_WAIT_DISPATCH_NO);
        }

        List<PickDeliverScheduleInfoEntity> addScheduleList = Lists.newLinkedList();
        List<PickDeliverScheduleInfoEntity> updateScheduleList = Lists.newLinkedList();
        List<PickDeliverScheduleInfoLogEntity> addScheduleLogScheduleList = Lists.newLinkedList();

        List<PickDeliverScheduleStatusLogEntity> scheduleStatusLogList = Lists.newArrayList();
        tackBackOrderList = tackBackOrderList.stream().filter(tackOrder -> hitSearch(reqVo.getServerTypeKey(), tackOrder.getServiceType(), Integer::equals))
                .collect(Collectors.toList());
        List<PickDeliverOrderInfoEntity> updateList = tackBackOrderList.stream().map(pickOrder -> {

            try {
                if (buildPickScheduleInfo(reqVo, addScheduleList, updateScheduleList, addScheduleLogScheduleList, pickOrder)) {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
            BaseTackBackReqVo backReqVo = new BaseTackBackReqVo();
            backReqVo.setPickDeliverOrderNo(pickOrder.getPickDeliverOrderNo());
            backReqVo.setServerTypeKey(pickOrder.getServiceType());
            backReqVo.setPickTime(format(pickOrder.getPickTime(), DATE_YYYY_MM_DD_CONTACT, Locale.CHINA));
            PickDeliverOrderInfoEntity updateObj = null;
            try {
                updateObj = buildPickOrderEntity(backReqVo);
            } catch (Exception e) {
                return null;
            }
            updateObj.setOrderNo(pickOrder.getOrderNo());
            updateObj.setScheduleStatus(INT_THREE);
            updateObj.setUpdateOp(reqVo.getHandleUser());
            scheduleStatusLogList.add(buildScheduleStatusLogEntity(pickOrder.getPickDeliverOrderNo(), INT_THREE, pickOrder.getServiceType(),null, reqVo.getHandleUser()));
            return updateObj;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        pickDeliverTrxService.batchDispatchSupplier(updateList, scheduleStatusLogList, addScheduleList, updateScheduleList, addScheduleLogScheduleList);
        if (!StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            batchSendMail(reqVo, dispatchEntity.getPickTime(), addScheduleList, updateScheduleList);
        } else {
            tackBackOrderList.forEach(tackBackOrder -> {
                PickDeliverScheduleInfoEntity schedule = new PickDeliverScheduleInfoEntity();
                schedule.setPickDeliverOrderNo(tackBackOrder.getPickDeliverOrderNo());
                schedule.setServiceType(tackBackOrder.getServiceType());
                PickDeliverScheduleInfoEntity scheduleInfo = pickDeliverScheduleInfoMapper.selectByCond(schedule);
                String serviceType = getLabelByCode(dictMap.get(FLOW_SERVER_TYPE), String.valueOf(tackBackOrder.getServiceType()));
                Map<String, Object> map = buildScheduleMap(null, serviceType, null, reqVo, dictMap, scheduleInfo, msgService, employCompanyMapper);
                msgService.sendMq(sqlServer_mq_exchange, sqlServer_mq_routing_key, INT_TOW, map);
            });
        }
    }

    private void batchSendMail(TackBackBatchDispatchSupplierReqVo reqVo, Date pickTime, List<PickDeliverScheduleInfoEntity> addScheduleList, List<PickDeliverScheduleInfoEntity> updateScheduleList) {
        List<PickDeliverScheduleInfoEntity> mailList = Lists.newLinkedList();
        mailList.addAll(addScheduleList);
        mailList.addAll(updateScheduleList);
        mailList.forEach(schedule -> {
            pickDeliverLogService.scheduleSendMail(schedule.getPickDeliverOrderNo(), pickTime, schedule.getSupplierCompanyId(), reqVo);
        });
    }

    private boolean buildPickScheduleInfo(TackBackBatchDispatchSupplierReqVo reqVo, List<PickDeliverScheduleInfoEntity> addScheduleList, List<PickDeliverScheduleInfoEntity> updateScheduleList, List<PickDeliverScheduleInfoLogEntity> addScheduleLogScheduleList, PickDeliverOrderInfoEntity pickOrder) throws InvocationTargetException, IllegalAccessException {
        PickDeliverScheduleInfoEntity schedule = new PickDeliverScheduleInfoEntity();
        schedule.setPickDeliverOrderNo(pickOrder.getPickDeliverOrderNo());
        schedule.setServiceType(pickOrder.getServiceType());
        PickDeliverScheduleInfoEntity scheduleInfoEntity = pickDeliverScheduleInfoMapper.selectByCond(schedule);
        PickDeliverScheduleInfoEntity scheduleInfo = new PickDeliverScheduleInfoEntity();
        scheduleInfo.setPickDeliverOrderNo(pickOrder.getPickDeliverOrderNo());
        scheduleInfo.setServiceType(pickOrder.getServiceType());
        scheduleInfo.setScheduleStatus(INT_THREE);
        scheduleInfo.setFieldAppStatus(pickOrder.getFieldAppStatus());
        scheduleInfo.setIsSupplierDistribute(INT_ONE);
        scheduleInfo.setSupplierCompanyId(reqVo.getSupplierCompanyId());
        scheduleInfo.setEditStatus(INT_ONE);
        try {
            EmployCompanyEntity companyEntity = employCompanyMapper.queryCompanyById(reqVo.getSupplierCompanyId());
            if (Objects.isNull(companyEntity)) {
                return true;
            }
            scheduleInfo.setSupplierCompanyName(companyEntity.getSecondCategory());
        } catch (Exception e) {
            return true;
        }
        PickDeliverScheduleInfoLogEntity scheduleInfoLogEntity = new PickDeliverScheduleInfoEntity();
        if (Objects.isNull(scheduleInfoEntity)) {
            scheduleInfo.setCreateOp(reqVo.getHandleUser());
            scheduleInfo.setCreateTime(convertToDate(LocalDateTime.now()));
            addScheduleList.add(scheduleInfo);
        } else {
            copyProperties(scheduleInfoLogEntity, scheduleInfoEntity);
            scheduleInfo.setUpdateOp(reqVo.getHandleUser());
            updateScheduleList.add(scheduleInfo);
        }
        scheduleInfoLogEntity.setPickDeliverOrderNo(pickOrder.getPickDeliverOrderNo());
        scheduleInfoLogEntity.setServiceType(pickOrder.getServiceType());
        scheduleInfoLogEntity.setIsSupplierDistribute(INT_ONE);
        scheduleInfoLogEntity.setSupplierCompanyId(reqVo.getSupplierCompanyId());
        scheduleInfoLogEntity.setSupplierCompanyName(scheduleInfo.getSupplierCompanyName());
        scheduleInfoLogEntity.setCreateOp(reqVo.getHandleUser());
        addScheduleLogScheduleList.add(scheduleInfoLogEntity);
        return false;
    }

    public void orderReduce(BaseTackBackReqVo reqVo) throws Exception {
        if (Objects.nonNull(reqVo.getHandleUserNo())) {
            SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(String.valueOf(reqVo.getHandleUserNo()));
            if (Objects.nonNull(accountEntity)) {
                throw new TackBackException(OPERATE_NO_PERMIT);
            }
        }

        PickDeliverOrderInfoEntity orderInfoEntity = getPickOrderInfoList(reqVo);
        if (Objects.isNull(orderInfoEntity)) {
            throw new TackBackException(TACK_BACK_NOT_EXIST);
        }

        if (orderInfoEntity.getScheduleStatus() != NEG_ZERO) {
            throw new TackBackException(OPERATE_NO_PERMIT);
        }

        updatePickOrder(null, reqVo.getHandleUser(), orderInfoEntity, INT_ONE, NEG_ZERO);
    }

    public void updateTackBackOrderScheduleStatus(OprTackBackOrderReqVo reqVo) throws Exception {

        PickDeliverOrderInfoEntity orderInfoEntity = getPickOrderInfoList(reqVo);
        if (Objects.isNull(orderInfoEntity)) {
            throw new TackBackException(TACK_BACK_NOT_EXIST);
        }

        if (commonService.checkSupplierData(reqVo.getHandleUserNo(), orderInfoEntity.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), orderInfoEntity.getOrderNo())) {
            throw new TackBackException(OPERATE_NO_PERMIT);
        }

        List<SysDictEntity> dictEntityList = dictCache.getDictByTypeNameFromCache(PICK_DELIVER_SCHEDULE_STATUS);

        SupplierAccountEntity supplier = supplierAccountMapper.selectSupplierByName(reqVo.getHandleUser());

        Integer oprType = null;
        if (Objects.isNull(supplier)) {
            // 取消
            if (objEquals(reqVo.getScheduleStatusKey(), INT_SEVENTEEN, Integer::equals) && orderInfoEntity.getScheduleStatus() == INT_SEVENTEEN) {
                throw new TackBackException(OPERATE_NO_PERMIT);
            }
            if (objEquals(reqVo.getScheduleStatusKey(), INT_SEVENTEEN, Integer::equals) &&
                    !getList(pickOrderCanCancel, SPLIT_LINE, Integer::parseInt).contains(orderInfoEntity.getScheduleStatus())) {
                throw new TackBackException(SCHEDULE_NO_PERMIT_CANCEL);
            }
            // 回退
            if (objEquals(reqVo.getScheduleStatusKey(), INT_TOW, Integer::equals) &&
                    !getList(pickOrderCanBack, SPLIT_LINE, Integer::parseInt).contains(orderInfoEntity.getScheduleStatus())) {
                throw new TackBackException(SCHEDULE_NO_PERMIT_BACK);
            }
            if (objEquals(reqVo.getScheduleStatusKey(), INT_TOW, Integer::equals)) {
                oprType = INT_ONE;
            }
            if (objEquals(reqVo.getScheduleStatusKey(), INT_SEVENTEEN, Integer::equals)) {
                oprType = INT_TOW;
            }
            // 无拒单
            updatePickOrder(reqVo.getReason(), reqVo.getHandleUser(), orderInfoEntity, reqVo.getScheduleStatusKey(), oprType);

            sendMail(reqVo, orderInfoEntity);

            return;
        }
        boolean isCanUpdate = getList(getLabel1ByCode(dictEntityList, String.valueOf(INT_FIVE)), SPLIT_COMMA, Integer::parseInt)
                .contains(orderInfoEntity.getScheduleStatus());
        if (isCanUpdate) {
            updatePickOrder(reqVo.getReason(), reqVo.getHandleUser(), orderInfoEntity, INT_FIVE, oprType);
            return;
        }
        throw new TackBackException(OPERATE_NO_PERMIT);
    }

    private void sendMail(OprTackBackOrderReqVo reqVo, PickDeliverOrderInfoEntity orderInfoEntity) {
        PickDeliverScheduleInfoEntity entity = new PickDeliverScheduleInfoEntity();
        entity.setPickDeliverOrderNo(orderInfoEntity.getPickDeliverOrderNo());
        entity.setServiceType(orderInfoEntity.getServiceType());
        PickDeliverScheduleInfoLogEntity scheduleInfoLogEntity = pickDeliverScheduleInfoLogMapper.selectByCond(entity);
        if (Objects.nonNull(scheduleInfoLogEntity) && objEquals(reqVo.getScheduleStatusKey(), INT_SEVENTEEN, Integer::equals)) {
            SupplierAccountEntity accountEntity = new SupplierAccountEntity();
            accountEntity.setCompanyId(scheduleInfoLogEntity.getSupplierCompanyId());
            accountEntity.setIsEnable(NEG_ZERO);
            List<SupplierAccountEntity> supplierList = supplierAccountMapper.queryCond(accountEntity);
            if (CollectionUtils.isNotEmpty(supplierList)) {
                String msgName = orderInfoEntity.getServiceType() == 3 ? TACK_CANCEL : BACK_CANCEL;
                Map<String, Object> paramMap = Maps.newHashMap();
                paramMap.put("tackBackorderNo", scheduleInfoLogEntity.getPickDeliverOrderNo());

                PickDeliverPickInfoEntity pickInfo = new PickDeliverPickInfoEntity();
                pickInfo.setPickDeliverOrderNo(scheduleInfoLogEntity.getPickDeliverOrderNo());
                pickInfo.setServiceType(orderInfoEntity.getServiceType());
                PickDeliverPickInfoLogEntity pickInfoLog = pickDeliverPickInfoLogMapper.selectByCond(pickInfo);
                paramMap.put("pickTime", dateToStringFormat(orderInfoEntity.getPickTime(), DATE_FULL));
                paramMap.put("pickAddr", Optional.ofNullable(pickInfoLog)
                        .filter(pick -> StringUtils.isNotEmpty(pick.getChangePickAddress()))
                        .map(PickDeliverPickInfoLogEntity::getChangePickAddress)
                        .orElse(orderInfoEntity.getPickAddr()));
                paramMap.put("userName", scheduleInfoLogEntity.getUserName());
                paramMap.put("userPhone", scheduleInfoLogEntity.getUserPhone());
                supplierList.forEach(supplier -> {
                    msgService.sendMail(msgName, supplier.getId().toString(), supplier.getEmail(), reqVo.getHandleUser(), paramMap);
                });
            }
        }
    }

    private PickDeliverOrderInfoEntity getPickOrderInfoList(BaseTackBackReqVo reqVo) throws Exception {
        PickDeliverOrderInfoEntity pickDeliverOrderInfoEntity = buildPickOrderEntity(reqVo);
        List<PickDeliverOrderInfoEntity> tackBackList = pickDeliverOrderInfoMapper.selectByCond(pickDeliverOrderInfoEntity);
        if (CollectionUtils.isEmpty(tackBackList)) {
            return null;
        }

        PickDeliverOrderInfoEntity orderInfoEntity = tackBackList.stream().findFirst().orElse(null);
        if (Objects.isNull(orderInfoEntity)) {
            return null;
        }

        return orderInfoEntity;
    }

    private void updatePickOrder(String reason, String user, PickDeliverOrderInfoEntity orderInfoEntity, int scheduleStatus, Integer oprType) {
        orderInfoEntity.setOrderNo(orderInfoEntity.getOrderNo());
        orderInfoEntity.setScheduleStatus(scheduleStatus);
        orderInfoEntity.setOprType(oprType);
        orderInfoEntity.setUpdateOp(user);

        PickDeliverScheduleStatusLogEntity scheduleStatusLogEntity = buildScheduleStatusLogEntity(orderInfoEntity.getPickDeliverOrderNo(), scheduleStatus,
                orderInfoEntity.getServiceType(), reason, user);
        pickDeliverTrxService.updateTackBackScheduleStatusTrx(orderInfoEntity, scheduleStatusLogEntity);
        PickDeliverScheduleInfoEntity schedule = new PickDeliverScheduleInfoEntity();
        schedule.setPickDeliverOrderNo(orderInfoEntity.getPickDeliverOrderNo());
        schedule.setServiceType(orderInfoEntity.getServiceType());
        PickDeliverScheduleInfoEntity scheduleInfoEntity = pickDeliverScheduleInfoMapper.selectByCond(schedule);
        if (Objects.nonNull(scheduleInfoEntity)) {
            pickDeliverScheduleInfoLogMapper.insertSelective(scheduleInfoEntity);
        }
    }

    public PickDeliverScheduleStatusLogEntity buildScheduleStatusLogEntity(String pickOrderNo, int status, Integer serviceType, String reason, String user) {
        PickDeliverScheduleStatusLogEntity scheduleStatusLogEntity = new PickDeliverScheduleStatusLogEntity();
        scheduleStatusLogEntity.setPickDeliverOrderNo(pickOrderNo);
        scheduleStatusLogEntity.setServiceType(serviceType);
        scheduleStatusLogEntity.setScheduleStatus(status);
        scheduleStatusLogEntity.setReason(reason);
        scheduleStatusLogEntity.setCreateOp(user);
        return scheduleStatusLogEntity;
    }

    private PickDeliverOrderInfoEntity buildPickOrderEntity(BaseTackBackReqVo reqVo) throws Exception {
        PickDeliverOrderInfoEntity pickDeliverOrderInfoEntity = new PickDeliverOrderInfoEntity();
        pickDeliverOrderInfoEntity.setPickTime(parse(reqVo.getPickTime(), DATE_YYYY_MM_DD_CONTACT));
        pickDeliverOrderInfoEntity.setPickDeliverOrderNo(reqVo.getPickDeliverOrderNo());
        pickDeliverOrderInfoEntity.setServiceType(reqVo.getServerTypeKey());
        return pickDeliverOrderInfoEntity;
    }

    public EmployCompanyEntity getSupplierCompany(String newUserId){
        SupplierAccountEntity accountEntity = supplierAccountMapper.selectSupplierByUserId(newUserId);
        if (Objects.isNull(accountEntity)){
            return null;
        }
        return employCompanyMapper.queryCompanyById(accountEntity.getCompanyId());
    }

    public TackBackPageRespVo findObjByCond(TackBackPageReqVo reqVo, String newUserId) throws Exception {

        TackBackPageRespVo resList = new TackBackPageRespVo();
        resList.setPageNo(reqVo.getPageNo());
        resList.setPageSize(reqVo.getPageSize());

        List<String> typeList = buildTypeList(FLOW_SERVER_TYPE, PUBLISH_TYPE, PICK_DELIVER_SCHEDULE_STATUS, OFFLINE_ORDER_TYPE, VEHICLE_TYPE,
                IS_OWN_USER, TRANS_SCENE_SOURCE, TRANS_SOURCE);
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);

        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            EmployCompanyEntity companyEntity = getSupplierCompany(newUserId);
            resList.setTotal(sqlServerTackBackService.queryCountByCond(reqVo,
                    getObjOrDefault(companyEntity, EmployCompanyEntity::getSecondCategory, null), dictMap));
            List<AotuInfor005Entity> aotuInfor005Entities = sqlServerTackBackService.queryListByCond(reqVo,
                    getObjOrDefault(companyEntity, EmployCompanyEntity::getSecondCategory, null), dictMap);
            if (CollectionUtils.isNotEmpty(aotuInfor005Entities)) {
                resList.setList(aotuInfor005Entities.stream()
                        .map(auto -> TackBackConvert.convert(auto, sqlServerTackBackService, dictMap))
                        .collect(Collectors.toList()));
            }
            return resList;
        }

        Date pickTime = parse(reqVo.getPickTimeYear() + Optional.ofNullable(reqVo.getPickTimeMonthDay()).orElse("0101"),
                DATE_YYYY_MM_DD_CONTACT);
        reqVo.setPickTime(pickTime);
        int total = pickDeliverOrderInfoMapper.queryCountByCond(reqVo);
        resList.setTotal(total);
        if (total <= 0) {
            return resList;
        }
        List<PageCondTackBackEntity> pickBackOrderList = pickDeliverOrderInfoMapper.queryByCond(reqVo);

        if (CollectionUtils.isEmpty(pickBackOrderList)) {
            return resList;
        }
        pickBackOrderList = filterCity(reqVo, pickBackOrderList);

        if (StringUtils.isNotEmpty(newUserId)) {
            SupplierAccountEntity accountEntity = supplierAccountMapper.selectSupplierByUserId(newUserId);
            if (Objects.nonNull(accountEntity)) {
                pickBackOrderList = pickBackOrderList.stream()
                        .filter(schedule -> objEquals(accountEntity.getCompanyId(), schedule.getSupplierCompanyId(), Integer::equals))
                        .collect(Collectors.toList());
            }
        }

        List<TackBackPageVo> list = buildRespVoList(dictMap, pickBackOrderList);
        resList.setList(list);
        resList.setTotal(Math.min(total, list.size()));
        return resList;

    }

    private List<TackBackPageVo> buildRespVoList(Map<String, List<SysDictEntity>> dictMap, List<PageCondTackBackEntity> pickBackOrderList) {
        return pickBackOrderList.stream().map(tackBack -> buildPageVo(dictMap, tackBack)).collect(Collectors.toList());
    }

    private List<PageCondTackBackEntity> filterCity(TackBackPageReqVo reqVo, List<PageCondTackBackEntity> backEntityList) {
        return backEntityList.stream()
                .filter(order -> matchCityListSearch(getList(reqVo.getCityNameStr(), SPLIT_COMMA, String::valueOf), order.getBelongCity(), List::contains))
                .collect(Collectors.toList());
    }

    private TackBackPageVo buildPageVo(Map<String, List<SysDictEntity>> dictMap, PageCondTackBackEntity tackBack) {
        TackBackPageVo vo = new TackBackPageVo();
        vo.setId(tackBack.getId().longValue());
        vo.setPickDeliverOrderNo(tackBack.getPickDeliverOrderNo());
        vo.setTransOrderNo(tackBack.getOrderNo());
        vo.setServerTypeKey(tackBack.getServiceType());
        vo.setServerTypeVal(getLabelByCode(dictMap.get(FLOW_SERVER_TYPE), String.valueOf(tackBack.getServiceType())));
        vo.setIsSupplyKey(tackBack.getIsSupply());
        vo.setIsSupplyVal(getLabelByCode(dictMap.get(PUBLISH_TYPE), String.valueOf(tackBack.getIsSupply())));
        vo.setPickTime(tackBack.getPickTime());
        vo.setScheduleStatusKey(tackBack.getScheduleStatus());
        vo.setScheduleStatusVal(getLabelByCode(dictMap.get(PICK_DELIVER_SCHEDULE_STATUS), String.valueOf(tackBack.getScheduleStatus())));
        vo.setPickAddr(tackBack.getPickAddr());
        vo.setDeliverAddr(tackBack.getDeliverAddr());

        vo.setBelongCity(tackBack.getBelongCity());
        vo.setOfflineOrderTypeKey(tackBack.getOfflineOrderType());
        vo.setOfflineOrderTypeVal(getLabelByCode(dictMap.get(OFFLINE_ORDER_TYPE), String.valueOf(tackBack.getOfflineOrderType())));
        vo.setPartner(tackBack.getPartner());

        vo.setVehicleTypeKey(tackBack.getVehicleType());
        vo.setVehicleTypeVal(getLabelByCode(dictMap.get(VEHICLE_TYPE), String.valueOf(tackBack.getVehicleType())));
        vo.setVehicleNo(tackBack.getVehicleNo());
        vo.setVehicleModel(tackBack.getVehicleModel());

        vo.setTackBackUserName(tackBack.getUserName());
        vo.setServerCompany(tackBack.getSupplierCompanyName());
        vo.setIsOwnUserKey(tackBack.getIsOwnPerson());
        vo.setIsOwnUserVal(Optional.ofNullable(tackBack.getIsOwnPerson())
                .map(is -> getLabelByCode(dictMap.get(IS_OWN_USER), String.valueOf(tackBack.getIsOwnPerson()))).orElse(null));

        vo.setOwnerName(tackBack.getOwnerName());
        vo.setRenterName(tackBack.getRenterName());
        vo.setMemFlag(tackBack.getMemFlag());
        return vo;
    }
}
