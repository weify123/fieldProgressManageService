package com.autoyol.field.progress.manage.request.user;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BaseUserQueryVo extends BasePage {
    private static final long serialVersionUID = 7503480117262928L;

    @AutoDocProperty("公司id")
    private Integer companyId;

    @AutoDocProperty("城市id列表,多个使用[,]分隔")
    private String cityIdStr;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCityIdStr() {
        return cityIdStr;
    }

    public void setCityIdStr(String cityIdStr) {
        this.cityIdStr = cityIdStr;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
