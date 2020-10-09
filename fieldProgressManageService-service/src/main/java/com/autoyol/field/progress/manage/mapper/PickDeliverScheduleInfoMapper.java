package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.PickDeliverScheduleInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PickDeliverScheduleInfoMapper {

    List<PickDeliverScheduleInfoEntity> queryByPickBackNo(@Param("pickBackNoList") List<String> pickBackNoList);

    List<PickDeliverScheduleInfoEntity> queryByUserId(@Param("userId") Integer userId);

    PickDeliverScheduleInfoEntity selectByCond(PickDeliverScheduleInfoEntity entity);

    int batchInsert(@Param("list") List<PickDeliverScheduleInfoEntity> list);

    int batchUpdate(@Param("list") List<PickDeliverScheduleInfoEntity> list);

    int batchCancelTackBackOrder(@Param("pickBackNoList") List<String> pickBackNoList);

    int updateByPickDeliverNo(PickDeliverScheduleInfoEntity record);

    int updateStatusByPickDeliverNo(PickDeliverScheduleInfoEntity record);

    int clearScheduleByPickDeliverNo(@Param("pickDeliverOrderNo") String pickDeliverOrderNo);

    int batchClearScheduleByPickDeliverNo(@Param("list") List<String> list);

    int updateByCond(PickDeliverScheduleInfoEntity record);

    int insertSelective(PickDeliverScheduleInfoEntity record);
}