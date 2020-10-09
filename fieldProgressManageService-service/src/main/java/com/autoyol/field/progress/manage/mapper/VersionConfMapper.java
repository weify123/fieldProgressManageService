package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.VersionConfEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VersionConfMapper {

    List<VersionConfEntity> findByCond(VersionConfEntity record);

    VersionConfEntity selectById(@Param("id") Integer id);

    int countByCond(VersionConfEntity record);

    int updateById(VersionConfEntity record);

    int insert(VersionConfEntity record);

    int insertSelective(VersionConfEntity record);
}