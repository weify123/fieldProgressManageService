package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.HideFieldConfEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HideFieldConfMapper {

    List<HideFieldConfEntity> queryByServerType(@Param("serviceType") Integer serviceType);

    int insertSelective(HideFieldConfEntity record);
}