package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.AppUserEntity;
import com.autoyol.field.progress.manage.page.BasePage;
import com.autoyol.field.progress.manage.request.user.AppUserQueryReqVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppUserMapper {

    int insertSelective(AppUserEntity record);

    int updateById(AppUserEntity record);

    int batchCountQuery();

    List<AppUserEntity> selectNotDisabledByCond(AppUserEntity record);

    List<AppUserEntity> batchQueryByCond(BasePage basePage);

    List<AppUserEntity> queryAppUserByCond(AppUserEntity record);

    int countAppUserPageByCond(AppUserQueryReqVo record);

    List<AppUserEntity> queryAppUserPageByCond(AppUserQueryReqVo record);

    int countByCond(AppUserEntity record);

    AppUserEntity selectByPrimaryKey(@Param("id") Integer id);
}