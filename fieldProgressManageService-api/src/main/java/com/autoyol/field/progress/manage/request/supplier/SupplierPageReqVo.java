package com.autoyol.field.progress.manage.request.supplier;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SupplierPageReqVo extends BasePage {
    private static final long serialVersionUID = -2124467229311180954L;

    @AutoDocProperty("用户名")
    private String userName;

    @AutoDocProperty("城市id")
    private Integer cityId;

    @AutoDocProperty("公司id")
    private Integer companyId;

    @AutoDocProperty("账户状态：0启用、1停用;字典类型名[user_status]")
    private Integer statusKey;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(Integer statusKey) {
        this.statusKey = statusKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
