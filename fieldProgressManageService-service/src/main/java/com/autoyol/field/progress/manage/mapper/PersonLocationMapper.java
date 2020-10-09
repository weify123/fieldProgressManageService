package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.PersonLocationEntity;
import org.apache.ibatis.annotations.Param;

public interface PersonLocationMapper {

    PersonLocationEntity queryByUserId(@Param("userId") Integer userId);

    int insert(PersonLocationEntity record);

    int insertSelective(PersonLocationEntity record);
}