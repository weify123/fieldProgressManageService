package com.autoyol.field.progress.manage.convertsqlserver;

import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.entity.sqlserver.AotuInfor005Entity;
import com.autoyol.field.progress.manage.entity.sqlserver.AutoDictDataEntity;
import com.autoyol.field.progress.manage.entity.sqlserver.AutoOther001Entity;
import com.autoyol.field.progress.manage.entity.sqlserver.SyspUserEntity;
import com.autoyol.field.progress.manage.mapper.EmployCompanyMapper;
import com.autoyol.field.progress.manage.service.sqlserver.SqlServerTackBackService;

import com.autoyol.field.progress.manage.util.LocalDateUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.convertToLocalDateTime;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.format;
import static com.autoyol.field.progress.manage.util.OprUtil.*;
import static com.autoyol.field.progress.manage.util.ChConvertScoresUtil.*;

public class TackBackConvertEntity {

    public static TransOrderInfoEntity convertTransOrder(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService,
                                                         Map<String, List<SysDictEntity>> dictMap) throws Exception {
        TransOrderInfoEntity vo = new TransOrderInfoEntity();
        vo.setOrderNo(entity.getAutoInf05C050());
        vo.setRenewOrderNo(entity.getAutoInf05Revedno());
        AutoDictDataEntity cityDict = sqlServerTackBackService.getDictData(sqlServer_city_code, entity.getAutoInf05C008());
        vo.setBelongCity(getObjOrDefault(cityDict, AutoDictDataEntity::getName, null));
        Optional.ofNullable(entity.getAutoInf05Source()).ifPresent(x -> {
            vo.setSource(getCodeByLabel1(dictMap.get(TRANS_SOURCE), x));
        });
        Optional.ofNullable(entity.getAutoInf05C072()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            vo.setSceneSource(getCodeByLabel(dictMap.get(TRANS_SCENE_SOURCE), x));
        });
        vo.setOfflineOrderType(getCodeByLabel1(dictMap.get(OFFLINE_ORDER_TYPE), entity.getAutoInf05Offlineordertype()));
        vo.setRentStartTime(entity.getAutoInf05Qtime());
        vo.setRentEntTime(entity.getAutoInf05Htime());
        vo.setRentAmt(entity.getAutoInf05Rentamt());
        vo.setPricePerDay(entity.getAutoInf05Hdaverage());

        String patten = Optional.ofNullable(entity.getAutoInf05Depositpaytime()).filter(StringUtils::isNotEmpty)
                .map(x -> Optional.of(x.length()).filter(y -> y < 19).map(y -> DATE_NO_SECOND).orElse(DATE_FULL))
                .orElse(null);
        if (Objects.nonNull(patten) && StringUtils.isNotEmpty(entity.getAutoInf05Depositpaytime())) {
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

    public static TransVehicleInfoEntity convertTransVehicle(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService,
                                                             Map<String, List<SysDictEntity>> dictMap) {
        TransVehicleInfoEntity vo = new TransVehicleInfoEntity();
        vo.setOrderNo(entity.getAutoInf05C050());
        Optional.ofNullable(entity.getAutoInf05C071()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            vo.setVehicleType(getCodeByLabel(dictMap.get(VEHICLE_TYPE), x));
        });
        vo.setEscrowAdmin(entity.getAutoInf05Delegaadmin());
        vo.setEscrowAdminPhone(entity.getAutoInf05Delegaadminphone());
        vo.setVehicleModel(entity.getAutoInf05C007());
        vo.setVehicleNo(entity.getAutoInf05C006());
        vo.setTankCapacity(entity.getAutoInf05Tankcapacity());
        vo.setOilCapacity(entity.getAutoInf05Jyoil());
        vo.setDisplacement(entity.getAutoInf05Displacement());
        vo.setOilScaleDenominator(getObjOrDefault(entity.getAutoInf05Oilscaledenominator(), String::valueOf, null));
        vo.setEngineType(getObjOrDefault(entity.getAutoInf05Oils(), Integer::parseInt, null));
        vo.setOilPrice(getObjOrDefault(convertFeeToDouble(entity.getAutoInf05Oilprice()), BigDecimal::new, null));
        vo.setOilUnitPrice(getObjOrDefault(entity.getAutoInf05Youhaoprice(), BigDecimal::toPlainString, null));
        Optional.ofNullable(entity.getAutoInf05Daymileage()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity dayMileageDict = sqlServerTackBackService.getDictData(sqlServer_day_mileage_type, x);
            vo.setDayMileage(getObjOrDefault(dayMileageDict, AutoDictDataEntity::getName, null));
        });

