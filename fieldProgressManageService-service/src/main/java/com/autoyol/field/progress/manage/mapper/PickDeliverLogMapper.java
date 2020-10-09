package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.PickDeliverLogEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PickDeliverLogMapper {

    List<PickDeliverLogEntity> queryByCond(PickDeliverLogEntity record);

    int queryCountByCond(PickDeliverLogEntity record);

    int insertSelective(PickDeliverLogEntity record);

    int batchInsert(@Param("list") List<PickDeliverLogEntity> list);
}