package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.CityLevelEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CityLevelMapper {
    int insert(CityLevelEntity record);

    int insertSelective(CityLevelEntity record);

    Integer updateCityById(CityLevelEntity record);

    CityLevelEntity selectByPrimaryKey(@Param("cityId") Integer cityId);

    List<CityLevelEntity> selectByCityIdList(@Param("cityIdList") List<Integer> cityIdList);

    List<CityLevelEntity> selectAll();

    Integer getCityIdByName(@Param("city") String city);

    int batchUpdate(@Param("list") List<CityLevelEntity> list);
}