package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.PickDeliverScheduleStatusLogEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PickDeliverScheduleStatusLogMapper {

    List<PickDeliverScheduleStatusLogEntity> queryObjListByOrderId(@Param("pickDeliverOrderNo") String pickDeliverOrderNo);

    int batchInsert(@Param("list") List<PickDeliverScheduleStatusLogEntity> list);

    int insert(PickDeliverScheduleStatusLogEntity record);

    int insertSelective(PickDeliverScheduleStatusLogEntity record);
}