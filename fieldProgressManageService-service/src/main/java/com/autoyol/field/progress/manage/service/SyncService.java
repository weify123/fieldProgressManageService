package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.convertsqlserver.TackBackConvertEntity;
import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.entity.sqlserver.AotuInfor005Entity;
import com.autoyol.field.progress.manage.mapper.EmployCompanyMapper;
import com.autoyol.field.progress.manage.request.BaseRequest;
import com.autoyol.field.progress.manage.service.sqlserver.SqlServerTackBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

public interface SyncService {
    Logger logger = LoggerFactory.getLogger(SyncService.class);

    List<String> typeList = buildTypeList(FLOW_SERVER_TYPE, PUBLISH_TYPE, PICK_BACK_OPR_TYPE, PICK_DELIVER_SCHEDULE_STATUS, DELAY_REASON,
            IS_OWN_USER, DISPATCH_TYPE, VEHICLE_STATUS, FLOW_NODE_NAME_TYPE, TRANS_SOURCE, TRANS_SCENE_SOURCE, VEHICLE_TYPE, OFFLINE_ORDER_TYPE,
            BEAR_TYPE, SUBSIDY_OWNER_REASON_TYPE, SUBSIDY_RENT_REASON_TYPE, ADJ_PRICE_OWNER_REASON_TYPE, ADJ_PRICE_RENT_REASON_TYPE,
            EXPENSE_FUEL, EXPENSE_PARK, EXPENSE_TRAFFIC, ORDER_CHANNEL_SOURCE);

    /**
     * 同步本地 单条
     *
     * @param dictMap
     * @param transOrderInfoEntity
     * @param entity
     * @throws Exception
     */
    default void syncLocal(Map<String, List<SysDictEntity>> dictMap, TransOrderInfoEntity transOrderInfoEntity, AotuInfor005Entity entity,
                           SqlServerTackBackService sqlServerTackBackService, EmployCompanyMapper employCompanyMapper) throws Exception {
        PickDeliverOrderInfoEntity tackBackOrder = TackBackConvertEntity.convertPickOrderInfo(entity, sqlServerTackBackService, dictMap);
        PickDeliverScheduleInfoEntity scheduleInfoEntity = TackBackConvertEntity.convertScheduleInfo(entity, sqlServerTackBackService,
                dictMap, tackBackOrder, employCompanyMapper);
        TransVehicleInfoEntity transVehicleInfoEntity = TackBackConvertEntity.convertTransVehicle(entity, sqlServerTackBackService, dictMap);
        TransRentUserInfoEntity transUserInfoEntity = TackBackConvertEntity.convertTransUser(entity);

        PickDeliverPickInfoEntity pickInfo = TackBackConvertEntity.convertPickInfo(entity, sqlServerTackBackService, dictMap);
        PickDeliverDeliverInfoEntity deliverInfo = TackBackConvertEntity.convertDeliverInfo(entity, sqlServerTackBackService, dictMap);

        PickDeliverFeeWithMemoEntity feeMemoInfo = TackBackConvertEntity.convertFeeInfoMemo(entity, sqlServerTackBackService, dictMap);
        scheduleInfoEntity.setServiceType(tackBackOrder.getServiceType());
        pickInfo.setServiceType(tackBackOrder.getServiceType());
        deliverInfo.setServiceType(tackBackOrder.getServiceType());
        feeMemoInfo.setServiceType(tackBackOrder.getServiceType());

        sqlServerTackBackService.saveLocal(transOrderInfoEntity, transVehicleInfoEntity, transUserInfoEntity, tackBackOrder, scheduleInfoEntity,
                pickInfo, deliverInfo, feeMemoInfo, entity, dictMap);
    }

