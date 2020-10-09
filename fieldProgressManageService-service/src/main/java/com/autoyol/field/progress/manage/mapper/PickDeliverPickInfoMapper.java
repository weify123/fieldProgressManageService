package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.PickDeliverPickInfoEntity;

public interface PickDeliverPickInfoMapper {

    PickDeliverPickInfoEntity selectByCond(PickDeliverPickInfoEntity record);

    int updateByTackBackOrderNo(PickDeliverPickInfoEntity record);

    int insertSelective(PickDeliverPickInfoEntity record);
}