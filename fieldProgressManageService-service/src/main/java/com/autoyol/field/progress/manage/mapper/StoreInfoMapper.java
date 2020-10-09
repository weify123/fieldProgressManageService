package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.StoreInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StoreInfoMapper {

    List<StoreInfoEntity> findAllByCond(StoreInfoEntity record);

    int existCond(StoreInfoEntity record);

    int updateById(StoreInfoEntity record);

    int insert(StoreInfoEntity record);

    int insertSelective(StoreInfoEntity record);

    int batchInsert(@Param("list") List<StoreInfoEntity> list);

    int batchUpdate(@Param("list") List<StoreInfoEntity> list);
}