package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.MsgTemplateLogEntity;

import java.util.List;

public interface MsgTemplateLogMapper {

    List<MsgTemplateLogEntity> selectMailSending();

    int updateById(MsgTemplateLogEntity record);

    int insert(MsgTemplateLogEntity record);

    int insertSelective(MsgTemplateLogEntity record);
}