package com.autoyol.field.progress.manage.mapper.sqlserver;

import com.autoyol.field.progress.manage.entity.sqlserver.AutoRecordInfoEntity;

import java.util.List;

public interface AutoRecordInfoMapper {

    List<AutoRecordInfoEntity> selectByCond(AutoRecordInfoEntity record);

    AutoRecordInfoEntity selectByPrimaryKey(Long autoRedId);

}