    /**
     * 同步本地 多条
     *
     * @param dictMap
     * @param entityList
     * @throws Exception
     */
    default void syncListLocal(Map<String, List<SysDictEntity>> dictMap, List<AotuInfor005Entity> entityList,
                               SqlServerTackBackService sqlServerTackBackService, EmployCompanyMapper employCompanyMapper) throws Exception {
        entityList.forEach(entity -> {
            TransOrderInfoEntity transOrderInfoEntity = null;
            try {
                transOrderInfoEntity = TackBackConvertEntity.convertTransOrder(entity, sqlServerTackBackService, dictMap);

                PickDeliverOrderInfoEntity tackBackOrder = TackBackConvertEntity.convertPickOrderInfo(entity, sqlServerTackBackService, dictMap);
                PickDeliverScheduleInfoEntity scheduleInfoEntity = TackBackConvertEntity.convertScheduleInfo(entity, sqlServerTackBackService,
                        dictMap, tackBackOrder, employCompanyMapper);
                TransVehicleInfoEntity transVehicleInfoEntity = TackBackConvertEntity.convertTransVehicle(entity, sqlServerTackBackService, dictMap);
                TransRentUserInfoEntity transUserInfoEntity = TackBackConvertEntity.convertTransUser(entity);

                PickDeliverPickInfoEntity pickInfo = TackBackConvertEntity.convertPickInfo(entity, sqlServerTackBackService, dictMap);
                PickDeliverDeliverInfoEntity deliverInfo = TackBackConvertEntity.convertDeliverInfo(entity, sqlServerTackBackService, dictMap);

                PickDeliverFeeWithMemoEntity feeMemoInfo = TackBackConvertEntity.convertFeeInfoMemo(entity, sqlServerTackBackService, dictMap);
                scheduleInfoEntity.setServiceType(tackBackOrder.getServiceType());
                pickInfo.setServiceType(tackBackOrder.getServiceType());
                deliverInfo.setServiceType(tackBackOrder.getServiceType());
                feeMemoInfo.setServiceType(tackBackOrder.getServiceType());

                sqlServerTackBackService.saveLocal(transOrderInfoEntity, transVehicleInfoEntity, transUserInfoEntity, tackBackOrder, scheduleInfoEntity,
                        pickInfo, deliverInfo, feeMemoInfo, entity, dictMap);
            } catch (Exception e) {
                logger.error("syncListLocal 失败,entity [{}] e {}", entity, e);
            }
        });

    }

    default Map<String, Object> buildScheduleMap(Long id, String serviceType, Integer editStatus, BaseRequest reqVo,
                                                 Map<String, List<SysDictEntity>> dictMap,
                                                 PickDeliverScheduleInfoEntity scheduleInfo, MsgService msgService,
                                                 EmployCompanyMapper employCompanyMapper) {
        Map<String, Object> map = msgService.buildMap();
        map.put("id", id);
        map.put("tackBackOrderNo", scheduleInfo.getPickDeliverOrderNo());
        map.put("serviceType", serviceType);
        map.put("editStatus", editStatus);
        map.put("isSupplierDistribute", Optional.ofNullable(scheduleInfo.getIsSupplierDistribute()).orElse(NEG_ZERO));
        map.put("companyId", getObjOrDefault(employCompanyMapper.getObjByName(scheduleInfo.getSupplierCompanyName()),
                EmployCompanyEntity::getRenYunId, null));
        map.put("companyName", scheduleInfo.getSupplierCompanyName());
        map.put("userId", scheduleInfo.getUserId());
        map.put("userName", scheduleInfo.getUserName());
        map.put("userPhone", scheduleInfo.getUserPhone());
        map.put("operatorNo", reqVo.getHandleUserNo());
        map.put("operatorName", reqVo.getHandleUser());
        map.put("isOwnUser", getLabelByCode(dictMap.get(IS_OWN_USER), String.valueOf(scheduleInfo.getIsOwnPerson())));
        map.put("dispatchType", getLabelByCode(dictMap.get(DISPATCH_TYPE), String.valueOf(scheduleInfo.getDistributeType())));
        map.put("scheduleMemo", scheduleInfo.getScheduleMemo());
        map.put("flightNo", scheduleInfo.getFlightNo());
        return map;
    }
}
