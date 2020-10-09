package com.autoyol.field.progress.manage.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2020/02/25
 */
public class PickDeliverPickInfoLogEntity {
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
     * 实际取车时间
     */
    private Date realPickTime;

    /**
     * 取车油量
     */
    private BigDecimal pickOil;

    /**
     * 预计取车公里数
     */
    private BigDecimal estimatePickKilo;

    /**
     * 取车公里数
     */
    private Integer pickKilo;

    /**
     * 车辆状况;0、有新损；1、无新损
     */
    private Integer vehicleStatus;

    /**
     * 变更取车地址
     */
    private String changePickAddress;

    /**
     * 变更取车经度
     */
    private BigDecimal changePickLong;

    /**
     * 变更取车纬度
     */
    private BigDecimal changePickLat;

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

    public Date getRealPickTime() {
        return realPickTime;
    }

    public void setRealPickTime(Date realPickTime) {
        this.realPickTime = realPickTime;
    }

    public BigDecimal getPickOil() {
        return pickOil;
    }

    public void setPickOil(BigDecimal pickOil) {
        this.pickOil = pickOil;
    }

    public BigDecimal getEstimatePickKilo() {
        return estimatePickKilo;
    }

    public void setEstimatePickKilo(BigDecimal estimatePickKilo) {
        this.estimatePickKilo = estimatePickKilo;
    }

    public Integer getPickKilo() {
        return pickKilo;
    }

    public void setPickKilo(Integer pickKilo) {
        this.pickKilo = pickKilo;
    }

    public Integer getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(Integer vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getChangePickAddress() {
        return changePickAddress;
    }

    public void setChangePickAddress(String changePickAddress) {
        this.changePickAddress = changePickAddress == null ? null : changePickAddress.trim();
    }

    public BigDecimal getChangePickLong() {
        return changePickLong;
    }

    public void setChangePickLong(BigDecimal changePickLong) {
        this.changePickLong = changePickLong;
    }

    public BigDecimal getChangePickLat() {
        return changePickLat;
    }

    public void setChangePickLat(BigDecimal changePickLat) {
        this.changePickLat = changePickLat;
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
}