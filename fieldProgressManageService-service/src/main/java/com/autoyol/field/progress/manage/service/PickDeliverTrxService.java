package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.mapper.*;
import com.autoyol.field.progress.manage.request.tackback.TransOrderReqVo;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@Service
public class PickDeliverTrxService {

    @Resource
    PickDeliverOrderInfoMapper pickDeliverOrderInfoMapper;

    @Resource
    PickDeliverScheduleStatusLogMapper pickDeliverScheduleStatusLogMapper;

    @Resource
    PickDeliverScheduleInfoMapper pickDeliverScheduleInfoMapper;

    @Resource
    PickDeliverScheduleInfoLogMapper pickDeliverScheduleInfoLogMapper;

    @Resource
    PickDeliverPickInfoMapper pickDeliverPickInfoMapper;

    @Resource
    PickDeliverDeliverInfoMapper pickDeliverDeliverInfoMapper;

    @Resource
    PickDeliverFileMapper pickDeliverFileMapper;

    @Resource
    TransOrderInfoMapper transOrderInfoMapper;

    @Resource
    TransOrderInfoLogMapper transOrderInfoLogMapper;

    @Resource
    PickDeliverLogMapper pickDeliverLogMapper;

    @Resource
    PickDeliverFeeMapper pickDeliverFeeMapper;

    @Resource
    FeeRecordMapper feeRecordMapper;

    @Transactional
    public void updateTackBackScheduleStatusTrx(PickDeliverOrderInfoEntity orderInfoEntity, PickDeliverScheduleStatusLogEntity scheduleStatusLogEntity) {
        if (orderInfoEntity.getScheduleStatus() == INT_TOW) {
            pickDeliverScheduleInfoMapper.clearScheduleByPickDeliverNo(orderInfoEntity.getPickDeliverOrderNo());
        }else{
            PickDeliverScheduleInfoEntity scheduleInfoEntity = new PickDeliverScheduleInfoEntity();
            scheduleInfoEntity.setPickDeliverOrderNo(orderInfoEntity.getPickDeliverOrderNo());
            scheduleInfoEntity.setScheduleStatus(orderInfoEntity.getScheduleStatus());
            scheduleInfoEntity.setFieldAppStatus(orderInfoEntity.getFieldAppStatus());
            pickDeliverScheduleInfoMapper.updateStatusByPickDeliverNo(scheduleInfoEntity);
        }
        pickDeliverOrderInfoMapper.updateSelectById(orderInfoEntity);
        pickDeliverScheduleStatusLogMapper.insertSelective(scheduleStatusLogEntity);
    }

    @Transactional
    public void batchDispatchSupplier(List<PickDeliverOrderInfoEntity> updatePickOrderList, List<PickDeliverScheduleStatusLogEntity> scheduleStatusLogList,
                                      List<PickDeliverScheduleInfoEntity> addScheduleList, List<PickDeliverScheduleInfoEntity> updateScheduleList,
                                      List<PickDeliverScheduleInfoLogEntity> addScheduleLogScheduleList) {
        pickDeliverOrderInfoMapper.batchUpdate(updatePickOrderList);
        pickDeliverScheduleStatusLogMapper.batchInsert(scheduleStatusLogList);

        if (!CollectionUtils.isEmpty(addScheduleList)) {
            pickDeliverScheduleInfoMapper.batchInsert(addScheduleList);
        }
        if (!CollectionUtils.isEmpty(updateScheduleList)) {
            pickDeliverScheduleInfoMapper.batchUpdate(updateScheduleList);
        }
        pickDeliverScheduleInfoLogMapper.batchInsert(addScheduleLogScheduleList);
    }

    @Transactional
    public void updateTackBackPickInfo(PickDeliverPickInfoEntity pickInfoEntity,
                                       List<PickDeliverFileEntity> list, List<PickDeliverFileEntity> updateList) {
        pickDeliverPickInfoMapper.updateByTackBackOrderNo(pickInfoEntity);

        if (!CollectionUtils.isEmpty(list)) {
            pickDeliverFileMapper.batchInsert(list);
        }

        if (!CollectionUtils.isEmpty(updateList)) {
            pickDeliverFileMapper.batchDelete(updateList);
        }
    }

    @Transactional
    public void insertTackBackPickInfoAndFile(PickDeliverPickInfoEntity pickInfoEntity, List<PickDeliverFileEntity> list,
                                              List<PickDeliverFileEntity> updateList) {
        pickDeliverPickInfoMapper.insertSelective(pickInfoEntity);
        if (!CollectionUtils.isEmpty(list)) {
            pickDeliverFileMapper.batchInsert(list);
        }

        if (!CollectionUtils.isEmpty(updateList)) {
            pickDeliverFileMapper.batchDelete(updateList);
        }
    }

