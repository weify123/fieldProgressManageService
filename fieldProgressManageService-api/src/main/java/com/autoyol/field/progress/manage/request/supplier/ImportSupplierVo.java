package com.autoyol.field.progress.manage.request.supplier;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import com.autoyol.field.progress.manage.util.Email;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class ImportSupplierVo implements Serializable {
    private static final long serialVersionUID = 3729557854224361376L;

    private String userId;

    private Integer companyId;

    private Integer statusKey;

    @AutoDocProperty("用户名")
    private String userName;

    @AutoDocProperty("服务公司")
    private String companyName;

    @AutoDocProperty("接收通知邮箱")
    private String email;

    @AutoDocProperty(value = "管理城市")
    private String mannageCity;

    @AutoDocProperty("是否启用：0启用、1停用;字典类型名[user_status]")
    private String statusVal;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMannageCity() {
        return mannageCity;
    }

    public void setMannageCity(String mannageCity) {
        this.mannageCity = mannageCity;
    }

    public String getStatusVal() {
        return statusVal;
    }

    public void setStatusVal(String statusVal) {
        this.statusVal = statusVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImportSupplierVo that = (ImportSupplierVo) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(companyName, that.companyName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(mannageCity, that.mannageCity) &&
                Objects.equals(statusVal, that.statusVal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, companyName, email, mannageCity, statusVal);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
