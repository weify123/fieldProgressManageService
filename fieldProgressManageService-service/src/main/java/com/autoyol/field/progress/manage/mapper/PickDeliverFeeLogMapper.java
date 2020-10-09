package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.PickDeliverFeeLogWithMemoEntity;

public interface PickDeliverFeeLogMapper {

    PickDeliverFeeLogWithMemoEntity selectByCond(PickDeliverFeeLogWithMemoEntity record);

    int insert(PickDeliverFeeLogWithMemoEntity record);

    int insertSelective(PickDeliverFeeLogWithMemoEntity record);
}