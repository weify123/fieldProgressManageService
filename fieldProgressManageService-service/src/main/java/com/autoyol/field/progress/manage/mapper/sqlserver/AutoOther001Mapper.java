package com.autoyol.field.progress.manage.mapper.sqlserver;

import com.autoyol.field.progress.manage.entity.sqlserver.AutoOther001Entity;

import java.util.List;

public interface AutoOther001Mapper {
    AutoOther001Entity selectByPrimaryKey(Long autoOt01Id);

    List<AutoOther001Entity> selectByByName(String name);

    int countByCond(AutoOther001Entity entity);

    List<AutoOther001Entity> selectByCond(AutoOther001Entity entity);
}