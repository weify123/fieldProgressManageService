package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.AttrLabelEntity;

import java.util.List;

public interface AttrLabelMapper {

    List<AttrLabelEntity> selectAll();

    int insert(AttrLabelEntity record);

    int insertSelective(AttrLabelEntity record);
}