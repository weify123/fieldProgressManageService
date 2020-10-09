package com.autoyol.field.progress.manage.request.user;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class BaseAppUserReqVo implements Serializable {
    private static final long serialVersionUID = 3104368205810744462L;

    /**
     * 人员id
     */
    @AutoDocProperty(value = "人员id", required = true)
    @NotNull(message = "userId不能为空")
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
