package com.autoyol.field.progress.manage.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CarServiceItemEntity {
    /**
    * 服务id
    */
    private Integer serviceId;

    /**
    * 服务分类：1保养 2美容 3养护 4清洗套餐 5年检 6车辆检测 7维修
    */
    private Integer serviceType;

    /**
    * 服务产品名称
    */
    private String serviceProductName;

    /**
    * 门店挂牌价格
    */
    private BigDecimal storePrice;

    /**
    * 凹凸销售价格
    */
    private BigDecimal aotuPrice;

    /**
    * 服务时长
    */
    private String serviceTime;

    /**
    * 适用的车型
    */
    private String applicableModel;

    /**
    * 是否有效
    */
    private Integer isEffective;

    /**
    * 是否删除:0:正常;1,已删除
    */
    private Integer isDelete;

    /**
    * 创建人
    */
    private String createOp;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新人
    */
    private String updateOp;

    /**
    * 更新时间
    */
    private Date updateTime;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceProductName() {
        return serviceProductName;
    }

    public void setServiceProductName(String serviceProductName) {
        this.serviceProductName = serviceProductName;
    }

    public BigDecimal getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(BigDecimal storePrice) {
        this.storePrice = storePrice;
    }

    public BigDecimal getAotuPrice() {
        return aotuPrice;
    }

    public void setAotuPrice(BigDecimal aotuPrice) {
        this.aotuPrice = aotuPrice;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getApplicableModel() {
        return applicableModel;
    }

    public void setApplicableModel(String applicableModel) {
        this.applicableModel = applicableModel;
    }

    public Integer getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(Integer isEffective) {
        this.isEffective = isEffective;
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
}