package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.PickDeliverFeeWithMemoEntity;

public interface PickDeliverFeeMapper {

    PickDeliverFeeWithMemoEntity selectByCond(PickDeliverFeeWithMemoEntity record);

    int updateByCond(PickDeliverFeeWithMemoEntity record);

    int insertSelective(PickDeliverFeeWithMemoEntity record);
}