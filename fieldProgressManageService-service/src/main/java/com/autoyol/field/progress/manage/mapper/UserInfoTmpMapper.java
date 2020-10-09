package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.UserInfoTmpEntity;
import com.autoyol.field.progress.manage.page.BasePage;

import java.util.List;

public interface UserInfoTmpMapper {
    List<UserInfoTmpEntity> queryList(BasePage basePage);

    int batchUpdate(List<UserInfoTmpEntity> list);

}