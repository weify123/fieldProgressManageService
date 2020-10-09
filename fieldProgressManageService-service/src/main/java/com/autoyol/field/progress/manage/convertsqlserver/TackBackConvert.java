package com.autoyol.field.progress.manage.convertsqlserver;

import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.entity.sqlserver.*;
import com.autoyol.field.progress.manage.request.company.CategoryVo;
import com.autoyol.field.progress.manage.response.company.CompanySecondCategory;
import com.autoyol.field.progress.manage.response.tackback.*;
import com.autoyol.field.progress.manage.response.trans.TransOrderInfoRespVo;
import com.autoyol.field.progress.manage.response.trans.TransUserInfoRespVo;
import com.autoyol.field.progress.manage.response.trans.TransVehicleInfoRespVo;
import com.autoyol.field.progress.manage.service.sqlserver.SqlServerTackBackService;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.ChConvertScoresUtil.*;
import static com.autoyol.field.progress.manage.util.Chinese2Letter.getFirstLetter;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.convertToLocalDateTime;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.format;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

public class TackBackConvert {

    public static TackBackPersonVo convertPerson(AutoOther001Entity person, AutoCompinfoEntity compinfoEntity,
                                                 SqlServerTackBackService sqlServerTackBackService, List<SysDictEntity> dictList) {
        TackBackPersonVo vo = new TackBackPersonVo();
        vo.setUserId(getObjOrDefault(person.getAutoOt01Id(), Long::intValue, null));
        vo.setUserName(person.getAutoOt01C002());
        vo.setUserPhone(person.getAutoOt01C003());
        if (StringUtils.isNotEmpty(vo.getUserName())) {
            vo.setFirstLetter(getFirstLetter(vo.getUserName()));
        }
        vo.setCompanyId(compinfoEntity.getAutoCpinfoId().intValue());
        vo.setCompanyName(getObjOrDefault(sqlServerTackBackService.getDictData(sqlServer_company, compinfoEntity.getAutoCpinfoComp2()),
                AutoDictDataEntity::getName, null));
        vo.setAddress(person.getAutoOt01C004());
        vo.setStarVal(getLabelByCode(dictList, person.getAutoOt01C005()));
        vo.setStationVal(getObjOrDefault(sqlServerTackBackService.getDictData(sqlServer_station_type, person.getAutoOt01C004())
                , AutoDictDataEntity::getName, null));
        return vo;
    }

    public static List<CategoryVo> convertCompany(Map<String, List<AutoCompinfoEntity>> map) {
        List<CategoryVo> list = Lists.newArrayList();
        map.forEach((k, v) -> {
            CategoryVo vo = new CategoryVo();
            vo.setFirstCategory(k);
            vo.setSecondCategory(v.stream().map(x -> {
                CompanySecondCategory secondCategory = new CompanySecondCategory();
                secondCategory.setCompanyId(Optional.ofNullable(x.getAutoCpinfoId()).map(Long::intValue).orElse(null));
                secondCategory.setSecondCategory(x.getAutoCpinfoComp2());
                return secondCategory;
            }).collect(Collectors.toList()));
            list.add(vo);
        });
        return list;
    }

    public static PickBackFeeRespVo convertFee(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService,
                                               Map<String, List<SysDictEntity>> dictMap) {
        PickBackFeeRespVo vo = new PickBackFeeRespVo();
        vo.setPickDeliverOrderNo(String.valueOf(entity.getAutoInf05C073()));
        Optional.ofNullable(entity.getAutoInf05Bears()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity bearDict = sqlServerTackBackService.getDictData(sqlServer_bear_type, x);
            vo.setBearTypeVal(getObjOrDefault(bearDict, AutoDictDataEntity::getName, null));
            vo.setBearTypeKey(getCodeByLabel(dictMap.get(BEAR_TYPE), vo.getBearTypeVal()));
        });
        vo.setAllowanceOwnerReasonTypeVal(entity.getAutoInf05Czcost());
        vo.setAllowanceTenantReasonTypeVal(entity.getAutoInf05Zkcost());

