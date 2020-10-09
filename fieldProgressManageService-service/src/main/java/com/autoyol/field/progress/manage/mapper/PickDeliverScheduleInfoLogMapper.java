package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.PickDeliverScheduleInfoLogEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PickDeliverScheduleInfoLogMapper {

    PickDeliverScheduleInfoLogEntity selectByCond(PickDeliverScheduleInfoLogEntity entity);

    int batchInsert(@Param("list") List<PickDeliverScheduleInfoLogEntity> list);

    int insertSelective(PickDeliverScheduleInfoLogEntity record);
}