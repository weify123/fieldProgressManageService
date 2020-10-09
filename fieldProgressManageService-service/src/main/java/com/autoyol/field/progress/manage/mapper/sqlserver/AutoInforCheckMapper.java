package com.autoyol.field.progress.manage.mapper.sqlserver;

import com.autoyol.field.progress.manage.entity.sqlserver.AutoInforCheckEntity;

public interface AutoInforCheckMapper {

    AutoInforCheckEntity selectByPrimaryKey(Long autoCheckId);
}