        vo.setGuideDayPrice(getObjOrDefault(convertGuidePrice(entity.getAutoInf05Guidedayprice()), Function.identity(), null));
        vo.setDetectStatus(entity.getAutoInf05Detectstatus());
        return vo;
    }

    public static TransRentUserInfoEntity convertTransUser(AotuInfor005Entity entity) {
        TransRentUserInfoEntity vo = new TransRentUserInfoEntity();
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
        return vo;
    }

    public static PickDeliverOrderInfoEntity convertPickOrderInfo(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService,
                                                                  Map<String, List<SysDictEntity>> dictMap) {
        PickDeliverOrderInfoEntity pickOrderInfoVo = new PickDeliverOrderInfoEntity();
        pickOrderInfoVo.setOrderNo(entity.getAutoInf05C050());
        pickOrderInfoVo.setPickDeliverOrderNo(String.valueOf(entity.getAutoInf05C073()));
        AutoDictDataEntity serverTypeDict = sqlServerTackBackService.getDictData(sqlServer_serverType_code, entity.getAutoInf05C002());
        Optional.ofNullable(serverTypeDict).ifPresent(dict -> {
            pickOrderInfoVo.setServiceType(getCodeByLabel(dictMap.get(FLOW_SERVER_TYPE), serverTypeDict.getName()));
        });
        pickOrderInfoVo.setIsSupply(NEG_ZERO);
        if (StringUtils.equalsIgnoreCase(entity.getAutoInf05C002(), sqlServer_is_supply)) {
            pickOrderInfoVo.setIsSupply(INT_ONE);
        }
        AutoDictDataEntity oprTypeDict = sqlServerTackBackService.getDictData(sqlServer_oprType_code, entity.getAutoInf05C003());
        Optional.ofNullable(oprTypeDict).ifPresent(dict -> {
            pickOrderInfoVo.setOprType(getCodeByLabel(dictMap.get(PICK_BACK_OPR_TYPE), oprTypeDict.getName()));
        });

        int scheduleStatusKey = TackBackConvert.setScheduleStatus(entity, sqlServerTackBackService);
        pickOrderInfoVo.setScheduleStatus(scheduleStatusKey);

        if (Objects.nonNull(entity.getAutoInf05C059())) {
            // AutoDictDataEntity fieldAppDict = sqlServerTackBackService.getDictData(sqlServer_field_app_type, String.valueOf(entity.getAutoInf05C059()));
            String fieldApp = sqlServerTackBackService.selectCurByCond(entity.getAutoInf05Id());
            pickOrderInfoVo.setFieldAppStatus(getCodeByLabel1(dictMap.get(FLOW_NODE_NAME_TYPE), fieldApp));
        }

        pickOrderInfoVo.setIsDirectCompensate(getObjOrDefault(entity.getAutoInf05C093(), Integer::parseInt, null));
        pickOrderInfoVo.setChannelKey(getObjOrDefault(entity.getAutoInf05Channeltype(), Integer::parseInt, null));
        pickOrderInfoVo.setServiceClass(getObjOrDefault(entity.getAutoInf05Servertype(), Integer::parseInt, null));
        pickOrderInfoVo.setIsFree(getObjOrDefault(entity.getAutoInf05Isfree(), Integer::parseInt, null));
        pickOrderInfoVo.setServiceFee(getObjOrDefault(entity.getAutoInf05Freemoney(), BigDecimal::new, null));
        pickOrderInfoVo.setReportNo(entity.getAutoInf05Baoanno());
        pickOrderInfoVo.setGoldKey(entity.getAutoInf05Keyperson());
        pickOrderInfoVo.setOrderChannelSource(getCodeByLabel(dictMap.get(ORDER_CHANNEL_SOURCE), entity.getAutoInf05Dqly()));
        pickOrderInfoVo.setEstimateServiceTime(entity.getAutoInf05Extime());

        pickOrderInfoVo.setPickTime(entity.getAutoInf05C004());
        pickOrderInfoVo.setDeliverTime(entity.getAutoInf05Qtime());

        pickOrderInfoVo.setPickAddr(entity.getAutoInf05C010());
        pickOrderInfoVo.setPickLong(getObjOrDefault(entity.getAutoInf05Taddrlon(), BigDecimal::new, null));
        pickOrderInfoVo.setPickLat(getObjOrDefault(entity.getAutoInf05Taddrlat(), BigDecimal::new, null));

        pickOrderInfoVo.setDeliverAddr(entity.getAutoInf05C012());
        pickOrderInfoVo.setDeliverLong(getObjOrDefault(entity.getAutoInf05Saddrlon(), BigDecimal::new, null));
        pickOrderInfoVo.setDeliverLat(getObjOrDefault(entity.getAutoInf05Saddrlat(), BigDecimal::new, null));
        if (objEquals(pickOrderInfoVo.getServiceType(), INT_FOUR, Integer::equals)) {
            pickOrderInfoVo.setPickTime(entity.getAutoInf05Htime());
            pickOrderInfoVo.setDeliverTime(entity.getAutoInf05Aftertime());
        }
        pickOrderInfoVo.setTakeNote(entity.getAutoInf05Takenote());

        pickOrderInfoVo.setRepairShopContact(entity.getAutoInf05C091());
        pickOrderInfoVo.setRepairShopPhone(entity.getAutoInf05C092());
        pickOrderInfoVo.setStoreName(entity.getAutoInf05Sname());
        pickOrderInfoVo.setStorePhone(entity.getAutoInf05Sphone());

        pickOrderInfoVo.setCreateTime(entity.getPubCrttime());
        SyspUserEntity syspUserEntity = sqlServerTackBackService.selectUserById(entity.getPubCrtusid());
        pickOrderInfoVo.setCreateOp(getObjOrDefault(syspUserEntity, SyspUserEntity::getSyspUseLoginname, null));
        pickOrderInfoVo.setUpdateTime(entity.getPubFupdtime());
        SyspUserEntity syspUser = sqlServerTackBackService.selectUserById(entity.getPubFupdusid());
        pickOrderInfoVo.setUpdateOp(getObjOrDefault(syspUser, SyspUserEntity::getSyspUseLoginname, null));
        return pickOrderInfoVo;
    }

    public static PickDeliverScheduleInfoEntity convertScheduleInfo(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService,
                                                                    Map<String, List<SysDictEntity>> dictMap, PickDeliverOrderInfoEntity tackBackOrder,
                                                                    EmployCompanyMapper employCompanyMapper) {
        PickDeliverScheduleInfoEntity scheduleInfoVo = new PickDeliverScheduleInfoEntity();
        scheduleInfoVo.setPickDeliverOrderNo(String.valueOf(entity.getAutoInf05C073()));
        scheduleInfoVo.setScheduleStatus(tackBackOrder.getScheduleStatus());
        scheduleInfoVo.setFieldAppStatus(tackBackOrder.getFieldAppStatus());
        scheduleInfoVo.setIsSupplierDistribute(0);
        if (CollectionUtils.isNotEmpty(sqlServerTackBackService.queryFlow(entity.getAutoInf05Id(), null, null, null, null, sqlServer_new, null))) {
            scheduleInfoVo.setIsSupplierDistribute(1);
            Optional.ofNullable(entity.getAutoInf05Sercompany()).filter(StringUtils::isNotEmpty).ifPresent(supplier -> {
                AutoDictDataEntity companyDict = sqlServerTackBackService.getDictData(sqlServer_company, supplier);
                scheduleInfoVo.setSupplierCompanyId(getObjOrDefault(getObjOrDefault(companyDict,
                        c -> getObjOrDefault(employCompanyMapper.getObjByName(c.getName()), EmployCompanyEntity::getId, null),
                        null), Function.identity(), null));
                scheduleInfoVo.setSupplierCompanyName(getObjOrDefault(companyDict, AutoDictDataEntity::getName, null));
            });

        }
        if (StringUtils.isNotEmpty(entity.getAutoInf05C022())) {
            AutoOther001Entity other001Entity = sqlServerTackBackService.getTackBackPerson(Long.parseLong(entity.getAutoInf05C022()));
            Optional.ofNullable(other001Entity).ifPresent(x -> {
                AutoDictDataEntity companyDict = sqlServerTackBackService.getDictData(sqlServer_company, x.getAutoOt01Sercompany());
                scheduleInfoVo.setSupplierCompanyId(getObjOrDefault(getObjOrDefault(companyDict,
                        c -> getObjOrDefault(employCompanyMapper.getObjByName(c.getName()), EmployCompanyEntity::getId, null),
                        null), Function.identity(), null));
                scheduleInfoVo.setSupplierCompanyName(getObjOrDefault(companyDict, AutoDictDataEntity::getName, null));
                scheduleInfoVo.setUserId(x.getAutoOt01Id().intValue());
                scheduleInfoVo.setUserName(x.getAutoOt01C002());
                scheduleInfoVo.setUserPhone(x.getAutoOt01C003());
            });
        }
        AutoDictDataEntity ownUserDict = sqlServerTackBackService.getDictData(sqlServer_owen_type, entity.getAutoInf05Isqsctype());
        scheduleInfoVo.setIsOwnPerson(getObjOrDefault(ownUserDict, x -> getCodeByLabel(dictMap.get(IS_OWN_USER), x.getName()), null));
        Optional.ofNullable(entity.getAutoInf05Sendtype()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity dispatchDict = sqlServerTackBackService.getDictData(sqlServer_dispatch_type, x);
            String label = getObjOrDefault(dispatchDict, AutoDictDataEntity::getName, null);
            scheduleInfoVo.setDistributeType(getCodeByLabel(dictMap.get(DISPATCH_TYPE), label));
        });

        scheduleInfoVo.setFlightNo(entity.getAutoInf05Flightno());
        scheduleInfoVo.setScheduleMemo(entity.getAutoInf05C025());
        scheduleInfoVo.setCreateOp(tackBackOrder.getCreateOp());
        scheduleInfoVo.setCreateTime(tackBackOrder.getCreateTime());
        scheduleInfoVo.setUpdateOp(tackBackOrder.getUpdateOp());
        scheduleInfoVo.setUpdateTime(tackBackOrder.getUpdateTime());
        scheduleInfoVo.setEditStatus(TackBackConvert.getEditStatus(scheduleInfoVo.getSupplierCompanyId(), entity.getAutoInf05Id(), sqlServerTackBackService));
        return scheduleInfoVo;
    }

    public static PickDeliverPickInfoEntity convertPickInfo(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService,
                                                            Map<String, List<SysDictEntity>> dictMap) {
        PickDeliverPickInfoEntity pickInfoVo = new PickDeliverPickInfoEntity();
        pickInfoVo.setPickDeliverOrderNo(String.valueOf(entity.getAutoInf05C073()));
        pickInfoVo.setEditStatus(entity.getPubEditflag());
        pickInfoVo.setRealPickTime(entity.getAutoInf05C026());
        pickInfoVo.setPickOil(getObjOrDefault(entity.getAutoInf05Qoilm(), BigDecimal::new, null));
        pickInfoVo.setEstimatePickKilo(getObjOrDefault(entity.getAutoInf05Getkilometre(), BigDecimal::new, null));
        pickInfoVo.setPickKilo(getObjOrDefault(entity.getAutoInf05Qmileage(), Integer::parseInt, null));

        AutoDictDataEntity vehicleDict = sqlServerTackBackService.getDictData(sqlServer_vehicle_status_type, entity.getAutoInf05Vstate());
        Optional.ofNullable(vehicleDict).ifPresent(vehicle -> {
            int vehicleStatusKey = getCodeByLabel(dictMap.get(VEHICLE_STATUS), vehicle.getName());
            pickInfoVo.setVehicleStatus(vehicleStatusKey);
        });
        pickInfoVo.setChangePickAddress(entity.getAutoInf05Newc010());
        pickInfoVo.setChangePickLong(getObjOrDefault(entity.getAutoInf05Newc010lon(), BigDecimal::new, null));
        pickInfoVo.setChangePickLat(getObjOrDefault(entity.getAutoInf05Newc010lat(), BigDecimal::new, null));
        return pickInfoVo;
    }

    public static PickDeliverDeliverInfoEntity convertDeliverInfo(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService,
                                                                  Map<String, List<SysDictEntity>> dictMap) {
        PickDeliverDeliverInfoEntity deliverInfoVo = new PickDeliverDeliverInfoEntity();
        deliverInfoVo.setPickDeliverOrderNo(String.valueOf(entity.getAutoInf05C073()));
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
            deliverInfoVo.setDelaySendReason(delayStatusKey);
        });
        return deliverInfoVo;
    }

    public static PickDeliverFeeWithMemoEntity convertFeeInfoMemo(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService,
                                                                  Map<String, List<SysDictEntity>> dictMap) {
        PickDeliverFeeWithMemoEntity vo = new PickDeliverFeeWithMemoEntity();
        vo.setPickDeliverOrderNo(String.valueOf(entity.getAutoInf05C073()));
        Optional.ofNullable(entity.getAutoInf05Bears()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity bearDict = sqlServerTackBackService.getDictData(sqlServer_bear_type, x);
            String str = getObjOrDefault(bearDict, AutoDictDataEntity::getName, null);
            vo.setBearType(getCodeByLabel(dictMap.get(BEAR_TYPE), str));
        });
        vo.setAllowanceOwnerReasonType(entity.getAutoInf05Czcost());
        vo.setAllowanceTenantReasonType(entity.getAutoInf05Zkcost());

        Optional.ofNullable(entity.getAutoInf05Opreason()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity ownerAdjDict = sqlServerTackBackService.getDictData(sqlServer_owner_ajd_type, x);
            vo.setAdjustPriceOwnerReasonType(getCodeByLabel(dictMap.get(ADJ_PRICE_OWNER_REASON_TYPE), ownerAdjDict.getName()));
        });
        Optional.ofNullable(entity.getAutoInf05Rpreason()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity ownerAdjDict = sqlServerTackBackService.getDictData(sqlServer_renter_ajd_type, x);
            vo.setAdjustPriceTenantReasonType(getObjOrDefault(ownerAdjDict, t -> Integer.parseInt(t.getCode()) - 1, null));
        });
        vo.setFeeMemo(entity.getAutoInf05C082());
        vo.setReportMemo(entity.getAutoInf05Appnote());
        vo.setFineMemo(entity.getAutoInf05C084());
        vo.setEditStatus(entity.getPubEditflag());
        return vo;
    }

    public static List<FeeRecordEntity> convertFee(AotuInfor005Entity entity, SqlServerTackBackService sqlServerTackBackService,
                                                   Map<String, List<SysDictEntity>> dictMap, Integer pickDeliverFeeId, List<AttrLabelEntity> labelEntities) {
        List<FeeRecordEntity> list = Lists.newArrayList();
        FeeRecordEntity vo = new FeeRecordEntity();
        vo.setPickDeliverFeeId(pickDeliverFeeId);
        vo.setEditStatus(entity.getPubEditflag());
        vo.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "ownerExtraAllowance"),
                AttrLabelEntity::getId, 0));
        vo.setAttrCode("ownerExtraAllowance");
        vo.setFeeValue(getObjOrDefault(entity.getAutoInf05Czlimit(), BigDecimal::new, null));
        list.add(vo);

        FeeRecordEntity renterExtraPrice = new FeeRecordEntity();
        renterExtraPrice.setPickDeliverFeeId(pickDeliverFeeId);
        renterExtraPrice.setEditStatus(entity.getPubEditflag());
        renterExtraPrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "renterExtraAllowance"),
                AttrLabelEntity::getId, 0));
        renterExtraPrice.setAttrCode("renterExtraAllowance");
        renterExtraPrice.setFeeValue(getObjOrDefault(entity.getAutoInf05Zklimit(), BigDecimal::new, null));
        list.add(renterExtraPrice);

        FeeRecordEntity ownerAdjPrice = new FeeRecordEntity();
        ownerAdjPrice.setPickDeliverFeeId(pickDeliverFeeId);
        ownerAdjPrice.setEditStatus(entity.getPubEditflag());
        ownerAdjPrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "ownerReAdjust"),
                AttrLabelEntity::getId, 0));
        ownerAdjPrice.setAttrCode("ownerReAdjust");
        ownerAdjPrice.setFeeValue(entity.getAutoInf05Oprice());
        list.add(ownerAdjPrice);

        FeeRecordEntity renterAdjPrice = new FeeRecordEntity();
        renterAdjPrice.setPickDeliverFeeId(pickDeliverFeeId);
        renterAdjPrice.setEditStatus(entity.getPubEditflag());
        renterAdjPrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "renterReAjust"),
                AttrLabelEntity::getId, 0));
        renterAdjPrice.setAttrCode("renterReAjust");
        renterAdjPrice.setFeeValue(entity.getAutoInf05Rprice());
        list.add(renterAdjPrice);

        FeeRecordEntity basePrice = new FeeRecordEntity();
        basePrice.setPickDeliverFeeId(pickDeliverFeeId);
        basePrice.setEditStatus(entity.getPubEditflag());
        basePrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "baseattr"),
                AttrLabelEntity::getId, 0));
        basePrice.setAttrCode("baseattr");
        basePrice.setFeeValue(entity.getAutoInf05C080());
        list.add(basePrice);

        FeeRecordEntity extPrice = new FeeRecordEntity();
        extPrice.setPickDeliverFeeId(pickDeliverFeeId);
        extPrice.setEditStatus(entity.getPubEditflag());
        extPrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "extraattr"),
                AttrLabelEntity::getId, 0));
        extPrice.setAttrCode("extraattr");
        extPrice.setFeeValue(entity.getAutoInf05C081());
        list.add(extPrice);

        FeeRecordEntity trafficAllowPrice = new FeeRecordEntity();
        trafficAllowPrice.setPickDeliverFeeId(pickDeliverFeeId);
        trafficAllowPrice.setEditStatus(entity.getPubEditflag());
        trafficAllowPrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "trafficAllowance"),
                AttrLabelEntity::getId, 0));
        trafficAllowPrice.setAttrCode("trafficAllowance");
        trafficAllowPrice.setFeeValue(getObjOrDefault(entity.getAutoInf05Jtbtcost(), BigDecimal::new, null));
        list.add(trafficAllowPrice);

        FeeRecordEntity fuelPrice = new FeeRecordEntity();
        fuelPrice.setPickDeliverFeeId(pickDeliverFeeId);
        fuelPrice.setEditStatus(entity.getPubEditflag());
        fuelPrice.setExpenseType("expenseFuel");
        Optional.ofNullable(entity.getAutoInf05C075()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity expenseOilDict = sqlServerTackBackService.getDictData(sqlServer_oil_expense_type, x);
            String str = getObjOrDefault(expenseOilDict, AutoDictDataEntity::getName, null);
            fuelPrice.setFeeType(getCodeByLabel(dictMap.get(EXPENSE_FUEL), str));
        });
        fuelPrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "fuelattr"),
                AttrLabelEntity::getId, 0));
        fuelPrice.setAttrCode("fuelattr");
        fuelPrice.setFeeValue(getObjOrDefault(convertFeeToDouble(entity.getAutoInf05C054()), BigDecimal::new, null));
        list.add(fuelPrice);

        FeeRecordEntity parkPrice = new FeeRecordEntity();
        parkPrice.setPickDeliverFeeId(pickDeliverFeeId);
        parkPrice.setEditStatus(entity.getPubEditflag());
        parkPrice.setExpenseType("expensePark");
        Optional.ofNullable(entity.getAutoInf05C075()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity expenseOilDict = sqlServerTackBackService.getDictData(sqlServer_park_expense_type, x);
            String str = getObjOrDefault(expenseOilDict, AutoDictDataEntity::getName, null);
            parkPrice.setFeeType(getCodeByLabel(dictMap.get(EXPENSE_PARK), str));
        });
        parkPrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "parkattr"),
                AttrLabelEntity::getId, 0));
        parkPrice.setAttrCode("parkattr");
        parkPrice.setFeeValue(getObjOrDefault(convertFeeToDouble(entity.getAutoInf05C055()), BigDecimal::new, null));
        list.add(parkPrice);

        FeeRecordEntity trafficPrice = new FeeRecordEntity();
        trafficPrice.setPickDeliverFeeId(pickDeliverFeeId);
        trafficPrice.setEditStatus(entity.getPubEditflag());
        trafficPrice.setExpenseType("expenseTraffic");
        Optional.ofNullable(entity.getAutoInf05C075()).filter(StringUtils::isNotEmpty).ifPresent(x -> {
            AutoDictDataEntity expenseOilDict = sqlServerTackBackService.getDictData(sqlServer_traffic_expense_type, x);
            String str = getObjOrDefault(expenseOilDict, AutoDictDataEntity::getName, null);
            trafficPrice.setFeeType(getCodeByLabel(dictMap.get(EXPENSE_TRAFFIC), str));
        });
        trafficPrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "trafficattr"),
                AttrLabelEntity::getId, 0));
        trafficPrice.setAttrCode("trafficattr");
        trafficPrice.setFeeValue(getObjOrDefault(convertFeeToDouble(entity.getAutoInf05C056()), BigDecimal::new, null));
        list.add(trafficPrice);

        FeeRecordEntity roadPrice = new FeeRecordEntity();
        roadPrice.setPickDeliverFeeId(pickDeliverFeeId);
        roadPrice.setEditStatus(entity.getPubEditflag());
        roadPrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "roadBridgeattr"),
                AttrLabelEntity::getId, 0));
        roadPrice.setAttrCode("roadBridgeattr");
        roadPrice.setFeeValue(getObjOrDefault(entity.getAutoInf05C058(), BigDecimal::new, null));
        list.add(trafficAllowPrice);

        FeeRecordEntity washPrice = new FeeRecordEntity();
        washPrice.setPickDeliverFeeId(pickDeliverFeeId);
        washPrice.setEditStatus(entity.getPubEditflag());
        washPrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "washVehicleattr"),
                AttrLabelEntity::getId, 0));
        washPrice.setAttrCode("washVehicleattr");
        washPrice.setFeeValue(getObjOrDefault(convertFeeToDouble(entity.getAutoInf05C057()), BigDecimal::new, null));
        list.add(washPrice);

        FeeRecordEntity finePrice = new FeeRecordEntity();
        finePrice.setPickDeliverFeeId(pickDeliverFeeId);
        finePrice.setEditStatus(entity.getPubEditflag());
        finePrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "fineattr"),
                AttrLabelEntity::getId, 0));
        finePrice.setAttrCode("fineattr");
        finePrice.setFeeValue(entity.getAutoInf05C083());
        list.add(finePrice);

        FeeRecordEntity comPrice = new FeeRecordEntity();
        comPrice.setPickDeliverFeeId(pickDeliverFeeId);
        comPrice.setEditStatus(entity.getPubEditflag());
        comPrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "compensateattr"),
                AttrLabelEntity::getId, 0));
        comPrice.setAttrCode("compensateattr");
        comPrice.setFeeValue(entity.getAutoInf05Compensationfees());
        list.add(comPrice);

        FeeRecordEntity comMoneyPrice = new FeeRecordEntity();
        comMoneyPrice.setPickDeliverFeeId(pickDeliverFeeId);
        comMoneyPrice.setEditStatus(entity.getPubEditflag());
        comMoneyPrice.setFeeLabelId(getObj(labelEntities, attr -> StringUtils.equalsIgnoreCase(attr.getAttrCode(), "compensateMoney"),
                AttrLabelEntity::getId, 0));
        comMoneyPrice.setAttrCode("compensateMoney");
        comMoneyPrice.setFeeValue(entity.getAutoInf05Peichangjine());
        list.add(comMoneyPrice);
        return list;
    }
}
