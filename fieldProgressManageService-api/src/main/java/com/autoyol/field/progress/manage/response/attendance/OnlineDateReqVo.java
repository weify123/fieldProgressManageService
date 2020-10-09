package com.autoyol.field.progress.manage.response.attendance;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class OnlineDateReqVo implements Serializable {
    private static final long serialVersionUID = 4218344893310019331L;

    @AutoDocProperty("上线日期")
    private String onlineDate;

    public OnlineDateReqVo() {
    }

    public OnlineDateReqVo(String onlineDate) {
        this.onlineDate = onlineDate;
    }

    public String getOnlineDate() {
        return onlineDate;
    }

    public void setOnlineDate(String onlineDate) {
        this.onlineDate = onlineDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
