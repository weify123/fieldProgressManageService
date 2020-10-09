package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class PickBackScheduleInfoVo implements Serializable {
    private static final long serialVersionUID = -5038900318553244350L;

    @AutoDocProperty(value = "取还车订单id")
    private String pickDeliverOrderNo;

    @AutoDocProperty(value = "是否供应商派单;字典:若无其他区别，暂时使用[publish_type]")
    private Integer isSupplierDistributeKey;

    @AutoDocProperty(value = "是否供应商派单")
    private String isSupplierDistributeVal;

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

    @AutoDocProperty(value = "是否安排自有人员")
    private String isOwnUserVal;

    @AutoDocProperty(value = "派单类型,字典[dispatch_type]")
    private Integer dispatchTypeKey;

    @AutoDocProperty(value = "派单类型")
    private String dispatchTypeVal;

    @AutoDocProperty(value = "航班号")
    private String flightNo;

    private String createOp;

    private Date createTime;

    private String updateOp;

    private Date updateTime;

    @AutoDocProperty(value = "调度备注")
    private String scheduleMemo;

    @AutoDocProperty(value = "编辑状态:0:未提交1,已提交")
    private Integer editStatus;

    public String getPickDeliverOrderNo() {
        return pickDeliverOrderNo;
    }

    public void setPickDeliverOrderNo(String pickDeliverOrderNo) {
        this.pickDeliverOrderNo = pickDeliverOrderNo;
    }

    public Integer getIsSupplierDistributeKey() {
        return isSupplierDistributeKey;
    }

    public void setIsSupplierDistributeKey(Integer isSupplierDistributeKey) {
        this.isSupplierDistributeKey = isSupplierDistributeKey;
    }

    public String getIsSupplierDistributeVal() {
        return isSupplierDistributeVal;
    }

    public void setIsSupplierDistributeVal(String isSupplierDistributeVal) {
        this.isSupplierDistributeVal = isSupplierDistributeVal;
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

    public String getIsOwnUserVal() {
        return isOwnUserVal;
    }

    public void setIsOwnUserVal(String isOwnUserVal) {
        this.isOwnUserVal = isOwnUserVal;
    }

    public Integer getDispatchTypeKey() {
        return dispatchTypeKey;
    }

    public void setDispatchTypeKey(Integer dispatchTypeKey) {
        this.dispatchTypeKey = dispatchTypeKey;
    }

    public String getDispatchTypeVal() {
        return dispatchTypeVal;
    }

    public void setDispatchTypeVal(String dispatchTypeVal) {
        this.dispatchTypeVal = dispatchTypeVal;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getCreateOp() {
        return createOp;
    }

    public void setCreateOp(String createOp) {
        this.createOp = createOp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateOp() {
        return updateOp;
    }

    public void setUpdateOp(String updateOp) {
        this.updateOp = updateOp;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getScheduleMemo() {
        return scheduleMemo;
    }

    public void setScheduleMemo(String scheduleMemo) {
        this.scheduleMemo = scheduleMemo;
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
