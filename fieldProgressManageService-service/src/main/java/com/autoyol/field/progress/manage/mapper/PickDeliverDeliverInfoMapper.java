package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.PickDeliverDeliverInfoEntity;

public interface PickDeliverDeliverInfoMapper {

    PickDeliverDeliverInfoEntity selectByCond(PickDeliverDeliverInfoEntity record);

    int updateByTackBackOrderNo(PickDeliverDeliverInfoEntity record);

    int insertSelective(PickDeliverDeliverInfoEntity record);
}