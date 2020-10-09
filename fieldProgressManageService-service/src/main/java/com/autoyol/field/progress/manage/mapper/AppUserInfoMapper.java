package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.AppUserInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppUserInfoMapper {

    int insertSelective(AppUserInfoEntity record);

    int batchInsert(@Param("list") List<AppUserInfoEntity> list);

    int countObjByCond(AppUserInfoEntity entity);

    List<AppUserInfoEntity> queryPageObjByCond(AppUserInfoEntity entity);

    AppUserInfoEntity selectByUserId(@Param("userId") Integer userId);

    List<AppUserInfoEntity> queryAllMobileAndIdNo();

    List<AppUserInfoEntity> queryExistUserId(@Param("list") List<Integer> list);

    int updateSelectById(AppUserInfoEntity record);

    int batchUpdate(@Param("list") List<AppUserInfoEntity> list);


}