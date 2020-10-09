package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.ImportLogEntity;
import com.autoyol.field.progress.manage.entity.ImportLogReqEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImportLogMapper {

    long countByType(@Param("businessType") Integer businessType);

    List<ImportLogEntity> queryPageLogByType(ImportLogReqEntity logReqEntity);

    int insert(ImportLogEntity record);

    int insertSelective(ImportLogEntity record);
}