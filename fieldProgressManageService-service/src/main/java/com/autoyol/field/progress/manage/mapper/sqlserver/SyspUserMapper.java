package com.autoyol.field.progress.manage.mapper.sqlserver;

import com.autoyol.field.progress.manage.entity.sqlserver.SyspUserEntity;

import java.util.List;

public interface SyspUserMapper {
    SyspUserEntity selectByPrimaryKey(Long syspUseId);

    List<SyspUserEntity> selectSupplierUser();
}