    @Transactional
    public void insertTackBackDeliverInfoAndFile(PickDeliverDeliverInfoEntity deliverInfoEntity, List<PickDeliverFileEntity> list,
                                                 List<PickDeliverFileEntity> updateList) {
        pickDeliverDeliverInfoMapper.insertSelective(deliverInfoEntity);
        if (!CollectionUtils.isEmpty(list)) {
            pickDeliverFileMapper.batchInsert(list);
        }

        if (!CollectionUtils.isEmpty(updateList)) {
            pickDeliverFileMapper.batchDelete(updateList);
        }
    }

    @Transactional
    public void updateTackBackDeliverInfo(PickDeliverDeliverInfoEntity deliverInfoEntity,
                                          List<PickDeliverFileEntity> list, List<PickDeliverFileEntity> updateList) {
        pickDeliverDeliverInfoMapper.updateByTackBackOrderNo(deliverInfoEntity);

        if (!CollectionUtils.isEmpty(list)) {
            pickDeliverFileMapper.batchInsert(list);
        }

        if (!CollectionUtils.isEmpty(updateList)) {
            pickDeliverFileMapper.batchDelete(updateList);
        }
    }

    public void updateTransOrderInfo(TransOrderReqVo reqVo) {
        TransOrderInfoEntity transOrderInfoEntity = new TransOrderInfoEntity();
        transOrderInfoEntity.setEditStatus(reqVo.getEditStatus());
        transOrderInfoEntity.setOrderNo(reqVo.getTransOrderNo());
        transOrderInfoEntity.setRenewOrderNo(reqVo.getRenewOrderNo());
        transOrderInfoEntity.setUpdateOp(reqVo.getHandleUser());
        transOrderInfoMapper.updateByOrderNo(transOrderInfoEntity);
    }

    @Transactional
    public void updateTackBackFeeInfo(PickDeliverFeeWithMemoEntity feeWithMemoEntity, List<FeeRecordEntity> feeRecordEntityList) {
        pickDeliverFeeMapper.updateByCond(feeWithMemoEntity);
        if (!CollectionUtils.isEmpty(feeRecordEntityList)) {
            feeRecordMapper.batchUpdate(feeRecordEntityList);
        }
    }

    public int setTackBackScheduleInfo(String handleUser, boolean isInsert, PickDeliverScheduleInfoEntity scheduleInfo) {

        if (isInsert) {
            scheduleInfo.setCreateOp(handleUser);
            scheduleInfo.setCreateTime(LocalDateUtil.convertToDate(LocalDateTime.now()));
            return pickDeliverScheduleInfoMapper.insertSelective(scheduleInfo);
        }
        scheduleInfo.setUpdateOp(handleUser);
        return pickDeliverScheduleInfoMapper.updateByPickDeliverNo(scheduleInfo);
    }

    @Transactional
    public void cancelOrder(List<PickDeliverOrderInfoEntity> tackBackList, List<PickDeliverScheduleStatusLogEntity> scheduleStatusLogList,
                            List<String> tackBackOrderNoList){
        pickDeliverOrderInfoMapper.transCancelBatchUpdate(tackBackList);
        pickDeliverScheduleStatusLogMapper.batchInsert(scheduleStatusLogList);
        pickDeliverScheduleInfoMapper.batchCancelTackBackOrder(tackBackOrderNoList);
    }

    @Transactional
    public void changeOrder(List<PickDeliverLogEntity> deliverLogList, List<PickDeliverOrderInfoEntity> preUpdateList, List<String> pickBackNoList,
                            List<PickDeliverScheduleInfoLogEntity> scheduleInfoLogList,List<PickDeliverScheduleStatusLogEntity> scheduleStatusLogList,
                            TransOrderInfoEntity transOrderInfo, TransOrderInfoEntity transOrderInfoEntity){
        if (CollectionUtils.isEmpty(preUpdateList)) {
            return;
        }
        pickDeliverOrderInfoMapper.batchBackUpdate(preUpdateList);
        pickDeliverLogMapper.batchInsert(deliverLogList);

        pickDeliverScheduleInfoMapper.batchClearScheduleByPickDeliverNo(pickBackNoList);
        pickDeliverScheduleInfoLogMapper.batchInsert(scheduleInfoLogList);

        pickDeliverScheduleStatusLogMapper.batchInsert(scheduleStatusLogList);
        if (Objects.nonNull(transOrderInfo)){
            transOrderInfoMapper.updateByOrderNo(transOrderInfo);
            transOrderInfoLogMapper.insertSelective(transOrderInfoEntity);
        }
    }


}
