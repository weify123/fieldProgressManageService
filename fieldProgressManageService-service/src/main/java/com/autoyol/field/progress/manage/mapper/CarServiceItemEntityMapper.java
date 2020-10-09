package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.CarServiceItemEntity;
import com.autoyol.field.progress.manage.request.car.QueryCarServiceItemReqVo;

import java.util.List;

public interface CarServiceItemEntityMapper {

    int insert(CarServiceItemEntity record);

    int countByEntity(CarServiceItemEntity record);

    List<CarServiceItemEntity> queryCarServiceItems(QueryCarServiceItemReqVo queryCarServiceItemReqVO);

    CarServiceItemEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(CarServiceItemEntity record);
}