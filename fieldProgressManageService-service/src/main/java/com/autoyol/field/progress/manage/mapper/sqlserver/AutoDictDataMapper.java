package com.autoyol.field.progress.manage.mapper.sqlserver;

import com.autoyol.field.progress.manage.entity.sqlserver.AutoDictDataEntity;

import java.util.List;

public interface AutoDictDataMapper {

    List<AutoDictDataEntity> selectByCond(AutoDictDataEntity entity);

    AutoDictDataEntity selectDictByCond(AutoDictDataEntity entity);
}
