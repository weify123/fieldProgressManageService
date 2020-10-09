package com.autoyol.field.progress.manage.request.survey;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

public class SurveyUrlReqVo extends BaseRequest {
    private static final long serialVersionUID = 8639735776576110985L;
    /**
     * 长链信息
     */
    @AutoDocProperty(value = "长链接", required = true)
    @NotBlank(message = "长链接不能为空")
    private String longUrl;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
