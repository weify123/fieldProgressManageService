package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.AppUserCertEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppUserCertMapper {
    int insert(AppUserCertEntity record);

    int insertSelective(AppUserCertEntity record);

    void deleteByCond(AppUserCertEntity record);

    int deleteSelectById(AppUserCertEntity record);

    List<AppUserCertEntity> findCertByUserId(@Param("userId") Integer userId);

    int findCertNotDeletedByCond(AppUserCertEntity record);
}