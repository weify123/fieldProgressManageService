package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.SysDictEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDictEntityMapper {

    List<SysDictEntity> selectByTypeName(@Param("typeName") String typeName);

    List<SysDictEntity> selectByTypeNameStr(@Param("nameList") List<String> nameList);

    List<SysDictEntity> selectAll();

    int insert(SysDictEntity record);

    int insertSelective(SysDictEntity record);
}