package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.AttendanceEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttendanceMapper {

    List<AttendanceEntity> selectByUserId(@Param("userId") Integer userId);

    List<AttendanceEntity> selectTodayByUserId(@Param("userId") Integer userId);

    AttendanceEntity selectById(@Param("id") Integer id);

    int updateMemoById(AttendanceEntity record);

    int insert(AttendanceEntity record);

    int insertSelective(AttendanceEntity record);

    int batchInsert(@Param("list") List<AttendanceEntity> list);

    int batchUpdate(@Param("list") List<AttendanceEntity> list);
}