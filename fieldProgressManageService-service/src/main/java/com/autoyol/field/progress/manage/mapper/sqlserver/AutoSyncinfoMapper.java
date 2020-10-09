package com.autoyol.field.progress.manage.mapper.sqlserver;

import com.autoyol.field.progress.manage.entity.sqlserver.AutoSyncinfoEntity;
import org.apache.ibatis.annotations.Param;

public interface AutoSyncinfoMapper {

    AutoSyncinfoEntity selectByMsgId(@Param("msgId") String msgId);
}