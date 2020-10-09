package com.autoyol.field.progress.manage.entity;

import java.util.Date;

/**
 *
 *
 * @author feiyu.wei
 * @date 2020/02/25
 */
public class PickDeliverScheduleInfoLogEntity {
    /**
     *
     */
    private Integer id;

    /**
     * 取送车订单号
     */
    private String pickDeliverOrderNo;

    /**
     * 服务分类(3:取车服务,4:还车服务)
     */
    private Integer serviceType;

    /**
     * 调度状态
     */
    private Integer scheduleStatus;

    /**
     * 外勤app状态
     */
    private Integer fieldAppStatus;

    /**
     * 是否供应商派单(0:否,1:是)
     */
    private Integer isSupplierDistribute;

    /**
     * 供应商公司id
     */
    private Integer supplierCompanyId;

    /**
     * 供应商公司名
     */
    private String supplierCompanyName;

    /**
     * 取送车人员id
     */
    private Integer userId;

    /**
     * 取送车人员姓名
     */
    private String userName;

    /**
     * 取送车人员电话
     */
    private String userPhone;

    /**
     * 是否安排自有人员(0:特殊自有,1:GPS自有)
     */
    private Integer isOwnPerson;

    /**
     * 派单类型(0:人工派单,1:系统派单;2:人工干预)
     */
    private Integer distributeType;

    /**
     * 调度备注
     */
    private String flightNo;

    /**
     * 是否删除:0:正常;1,已删除
     */
    private Integer isDelete;

    /**
     *
     */
    private String createOp;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private String updateOp;

    /**
     *
     */
    private Date updateTime;

    /**
     * 调度备注
     */
    private String scheduleMemo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPickDeliverOrderNo() {
        return pickDeliverOrderNo;
    }

    public void setPickDeliverOrderNo(String pickDeliverOrderNo) {
        this.pickDeliverOrderNo = pickDeliverOrderNo == null ? null : pickDeliverOrderNo.trim();
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(Integer scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public Integer getFieldAppStatus() {
        return fieldAppStatus;
    }

    public void setFieldAppStatus(Integer fieldAppStatus) {
        this.fieldAppStatus = fieldAppStatus;
    }

    public Integer getIsSupplierDistribute() {
        return isSupplierDistribute;
    }

    public void setIsSupplierDistribute(Integer isSupplierDistribute) {
        this.isSupplierDistribute = isSupplierDistribute;
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
        this.supplierCompanyName = supplierCompanyName == null ? null : supplierCompanyName.trim();
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
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }

    public Integer getIsOwnPerson() {
        return isOwnPerson;
    }

    public void setIsOwnPerson(Integer isOwnPerson) {
        this.isOwnPerson = isOwnPerson;
    }

    public Integer getDistributeType() {
        return distributeType;
    }

    public void setDistributeType(Integer distributeType) {
        this.distributeType = distributeType;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo == null ? null : flightNo.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateOp() {
        return createOp;
    }

    public void setCreateOp(String createOp) {
        this.createOp = createOp == null ? null : createOp.trim();
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
        this.updateOp = updateOp == null ? null : updateOp.trim();
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
        this.scheduleMemo = scheduleMemo == null ? null : scheduleMemo.trim();
    }
}