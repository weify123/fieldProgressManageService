package com.autoyol.field.progress.manage.entity;

public class PageCondTackBackEntity extends PickDeliverOrderInfoEntity{

    /**
     * 所属城市
     */
    private String belongCity;

    /**
     * 线下订单类型; 1:三方合同订单,2:托管车长租订单,3:旅游产品下单,4太保出险代步车(未满足)
     */
    private Integer offlineOrderType;

    /**
     * 合作方
     */
    private String partner;

    /**
     * 车辆类型
     */
    private Integer vehicleType;

    /**
     * 车型
     */
    private String vehicleModel;

    /**
     * 车牌
     */
    private String vehicleNo;

    /**
     * 取送车人员姓名
     */
    private String userName;

    /**
     * 是否安排自有人员(0:特殊自有,1:GPS自有)
     */
    private Integer isOwnPerson;

    private Integer supplierCompanyId;
    /**
     * 供应商公司名
     */
    private String supplierCompanyName;

    /**
     * 车主姓名
     */
    private String ownerName;

    /**
     * 租客姓名
     */
    private String renterName;

    /**
     * 会员标识
     */
    private String memFlag;

    public String getBelongCity() {
        return belongCity;
    }

    public void setBelongCity(String belongCity) {
        this.belongCity = belongCity;
    }

    public Integer getOfflineOrderType() {
        return offlineOrderType;
    }

    public void setOfflineOrderType(Integer offlineOrderType) {
        this.offlineOrderType = offlineOrderType;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getIsOwnPerson() {
        return isOwnPerson;
    }

    public void setIsOwnPerson(Integer isOwnPerson) {
        this.isOwnPerson = isOwnPerson;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterName(String renterName) {
        this.renterName = renterName;
    }

    public String getMemFlag() {
        return memFlag;
    }

    public void setMemFlag(String memFlag) {
        this.memFlag = memFlag;
    }
}
