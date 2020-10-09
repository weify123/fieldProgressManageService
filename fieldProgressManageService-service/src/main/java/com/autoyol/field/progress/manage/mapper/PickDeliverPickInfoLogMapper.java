package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.PickDeliverPickInfoLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PickDeliverPickInfoLogMapper {

    PickDeliverPickInfoLogEntity selectByCond(PickDeliverPickInfoLogEntity record);

    int insertSelective(PickDeliverPickInfoLogEntity record);
}