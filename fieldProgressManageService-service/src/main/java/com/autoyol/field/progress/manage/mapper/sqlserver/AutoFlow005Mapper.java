package com.autoyol.field.progress.manage.mapper.sqlserver;

import com.autoyol.field.progress.manage.entity.sqlserver.AutoFlow005Entity;

import java.util.List;

public interface AutoFlow005Mapper {
    AutoFlow005Entity selectByPrimaryKey(Long autoFw05Id);

    List<AutoFlow005Entity> selectByCond(AutoFlow005Entity entity);
}