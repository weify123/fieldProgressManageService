package com.autoyol.field.progress.manage.mapper.sqlserver;

import com.autoyol.field.progress.manage.entity.sqlserver.AutoOrdtraposinfoEntity;

public interface AutoOrdtraposinfoMapper {

    AutoOrdtraposinfoEntity selectByPrimaryKey(Long autoOtpId);

    String selectCurByCond(Long autoOtpFldtid);

}