package com.autoyol.field.progress.manage.response.attendance;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class AttendanceMemoRespVo implements Serializable {
    private static final long serialVersionUID = 4783982974565897722L;

    @AutoDocProperty("备注")
    private String memo;

    public AttendanceMemoRespVo() {
    }

    public AttendanceMemoRespVo(String memo) {
        this.memo = memo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
