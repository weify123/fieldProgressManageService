package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.TakeGiveTemplateEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TakeGiveTemplateMapper {

    List<TakeGiveTemplateEntity> findAll();

    TakeGiveTemplateEntity selectByPrimaryKey(@Param("id") Integer id);

    int insert(TakeGiveTemplateEntity record);

    int insertSelective(TakeGiveTemplateEntity record);

    int updateSelective(TakeGiveTemplateEntity record);
}