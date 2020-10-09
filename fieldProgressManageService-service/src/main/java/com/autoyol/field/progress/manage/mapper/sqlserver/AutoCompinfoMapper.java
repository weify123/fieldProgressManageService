package com.autoyol.field.progress.manage.mapper.sqlserver;

import com.autoyol.field.progress.manage.entity.sqlserver.AutoCompinfoEntity;

import java.util.List;

public interface AutoCompinfoMapper {
    AutoCompinfoEntity selectByPrimaryKey(Long autoCpinfoId);

    AutoCompinfoEntity queryCompanyBySecondName(String name);

    List<AutoCompinfoEntity> selectAll();

}