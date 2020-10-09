package com.autoyol.field.progress.manage.response.supplier;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.constraint.ExcelElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class SupplierVo implements Serializable {
    private static final long serialVersionUID = 8586189067954416090L;

    @AutoDocProperty("id")
    private Integer id;

    @AutoDocProperty("用户名")
    @ExcelElement(field = "用户名", index = 1)
    private String userName;

    @AutoDocProperty("服务公司id")
    private Integer companyId;

    @AutoDocProperty(value = "公司一级分类")
    private String firstCategory;

    @AutoDocProperty(value = "公司二级分类")
    @ExcelElement(field = "服务公司", index = 2)
    private String secondCategory;

    @AutoDocProperty("接收通知邮箱")
    @ExcelElement(field = "邮箱", index = 3)
    private String email;

    @AutoDocProperty(value = "管理城市id【、】分割")
    private String manageCityIdStr;

    @AutoDocProperty(value = "管理城市")
    @ExcelElement(field = "城市", index = 4)
    private String mannageCity;

    @AutoDocProperty("账户状态：0启用、1停用;字典类型名[user_status]")
    private Integer statusKey;

    @AutoDocProperty("账户状态：0启用、1停用;字典类型名[user_status]")
    @ExcelElement(field = "是否启用", index = 5)
    private String statusVal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getFirstCategory() {
        return firstCategory;
    }

    public void setFirstCategory(String firstCategory) {
        this.firstCategory = firstCategory;
    }

    public String getSecondCategory() {
        return secondCategory;
    }

    public void setSecondCategory(String secondCategory) {
        this.secondCategory = secondCategory;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getManageCityIdStr() {
        return manageCityIdStr;
    }

    public void setManageCityIdStr(String manageCityIdStr) {
        this.manageCityIdStr = manageCityIdStr;
    }

    public String getMannageCity() {
        return mannageCity;
    }

    public void setMannageCity(String mannageCity) {
        this.mannageCity = mannageCity;
    }

    public Integer getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(Integer statusKey) {
        this.statusKey = statusKey;
    }

    public String getStatusVal() {
        return statusVal;
    }

    public void setStatusVal(String statusVal) {
        this.statusVal = statusVal;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
