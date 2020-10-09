package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.PickDeliverDeliverInfoLogEntity;

public interface PickDeliverDeliverInfoLogMapper {

    PickDeliverDeliverInfoLogEntity selectByCond(PickDeliverDeliverInfoLogEntity record);

    int insertSelective(PickDeliverDeliverInfoLogEntity record);
}