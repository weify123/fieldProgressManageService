package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.MsgTemplateEntity;

public interface MsgTemplateMapper {

    MsgTemplateEntity selectByCond(MsgTemplateEntity record);

    int insert(MsgTemplateEntity record);

    int insertSelective(MsgTemplateEntity record);
}