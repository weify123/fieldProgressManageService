package com.autoyol.field.progress.manage.mapper.sqlserver;

import com.autoyol.field.progress.manage.entity.sqlserver.AotuInfor005Entity;

import java.util.List;

public interface AotuInfor005Mapper {

    AotuInfor005Entity selectByPrimaryKey(Long autoInf05Id);

    List<AotuInfor005Entity> selectSingleByCond(AotuInfor005Entity entity);

    int selectHisCountByCond(AotuInfor005Entity entity);
    List<AotuInfor005Entity> selectHisListByCond(AotuInfor005Entity entity);

    List<AotuInfor005Entity> selectListByCond(AotuInfor005Entity entity);

    List<AotuInfor005Entity> selectTransByCond(AotuInfor005Entity entity);

    List<AotuInfor005Entity> queryListByCond(AotuInfor005Entity entity);

    int queryCountByCond(AotuInfor005Entity entity);
}