        Optional.ofNullable(entity.getAutoInf05Opreason()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity ownerAdjDict = sqlServerTackBackService.getDictData(sqlServer_owner_ajd_type, x);
            vo.setAdjPriceOwnerReasonTypeVal(getObjOrDefault(ownerAdjDict, AutoDictDataEntity::getName, null));
            vo.setAdjPriceOwnerReasonTypeKey(getCodeByLabel(dictMap.get(ADJ_PRICE_OWNER_REASON_TYPE), ownerAdjDict.getName()));
        });
        Optional.ofNullable(entity.getAutoInf05Rpreason()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity ownerAdjDict = sqlServerTackBackService.getDictData(sqlServer_renter_ajd_type, x);
            vo.setAdjPriceTenantReasonTypeVal(getObjOrDefault(ownerAdjDict, AutoDictDataEntity::getName, null));
            vo.setAdjPriceTenantReasonTypeKey(getObjOrDefault(ownerAdjDict, t -> Integer.parseInt(t.getCode()) - 1, null));
        });
        vo.setFeeMemo(entity.getAutoInf05C082());
        vo.setReportMemo(entity.getAutoInf05Appnote());
        vo.setFineMemo(entity.getAutoInf05C084());
        vo.setEditStatus(entity.getPubEditflag());

        Map<String, FeeRecordVo> feeRecordMap = Maps.newHashMap();

        FeeRecordVo rentEstimateFee = new FeeRecordVo();
        rentEstimateFee.setAttrCode("renterEstimateFee");
        rentEstimateFee.setAttrName("预收费用");
        rentEstimateFee.setFeeValue(getObjOrDefault(entity.getAutoInf05Retainer(), BigDecimal::new, null));
        feeRecordMap.put(rentEstimateFee.getAttrCode(), rentEstimateFee);

        FeeRecordVo actualRentEstimateFee = new FeeRecordVo();
        actualRentEstimateFee.setAttrCode("actualRenterEstimateFee");
        actualRentEstimateFee.setAttrName("实际预收取费用");
        actualRentEstimateFee.setFeeValue(entity.getAutoInf05Realcost());
        feeRecordMap.put(actualRentEstimateFee.getAttrCode(), actualRentEstimateFee);

        FeeRecordVo ownerExtraPrice = new FeeRecordVo();
        ownerExtraPrice.setAttrCode("ownerExtraAllowance");
        ownerExtraPrice.setAttrName("给车主额外补贴");
        ownerExtraPrice.setFeeValue(getObjOrDefault(entity.getAutoInf05Czlimit(), BigDecimal::new, null));
        feeRecordMap.put(ownerExtraPrice.getAttrCode(), ownerExtraPrice);

        FeeRecordVo renterExtraPrice = new FeeRecordVo();
        renterExtraPrice.setAttrCode("renterExtraAllowance");
        renterExtraPrice.setAttrName("给租客额外补贴");
        renterExtraPrice.setFeeValue(getObjOrDefault(entity.getAutoInf05Zklimit(), BigDecimal::new, null));
        feeRecordMap.put(renterExtraPrice.getAttrCode(), renterExtraPrice);

        FeeRecordVo ownerAdjPrice = new FeeRecordVo();
        ownerAdjPrice.setAttrCode("ownerReAdjust");
        ownerAdjPrice.setAttrName("给车主调价");
        ownerAdjPrice.setFeeValue(entity.getAutoInf05Oprice());
        feeRecordMap.put(ownerAdjPrice.getAttrCode(), ownerAdjPrice);

        FeeRecordVo renterAdjPrice = new FeeRecordVo();
        renterAdjPrice.setAttrCode("renterReAjust");
        renterAdjPrice.setAttrName("给租客调价");
        renterAdjPrice.setFeeValue(entity.getAutoInf05Rprice());
        feeRecordMap.put(renterAdjPrice.getAttrCode(), renterAdjPrice);

        FeeRecordVo basePrice = new FeeRecordVo();
        basePrice.setAttrCode("baseattr");
        basePrice.setAttrName("基础费用");
        basePrice.setFeeValue(entity.getAutoInf05C080());
        feeRecordMap.put(basePrice.getAttrCode(), basePrice);

        FeeRecordVo extPrice = new FeeRecordVo();
        extPrice.setAttrCode("extraattr");
        extPrice.setAttrName("额外费用");
        extPrice.setFeeValue(entity.getAutoInf05C081());
        feeRecordMap.put(extPrice.getAttrCode(), extPrice);

        FeeRecordVo trafficAllowPrice = new FeeRecordVo();
        trafficAllowPrice.setAttrCode("trafficAllowance");
        trafficAllowPrice.setAttrName("交通费补贴");
        trafficAllowPrice.setFeeValue(getObjOrDefault(entity.getAutoInf05Jtbtcost(), BigDecimal::new, null));
        feeRecordMap.put(trafficAllowPrice.getAttrCode(), trafficAllowPrice);

        FeeRecordVo fuelPrice = new FeeRecordVo();
        fuelPrice.setExpenseType("expenseFuel");
        Optional.ofNullable(entity.getAutoInf05C075()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity expenseOilDict = sqlServerTackBackService.getDictData(sqlServer_oil_expense_type, x);
            fuelPrice.setFeeTypeVal(getObjOrDefault(expenseOilDict, AutoDictDataEntity::getName, null));
            fuelPrice.setFeeTypeKey(getCodeByLabel(dictMap.get(EXPENSE_FUEL), fuelPrice.getFeeTypeVal()));
        });
        fuelPrice.setAttrCode("fuelattr");
        fuelPrice.setAttrName("加油费");
        fuelPrice.setFeeValue(getObjOrDefault(convertFeeToDouble(entity.getAutoInf05C054()), BigDecimal::new, null));
        feeRecordMap.put(fuelPrice.getAttrCode(), fuelPrice);

        FeeRecordVo parkPrice = new FeeRecordVo();
        parkPrice.setExpenseType("expensePark");
        Optional.ofNullable(entity.getAutoInf05C076()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity expenseOilDict = sqlServerTackBackService.getDictData(sqlServer_park_expense_type, x);
            parkPrice.setFeeTypeVal(getObjOrDefault(expenseOilDict, AutoDictDataEntity::getName, null));
            parkPrice.setFeeTypeKey(getCodeByLabel(dictMap.get(EXPENSE_PARK), parkPrice.getFeeTypeVal()));
        });
        parkPrice.setAttrCode("parkattr");
        parkPrice.setAttrName("停车费");
        parkPrice.setFeeValue(getObjOrDefault(convertFeeToDouble(entity.getAutoInf05C055()), BigDecimal::new, null));
        feeRecordMap.put(parkPrice.getAttrCode(), parkPrice);

        FeeRecordVo trafficPrice = new FeeRecordVo();
        trafficPrice.setExpenseType("expenseTraffic");
        Optional.ofNullable(entity.getAutoInf05C077()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity expenseOilDict = sqlServerTackBackService.getDictData(sqlServer_traffic_expense_type, x);
            trafficPrice.setFeeTypeVal(getObjOrDefault(expenseOilDict, AutoDictDataEntity::getName, null));
            trafficPrice.setFeeTypeKey(getCodeByLabel(dictMap.get(EXPENSE_TRAFFIC), trafficPrice.getFeeTypeVal()));
        });
        trafficPrice.setAttrCode("trafficattr");
        trafficPrice.setAttrName("交通费");
        trafficPrice.setFeeValue(getObjOrDefault(convertFeeToDouble(entity.getAutoInf05C056()), BigDecimal::new, null));
        feeRecordMap.put(trafficPrice.getAttrCode(), trafficPrice);

        FeeRecordVo roadPrice = new FeeRecordVo();
        roadPrice.setAttrCode("roadBridgeattr");
        roadPrice.setAttrName("路桥费");
        roadPrice.setFeeValue(getObjOrDefault(entity.getAutoInf05C058(), BigDecimal::new, null));
        feeRecordMap.put(roadPrice.getAttrCode(), roadPrice);

        FeeRecordVo washPrice = new FeeRecordVo();
        washPrice.setAttrCode("washVehicleattr");
        washPrice.setAttrName("洗车费");
        washPrice.setFeeValue(getObjOrDefault(convertFeeToDouble(entity.getAutoInf05C057()), BigDecimal::new, null));
        feeRecordMap.put(washPrice.getAttrCode(), washPrice);

        FeeRecordVo finePrice = new FeeRecordVo();
        finePrice.setAttrCode("fineattr");
        finePrice.setAttrName("罚款");
        finePrice.setFeeValue(entity.getAutoInf05C083());
        feeRecordMap.put(finePrice.getAttrCode(), finePrice);

        FeeRecordVo comPrice = new FeeRecordVo();
        comPrice.setAttrCode("compensateattr");
        comPrice.setAttrName("补偿费用");
        comPrice.setFeeValue(entity.getAutoInf05Compensationfees());
        feeRecordMap.put(comPrice.getAttrCode(), comPrice);

        FeeRecordVo comMoneyPrice = new FeeRecordVo();
        comMoneyPrice.setAttrCode("compensateMoney");
        comMoneyPrice.setAttrName("赔偿金额");
        comMoneyPrice.setFeeValue(entity.getAutoInf05Peichangjine());
        feeRecordMap.put(comMoneyPrice.getAttrCode(), comMoneyPrice);

        vo.setFeeRecordMap(feeRecordMap);

        return vo;
    }

    public static TransUserInfoRespVo convertTransUser(AotuInfor005Entity entity) {
        TransUserInfoRespVo vo = new TransUserInfoRespVo();
        vo.setOrderNo(entity.getAutoInf05C050());
        vo.setOwnerNo(entity.getAutoInf05Ownerno());
        vo.setOwnerName(entity.getAutoInf05C013());
        vo.setOwnerPhone(entity.getAutoInf05C014());
        vo.setOwnerLevel(entity.getAutoInf05Ownerlevel());
        vo.setOwnerLabel(entity.getAutoInf05Ownerlables());

        vo.setRenterNo(entity.getAutoInf05Renterno());
        vo.setRenterName(entity.getAutoInf05C015());
        vo.setRenterPhone(entity.getAutoInf05C016());
        vo.setRenterLevel(entity.getAutoInf05Tenantlevel());
        vo.setRenterLabel(entity.getAutoInf05Tenantlables());

        vo.setMemFlag(entity.getAutoInf05Rentermemberflag());
        vo.setRenterUseTakeBackTime(getObjOrDefault(entity.getAutoInf05Zksyqhccs(), String::valueOf, null));

        vo.setRepairShopContact(entity.getAutoInf05C091());
        vo.setRepairShopPhone(entity.getAutoInf05C092());
        vo.setStoreName(entity.getAutoInf05Sname());
        vo.setStorePhone(entity.getAutoInf05Sphone());
        return vo;
    }

    public static TransVehicleInfoRespVo convertTransVehicle(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService,
                                                             Map<String, List<SysDictEntity>> dictMap) {
        TransVehicleInfoRespVo vo = new TransVehicleInfoRespVo();
        vo.setOrderNo(entity.getAutoInf05C050());
        Optional.ofNullable(entity.getAutoInf05C071()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            vo.setVehicleTypeVal(x);
            vo.setVehicleTypeKey(getCodeByLabel(dictMap.get(VEHICLE_TYPE), x));
        });
        vo.setEscrowAdmin(entity.getAutoInf05Delegaadmin());
        vo.setEscrowAdminPhone(entity.getAutoInf05Delegaadminphone());
        vo.setVehicleModel(entity.getAutoInf05C007());
        vo.setVehicleNo(entity.getAutoInf05C006());
        vo.setTankCapacity(entity.getAutoInf05Tankcapacity());
        vo.setDisplacement(entity.getAutoInf05Displacement());
        vo.setOilScaleDenominator(getObjOrDefault(entity.getAutoInf05Oilscaledenominator(), String::valueOf, null));
        vo.setEngineTypeKey(getObjOrDefault(entity.getAutoInf05Oils(), Integer::parseInt, null));
        vo.setEngineTypeVal(getLabelByCode(dictMap.get(ENGINE_TYPE), Optional.ofNullable(vo.getEngineTypeKey()).map(x -> String.valueOf(--x)).orElse(null)));
        vo.setOilPrice(getObjOrDefault(convertFeeToDouble(entity.getAutoInf05Oilprice()), BigDecimal::new, null));
        vo.setOilUnitPrice(getObjOrDefault(entity.getAutoInf05Youhaoprice(), BigDecimal::toPlainString, null));
        Optional.ofNullable(entity.getAutoInf05Daymileage()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity dayMileageDict = sqlServerTackBackService.getDictData(sqlServer_day_mileage_type, x);
            vo.setDayMileage(getObjOrDefault(dayMileageDict, AutoDictDataEntity::getName, null));
        });
        vo.setGuideDayPrice(getObjOrDefault(entity.getAutoInf05Guidedayprice(), BigDecimal::new, null));
        vo.setDetectStatusKey(entity.getAutoInf05Detectstatus());
        vo.setDetectStatusVal(getLabelByCode(dictMap.get(DETECT_STATUS), String.valueOf(vo.getDetectStatusKey())));
        vo.setTakeNote(entity.getAutoInf05Takenote());
        return vo;
    }

    public static TransOrderInfoRespVo convertTransOrder(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService,
                                                         Map<String, List<SysDictEntity>> dictMap) throws Exception {
        TransOrderInfoRespVo vo = new TransOrderInfoRespVo();
        vo.setOrderNo(entity.getAutoInf05C050());
        vo.setRenewOrderNo(entity.getAutoInf05Revedno());
        AutoDictDataEntity cityDict = sqlServerTackBackService.getDictData(sqlServer_city_code, entity.getAutoInf05C008());
        vo.setBelongCity(getObjOrDefault(cityDict, AutoDictDataEntity::getName, null));
        Optional.ofNullable(entity.getAutoInf05Source()).ifPresent(x -> {
            vo.setSourceKey(getCodeByLabel1(dictMap.get(TRANS_SOURCE), x));
            vo.setSourceVal(getLabelByCode(dictMap.get(TRANS_SOURCE), String.valueOf(vo.getSourceKey())));
        });
        Optional.ofNullable(entity.getAutoInf05C072()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            vo.setSceneSourceVal(x);
            vo.setSceneSourceKey(getCodeByLabel(dictMap.get(TRANS_SCENE_SOURCE), x));
        });
        vo.setOfflineOrderTypeKey(getCodeByLabel1(dictMap.get(OFFLINE_ORDER_TYPE), entity.getAutoInf05Offlineordertype()));
        vo.setOfflineOrderTypeVal(getLabelByCode(dictMap.get(OFFLINE_ORDER_TYPE), String.valueOf(vo.getOfflineOrderTypeKey())));
        vo.setRentStartTime(entity.getAutoInf05Qtime());
        vo.setRentEntTime(entity.getAutoInf05Htime());
        vo.setRentAmt(entity.getAutoInf05Rentamt());
        vo.setPricePerDay(entity.getAutoInf05Hdaverage());

        String patten = Optional.ofNullable(entity.getAutoInf05Depositpaytime()).filter(StringUtils::isNotEmpty)
                .map(x -> Optional.of(x.length()).filter(y -> y < 19).map(y -> DATE_NO_SECOND).orElse(DATE_FULL))
                .orElse(null);
        if (Objects.nonNull(patten)) {
            String payTime = entity.getAutoInf05Depositpaytime();
            if (StringUtils.containsIgnoreCase(payTime, M)) {
                if (StringUtils.contains(payTime, ONE_NINE_ZERO)){
                    LocalDateTime localDateTime = convertToLocalDateTime(entity.getPubCrttime());
                    payTime = StringUtils.replace(payTime, ONE_NINE_ZERO, String.valueOf(localDateTime.getYear()));
                }
                payTime = format(payTime, patten);
            }
            vo.setDepositPayTime(LocalDateUtil.parse(payTime, patten));
        }

        Optional.ofNullable(entity.getAutoInf05Ssarisks()).ifPresent(x -> {
            AutoDictDataEntity riskDict = sqlServerTackBackService.getDictData(sqlServer_supper_supple_risk_type, x);
            vo.setSuperSuppleRisk(Optional.ofNullable(riskDict).map(AutoDictDataEntity::getName).orElse(x));
        });
        vo.setPartner(entity.getAutoInf05Partner());
        Optional.ofNullable(entity.getAutoInf05Riskcontrolauditstate()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity risControlDict = sqlServerTackBackService.getDictData(sqlServer_risk_control_type, x);
            vo.setRiskControlAuditState(Optional.ofNullable(risControlDict).map(AutoDictDataEntity::getName).orElse(x));
        });
        vo.setCustNote(entity.getAutoInf05C020());
        vo.setEditStatus(entity.getPubEditflag());
        return vo;
    }

    public static PickBackSelectRespVo convertSelect(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService,
                                                     Map<String, List<SysDictEntity>> dictMap) {
        PickBackSelectRespVo pickBackSelectRespVo = new PickBackSelectRespVo();
        PickOrderInfoVo pickOrderInfoVo = new PickOrderInfoVo();
        pickOrderInfoVo.setOrderNo(entity.getAutoInf05C050());
        AutoDictDataEntity orderTypeDict = sqlServerTackBackService.getDictData(sqlServer_order_type, entity.getAutoInf05Ordertype());
        pickOrderInfoVo.setOrderType(getObjOrDefault(orderTypeDict, AutoDictDataEntity::getName, null));
        pickOrderInfoVo.setPickDeliverOrderNo(String.valueOf(entity.getAutoInf05C073()));
        AutoDictDataEntity serverTypeDict = sqlServerTackBackService.getDictData(sqlServer_serverType_code, entity.getAutoInf05C002());
        Optional.ofNullable(serverTypeDict).ifPresent(dict -> {
            pickOrderInfoVo.setServerTypeKey(getCodeByLabel(dictMap.get(FLOW_SERVER_TYPE), serverTypeDict.getName()));
            pickOrderInfoVo.setServerTypeVal(dict.getName());
        });
        pickOrderInfoVo.setIsSupplyKey(NEG_ZERO);
        pickOrderInfoVo.setIsSupplyVal("否");
        if (StringUtils.equalsIgnoreCase(entity.getAutoInf05C002(), sqlServer_is_supply)) {
            pickOrderInfoVo.setIsSupplyKey(INT_ONE);
            pickOrderInfoVo.setIsSupplyVal("是");
        }
        AutoDictDataEntity oprTypeDict = sqlServerTackBackService.getDictData(sqlServer_oprType_code, entity.getAutoInf05C003());
        Optional.ofNullable(oprTypeDict).ifPresent(dict -> {
            pickOrderInfoVo.setOprTypeKey(getCodeByLabel(dictMap.get(PICK_BACK_OPR_TYPE), oprTypeDict.getName()));
            pickOrderInfoVo.setOprTypeVal(dict.getName());
        });

        int scheduleStatusKey = setScheduleStatus(entity, sqlServerTackBackService);
        String scheduleStatusVal = getLabelByCode(dictMap.get(PICK_DELIVER_SCHEDULE_STATUS), String.valueOf(scheduleStatusKey));
        pickOrderInfoVo.setScheduleStatusKey(scheduleStatusKey);
        pickOrderInfoVo.setScheduleStatusVal(scheduleStatusVal);

        if (Objects.nonNull(entity.getAutoInf05C059())) {
            // AutoDictDataEntity fieldAppDict = sqlServerTackBackService.getDictData(sqlServer_field_app_type, String.valueOf(entity.getAutoInf05C059()));
            String fieldApp = sqlServerTackBackService.selectCurByCond(entity.getAutoInf05Id());
            pickOrderInfoVo.setFieldAppStatusVal(getLabelByLabel1(dictMap.get(FLOW_NODE_NAME_TYPE), fieldApp));
        }

        pickOrderInfoVo.setIsDirectCompensateVal(getLabelByCode(dictMap.get(PUBLISH_TYPE), entity.getAutoInf05C093()));
        pickOrderInfoVo.setChannelVal(getLabelByCode(dictMap.get(CHANNEL_TYPE), entity.getAutoInf05Channeltype()));
        pickOrderInfoVo.setServiceClassVal(getLabelByCode(dictMap.get(SERVICE_CLASS), entity.getAutoInf05Servertype()));
        pickOrderInfoVo.setIsFreeVal(getLabelByCode(dictMap.get(PUBLISH_TYPE), entity.getAutoInf05Isfree()));
        pickOrderInfoVo.setServiceFee(getObjOrDefault(entity.getAutoInf05Freemoney(), BigDecimal::new, null));
        pickOrderInfoVo.setReportNo(entity.getAutoInf05Baoanno());
        pickOrderInfoVo.setGoldKey(entity.getAutoInf05Keyperson());
        pickOrderInfoVo.setOrderChannelSourceVal(entity.getAutoInf05Dqly());
        pickOrderInfoVo.setEstimateServiceTime(entity.getAutoInf05Extime());

        AutoDictDataEntity cityDict = sqlServerTackBackService.getDictData(sqlServer_city_code, entity.getAutoInf05C008());
        pickOrderInfoVo.setBelongCity(getObjOrDefault(cityDict, AutoDictDataEntity::getName, null));

        pickOrderInfoVo.setPickTime(entity.getAutoInf05C004());
        pickOrderInfoVo.setDeliverTime(entity.getAutoInf05Qtime());

        pickOrderInfoVo.setPickAddr(entity.getAutoInf05C010());
        pickOrderInfoVo.setPickLong(getObjOrDefault(entity.getAutoInf05Taddrlon(), BigDecimal::new, null));
        pickOrderInfoVo.setPickLat(getObjOrDefault(entity.getAutoInf05Taddrlat(), BigDecimal::new, null));

        pickOrderInfoVo.setDeliverAddr(entity.getAutoInf05C012());
        pickOrderInfoVo.setDeliverLong(getObjOrDefault(entity.getAutoInf05Saddrlon(), BigDecimal::new, null));
        pickOrderInfoVo.setDeliverLat(getObjOrDefault(entity.getAutoInf05Saddrlat(), BigDecimal::new, null));
        if (objEquals(pickOrderInfoVo.getServerTypeKey(), INT_FOUR, Integer::equals)) {
            pickOrderInfoVo.setPickTime(entity.getAutoInf05Htime());
            pickOrderInfoVo.setDeliverTime(entity.getAutoInf05Aftertime());
        }

        pickOrderInfoVo.setCreateTime(entity.getPubCrttime());
        SyspUserEntity syspUserEntity = sqlServerTackBackService.selectUserById(entity.getPubCrtusid());
        pickOrderInfoVo.setCreateOp(getObjOrDefault(syspUserEntity, SyspUserEntity::getSyspUseLoginname, "system"));
        pickOrderInfoVo.setUpdateTime(entity.getPubFupdtime());
        SyspUserEntity syspUser = sqlServerTackBackService.selectUserById(entity.getPubFupdusid());
        pickOrderInfoVo.setUpdateOp(getObjOrDefault(syspUser, SyspUserEntity::getSyspUseLoginname, null));
        pickBackSelectRespVo.setPickOrderInfo(pickOrderInfoVo);
        // ======================调度信息=================================
        PickBackScheduleInfoVo scheduleInfoVo = new PickBackScheduleInfoVo();
        scheduleInfoVo.setPickDeliverOrderNo(pickOrderInfoVo.getPickDeliverOrderNo());

        scheduleInfoVo.setIsSupplierDistributeKey(0);
        if (CollectionUtils.isNotEmpty(sqlServerTackBackService.queryFlow(entity.getAutoInf05Id(), null, null, null, null, sqlServer_new, null))) {
            scheduleInfoVo.setIsSupplierDistributeKey(1);
            Optional.ofNullable(entity.getAutoInf05Sercompany()).filter(StringUtils::isNotEmpty).ifPresent(supplier -> {
                AutoDictDataEntity companyDict = sqlServerTackBackService.getDictData(sqlServer_company, supplier);
                scheduleInfoVo.setSupplierCompanyId(getObjOrDefault(getObjOrDefault(companyDict, AutoDictDataEntity::getId, null), Long::intValue, null));
                scheduleInfoVo.setSupplierCompanyName(getObjOrDefault(companyDict, AutoDictDataEntity::getName, null));
            });

        }
        scheduleInfoVo.setIsSupplierDistributeVal(getLabelByCode(dictMap.get(PUBLISH_TYPE), String.valueOf(scheduleInfoVo.getIsSupplierDistributeKey())));

        if (StringUtils.isNotEmpty(entity.getAutoInf05C022())) {
            AutoOther001Entity other001Entity = sqlServerTackBackService.getTackBackPerson(Long.parseLong(entity.getAutoInf05C022()));
            Optional.ofNullable(other001Entity).ifPresent(x -> {
                AutoDictDataEntity companyDict = sqlServerTackBackService.getDictData(sqlServer_company, x.getAutoOt01Sercompany());
                scheduleInfoVo.setSupplierCompanyId(getObjOrDefault(getObjOrDefault(companyDict, AutoDictDataEntity::getId, null), Long::intValue, null));
                scheduleInfoVo.setSupplierCompanyName(getObjOrDefault(companyDict, AutoDictDataEntity::getName, null));
                scheduleInfoVo.setUserId(x.getAutoOt01Id().intValue());
                scheduleInfoVo.setUserName(x.getAutoOt01C002());
                scheduleInfoVo.setUserPhone(x.getAutoOt01C003());
            });
        }
        AutoDictDataEntity ownUserDict = sqlServerTackBackService.getDictData(sqlServer_owen_type, entity.getAutoInf05Isqsctype());
        scheduleInfoVo.setIsOwnUserKey(getObjOrDefault(ownUserDict, x -> getCodeByLabel(dictMap.get(IS_OWN_USER), x.getName()), null));
        scheduleInfoVo.setIsOwnUserVal(getObjOrDefault(ownUserDict, AutoDictDataEntity::getName, null));
        Optional.ofNullable(entity.getAutoInf05Sendtype()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity dispatchDict = sqlServerTackBackService.getDictData(sqlServer_dispatch_type, x);
            scheduleInfoVo.setDispatchTypeVal(getObjOrDefault(dispatchDict, AutoDictDataEntity::getName, null));
            scheduleInfoVo.setDispatchTypeKey(getCodeByLabel(dictMap.get(DISPATCH_TYPE), scheduleInfoVo.getDispatchTypeVal()));
        });

        scheduleInfoVo.setFlightNo(entity.getAutoInf05Flightno());
        scheduleInfoVo.setScheduleMemo(entity.getAutoInf05C025());
        scheduleInfoVo.setCreateOp(pickOrderInfoVo.getCreateOp());
        scheduleInfoVo.setCreateTime(pickOrderInfoVo.getCreateTime());
        scheduleInfoVo.setUpdateOp(pickOrderInfoVo.getUpdateOp());
        scheduleInfoVo.setUpdateTime(pickOrderInfoVo.getUpdateTime());
        scheduleInfoVo.setEditStatus(getEditStatus(scheduleInfoVo.getSupplierCompanyId(), entity.getAutoInf05Id(), sqlServerTackBackService));

        pickBackSelectRespVo.setScheduleInfo(scheduleInfoVo);
        // ======================取车信息=================================
        TackPickInfoVo pickInfoVo = new TackPickInfoVo();
        pickInfoVo.setPickDeliverOrderNo(pickOrderInfoVo.getPickDeliverOrderNo());
        pickInfoVo.setEditStatus(entity.getPubEditflag());
        pickInfoVo.setRealPickTime(entity.getAutoInf05C026());
        pickInfoVo.setPickOil(getObjOrDefault(entity.getAutoInf05Qoilm(), BigDecimal::new, null));
        pickInfoVo.setEstimatePickKilo(getObjOrDefault(entity.getAutoInf05Getkilometre(), BigDecimal::new, null));
        pickInfoVo.setPickKilo(getObjOrDefault(entity.getAutoInf05Qmileage(), Integer::parseInt, null));

        AutoDictDataEntity vehicleDict = sqlServerTackBackService.getDictData(sqlServer_vehicle_status_type, entity.getAutoInf05Vstate());
        Optional.ofNullable(vehicleDict).ifPresent(vehicle -> {
            int vehicleStatusKey = getCodeByLabel(dictMap.get(VEHICLE_STATUS), vehicle.getName());
            pickInfoVo.setVehicleStatusKey(vehicleStatusKey);
            pickInfoVo.setVehicleStatusVal(vehicle.getName());
        });
        pickInfoVo.setChangePickAddress(entity.getAutoInf05Newc010());
        pickInfoVo.setChangePickLong(getObjOrDefault(entity.getAutoInf05Newc010lon(), BigDecimal::new, null));
        pickInfoVo.setChangePickLat(getObjOrDefault(entity.getAutoInf05Newc010lat(), BigDecimal::new, null));
        pickBackSelectRespVo.setPickInfo(pickInfoVo);
        // ======================送车信息=================================
        TackDeliverInfoVo deliverInfoVo = new TackDeliverInfoVo();
        deliverInfoVo.setPickDeliverOrderNo(pickOrderInfoVo.getPickDeliverOrderNo());
        deliverInfoVo.setEditStatus(entity.getPubEditflag());
        deliverInfoVo.setRealDeliverTime(entity.getAutoInf05C027());

        BigDecimal deliverOil = Optional.ofNullable(entity.getAutoInf05C053()).filter(StringUtils::isNumeric).map(BigDecimal::new)
                .orElseGet(() -> {
                    Double score = convertOil(entity.getAutoInf05C053());
                    return Optional.ofNullable(score).map(s -> s * entity.getAutoInf05Oilscaledenominator())
                            .map(s -> new BigDecimal(s).setScale(NEG_ZERO, BigDecimal.ROUND_HALF_UP))
                            .orElse(null);
                });
        deliverInfoVo.setDeliverOil(getObjOrDefault(deliverOil, o -> {
            if (o.compareTo(BigDecimal.valueOf(INT_SIXTEEN)) > 0){
                return null;
            }
            return o;
        }, null));

        String deliverKilo = Optional.ofNullable(entity.getAutoInf05C052()).filter(StringUtils::isNumeric)
                .orElse(convertKilo(entity.getAutoInf05C052()));
        deliverInfoVo.setDeliverKilo(getObjOrDefault(deliverKilo, d -> {
            if (!StringUtils.contains(d, ".") && StringUtils.length(d) > INT_SIX){
                return null;
            }
            if (StringUtils.contains(d, ".") && StringUtils.length(d) > INT_NINE){
                return null;
            }
            return new BigDecimal(d);
        }, null));

        deliverInfoVo.setChangeDeliverAddress(entity.getAutoInf05Newc012());
        deliverInfoVo.setChangeDeliverLong(getObjOrDefault(entity.getAutoInf05Newc012lon(), BigDecimal::new, null));
        deliverInfoVo.setChangeDeliverLat(getObjOrDefault(entity.getAutoInf05Newc012lat(), BigDecimal::new, null));
        deliverInfoVo.setDelayDeliverTime(entity.getAutoInf05Newc005());
        AutoDictDataEntity delayDict = sqlServerTackBackService.getDictData(sqlServer_deliver_delay_reason_type, entity.getAutoInf05Sdlatenote());
        Optional.ofNullable(delayDict).ifPresent(delay -> {
            int delayStatusKey = getCodeByLabel(dictMap.get(DELAY_REASON), delay.getName());
            deliverInfoVo.setDelayReasonKey(delayStatusKey);
            deliverInfoVo.setDelayReasonVal(delay.getName());
        });
        pickBackSelectRespVo.setDeliverInfo(deliverInfoVo);
        return pickBackSelectRespVo;
    }

    public static int getEditStatus(Integer companyId, Long id, SqlServerTackBackService sqlServerTackBackService) {
        List<AutoFlow005Entity> flow005EntityList = sqlServerTackBackService.queryFlow(id, null, null, null, null, null, null);
        if (CollectionUtils.isEmpty(flow005EntityList)) {
            return Optional.ofNullable(companyId).map(x -> NEG_ZERO).orElse(INT_ONE);
        }
        AutoFlow005Entity flow005Entity = flow005EntityList.get(0);
        if (hitSearch(sqlServer_new, flow005Entity.getAutoFw05C002(), String::contains)) {
            return NEG_ZERO;
        }
        return INT_ONE;
    }

    public static TackBackPageVo convert(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService,
                                         Map<String, List<SysDictEntity>> dictMap) {
        TackBackPageVo vo = new TackBackPageVo();
        vo.setId(entity.getAutoInf05Id());
        vo.setPickDeliverOrderNo(String.valueOf(entity.getAutoInf05C073()));
        vo.setTransOrderNo(entity.getAutoInf05C050());

        AutoDictDataEntity serverTypeDict = sqlServerTackBackService.getDictData(sqlServer_serverType_code, entity.getAutoInf05C002());
        Optional.ofNullable(serverTypeDict).ifPresent(dict -> {
            vo.setServerTypeKey(getCodeByLabel(dictMap.get(FLOW_SERVER_TYPE), dict.getName()));
            vo.setServerTypeVal(dict.getName());
        });

        vo.setIsSupplyKey(NEG_ZERO);
        vo.setIsSupplyVal("否");
        if (StringUtils.equalsIgnoreCase(entity.getAutoInf05C002(), sqlServer_is_supply)) {
            vo.setIsSupplyKey(INT_ONE);
            vo.setIsSupplyVal("是");
        }
        AutoDictDataEntity cityDict = sqlServerTackBackService.getDictData(sqlServer_city_code, entity.getAutoInf05C008());
        vo.setBelongCity(getObjOrDefault(cityDict, AutoDictDataEntity::getName, null));

        vo.setPickTime(entity.getAutoInf05C004());
        vo.setVehicleTypeKey(getCodeByLabel(dictMap.get(VEHICLE_TYPE), entity.getAutoInf05C071()));
        vo.setVehicleTypeVal(entity.getAutoInf05C071());
        vo.setOfflineOrderTypeKey(getCodeByLabel1(dictMap.get(OFFLINE_ORDER_TYPE), entity.getAutoInf05Offlineordertype()));
        vo.setOfflineOrderTypeVal(getLabelByCode(dictMap.get(OFFLINE_ORDER_TYPE), String.valueOf(vo.getOfflineOrderTypeKey())));
        vo.setPartner(entity.getAutoInf05Partner());
        vo.setVehicleNo(entity.getAutoInf05C006());
        vo.setVehicleModel(entity.getAutoInf05C007());
        vo.setPickAddr(entity.getAutoInf05C010());
        vo.setDeliverAddr(entity.getAutoInf05C012());
        vo.setOwnerName(entity.getAutoInf05C013());
        vo.setRenterName(entity.getAutoInf05Rentermemberflag());

        if (StringUtils.isNotEmpty(entity.getAutoInf05C022())) {
            AutoOther001Entity other001Entity = sqlServerTackBackService.getTackBackPerson(Long.parseLong(entity.getAutoInf05C022()));
            Optional.ofNullable(other001Entity).ifPresent(x -> {
                AutoDictDataEntity companyDict = sqlServerTackBackService.getDictData(sqlServer_company, x.getAutoOt01Sercompany());
                vo.setServerCompany(getObjOrDefault(companyDict, AutoDictDataEntity::getName, null));
                vo.setTackBackUserName(x.getAutoOt01C002());
            });
        }

        AutoDictDataEntity ownUserDict = sqlServerTackBackService.getDictData(sqlServer_owen_type, entity.getAutoInf05Isqsctype());
        vo.setIsOwnUserKey(getObjOrDefault(ownUserDict, x -> getCodeByLabel(dictMap.get(IS_OWN_USER), x.getName()), null));
        vo.setIsOwnUserVal(getObjOrDefault(ownUserDict, AutoDictDataEntity::getName, null));

        int scheduleStatusKey = setScheduleStatus(entity, sqlServerTackBackService);
        String scheduleStatusVal = getLabelByCode(dictMap.get(PICK_DELIVER_SCHEDULE_STATUS), String.valueOf(scheduleStatusKey));
        vo.setScheduleStatusKey(scheduleStatusKey);
        vo.setScheduleStatusVal(scheduleStatusVal);
        return vo;
    }

    public static int setScheduleStatus(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService) {
        int scheduleStatusKey = 0;

        List<AutoFlow005Entity> flow005Cancel = sqlServerTackBackService.queryFlow(entity.getAutoInf05Id(), null, null, null, null, null, sqlServer_cancel);
        List<AutoFlow005Entity> flow005Entities = sqlServerTackBackService.queryFlow(entity.getAutoInf05Id(), null, sqlServer_new, null, null, null, null);
        if (CollectionUtils.isNotEmpty(flow005Entities)) {
            AutoDictDataEntity oprTypeDict = sqlServerTackBackService.getDictData(sqlServer_oprType_code, entity.getAutoInf05C003());
            if (Objects.nonNull(oprTypeDict)) {
                scheduleStatusKey = oprType(oprTypeDict.getName(), entity.getAutoInf05C085(), flow005Cancel, INT_TOW, INT_ONE, INT_SEVENTEEN);
            }
        }

        if ("5".equals(entity.getAutoInf05C065())) {
            scheduleStatusKey = INT_FIVE;
        }
        if ("1".equals(entity.getAutoInf05C065())) {
            scheduleStatusKey = INT_EIGHT;
        }

        AutoCompinfoEntity compinfoEntity = Optional.ofNullable(entity.getAutoInf05Sercompany())
                .map(sqlServerTackBackService::queryCompanyBySecondName).orElse(null);
        boolean isSupplier = Objects.nonNull(compinfoEntity) && (objEquals(compinfoEntity.getAutoCpinfoComp1(), "p_cl1_003", String::equals) ||
                objEquals(compinfoEntity.getAutoCpinfoComp1(), "p_cl1_004", String::equals));

        if ("2".equals(entity.getAutoInf05C065())) {
            scheduleStatusKey = Optional.of(isSupplier).filter(x -> x).map(x -> 10).orElse(9);
        }
        if ("3".equals(entity.getAutoInf05C065())) {
            scheduleStatusKey = Optional.of(isSupplier).filter(x -> x).map(x -> 12).orElse(11);
        }
        List<AutoFlow005Entity> flow005Tack = sqlServerTackBackService.queryFlow(entity.getAutoInf05Id(), null, null, sqlServer_tack_deliver, null, null, null);
        if (CollectionUtils.isNotEmpty(flow005Tack)) {
            scheduleStatusKey = Optional.of(isSupplier).filter(x -> x).map(x -> INT_FOURTEEN).orElse(13);
        }

        List<AutoFlow005Entity> flow005Deliver = sqlServerTackBackService.queryFlow(entity.getAutoInf05Id(), null, null, null, sqlServer_tack_deliver, null, null);
        if (CollectionUtils.isNotEmpty(flow005Deliver)) {
            scheduleStatusKey = Optional.of(isSupplier).filter(x -> x).map(x -> 16).orElse(15);
        }

        List<AutoFlow005Entity> flow005Supplier = sqlServerTackBackService.queryFlow(entity.getAutoInf05Id(), null, null, null, null, sqlServer_new, null);
        if (CollectionUtils.isNotEmpty(flow005Supplier)) {
            AutoDictDataEntity oprTypeDict = sqlServerTackBackService.getDictData(sqlServer_oprType_code, entity.getAutoInf05C003());
            if (Objects.nonNull(oprTypeDict)) {
                scheduleStatusKey = oprType(oprTypeDict.getName(), entity.getAutoInf05C085(), flow005Cancel, INT_FOUR, INT_THREE, INT_SEVENTEEN);
            }
        }

        List<AutoFlow005Entity> flow005Wait = sqlServerTackBackService.queryFlow(entity.getAutoInf05Id(), null, null, sqlServer_new, null, null, null);
        if (CollectionUtils.isNotEmpty(flow005Wait) && "0".equals(entity.getAutoInf05C065())) {
            scheduleStatusKey = Optional.ofNullable(entity.getAutoInf05Sercompany()).map(x -> INT_SEVEN).orElse(INT_SIX);
        }
        if (CollectionUtils.isNotEmpty(flow005Cancel)) {
            AutoDictDataEntity oprTypeDict = sqlServerTackBackService.getDictData(sqlServer_oprType_code, entity.getAutoInf05C003());
            if (Objects.nonNull(oprTypeDict)) {
                scheduleStatusKey = oprType(oprTypeDict.getName(), entity.getAutoInf05C085(), flow005Cancel, INT_FOUR, INT_THREE, INT_SEVENTEEN);
            }
        }
        return scheduleStatusKey;
    }

    private static int oprType(String str, String reasonKey, List<AutoFlow005Entity> flow005Cancel, int... statusArray) {
        int status;
        switch (str) {
            case "变更":
                status = statusArray[0];
                break;
            case "新增":
                status = statusArray[1];
                break;
            case "取消":
                if (CollectionUtils.isEmpty(flow005Cancel)) {
                    status = statusArray[2];
                } else {
                    status = Optional.ofNullable(reasonKey).filter("7"::equals).map(x -> INT_EIGHTEEN).orElse(statusArray[2]);
                }
                break;
            default:
                status = -1;
        }
        return status;
    }

}
