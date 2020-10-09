package com.autoyol.field.progress.manage.request.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

public class TackBackScheduleInfoReqVo extends BaseTackReqVo {
    private static final long serialVersionUID = 4411537101853457431L;

    @AutoDocProperty(value = "是否供应商派单;字典:若无其他区别，暂时使用[publish_type]")
    private Integer isSupplierDistributeKey;

    @AutoDocProperty(value = "供应商公司id")
    private Integer supplierCompanyId;

    @AutoDocProperty(value = "供应商公司名")
    private String supplierCompanyName;

    @AutoDocProperty(value = "取送车人员id")
    private Integer userId;

    @AutoDocProperty(value = "取送车人员姓名")
    private String userName;

    @AutoDocProperty(value = "取送车人员电话")
    private String userPhone;

    @AutoDocProperty(value = "是否安排自有人员:字典0:特殊自有,1:GPS自有[is_own_user]")
    private Integer isOwnUserKey;

    @AutoDocProperty(value = "派单类型,字典[dispatch_type]")
    private Integer dispatchTypeKey;

    @AutoDocProperty(value = "调度备注")
    private String scheduleMemo;

    @AutoDocProperty(value = "航班号")
    private String flightNo;

    @AutoDocProperty(value = "编辑状态:0:未提交,1:已提交", required = true)
    @NotNull(message = "编辑状态不能为空")
    private Integer editStatus;

    public Integer getIsSupplierDistributeKey() {
        return isSupplierDistributeKey;
    }

    public void setIsSupplierDistributeKey(Integer isSupplierDistributeKey) {
        this.isSupplierDistributeKey = isSupplierDistributeKey;
    }

    public Integer getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(Integer supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public String getSupplierCompanyName() {
        return supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getIsOwnUserKey() {
        return isOwnUserKey;
    }

    public void setIsOwnUserKey(Integer isOwnUserKey) {
        this.isOwnUserKey = isOwnUserKey;
    }

    public Integer getDispatchTypeKey() {
        return dispatchTypeKey;
    }

    public void setDispatchTypeKey(Integer dispatchTypeKey) {
        this.dispatchTypeKey = dispatchTypeKey;
    }

    public String getScheduleMemo() {
        return scheduleMemo;
    }

    public void setScheduleMemo(String scheduleMemo) {
        this.scheduleMemo = scheduleMemo;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public Integer getEditStatus() {
        return editStatus;
    }

    public void setEditStatus(Integer editStatus) {
        this.editStatus = editStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
