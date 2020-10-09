package com.autoyol.field.progress.manage.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2020/02/25
 */
public class TransVehicleInfoEntity {

    private Integer id;
    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 车辆类型
     */
    private Integer vehicleType;

    /**
     * 代管车管理员
     */
    private String escrowAdmin;

    /**
     * 代管车管理员手机号
     */
    private String escrowAdminPhone;

    /**
     * 车型
     */
    private String vehicleModel;

    /**
     * 车牌
     */
    private String vehicleNo;

    /**
     * 油箱容量
     */
    private String tankCapacity;

    /**
     * 机油容量
     */
    private String oilCapacity;

    /**
     * 排量
     */
    private String displacement;

    /**
     * 油量刻度分母
     */
    private String oilScaleDenominator;

    /**
     * 油品型号(燃料)
     */
    private Integer engineType;

    /**
     * 油费单价
     */
    private BigDecimal oilPrice;

    /**
     * 油耗单价
     */
    private String oilUnitPrice;

    /**
     * 日限里程
     */
    private String dayMileage;

    /**
     * 日租金指导价
     */
    private BigDecimal guideDayPrice;

    /**
     * 最新检测状态:1,未检测;2,检测通过;3,检测通过-建议改善;4,高危车辆-已修复;5,高危车辆-未修复6 .未检测-已报名7 .检测失效
     */
    private Integer detectStatus;

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getEscrowAdmin() {
        return escrowAdmin;
    }

    public void setEscrowAdmin(String escrowAdmin) {
        this.escrowAdmin = escrowAdmin == null ? null : escrowAdmin.trim();
    }

    public String getEscrowAdminPhone() {
        return escrowAdminPhone;
    }

    public void setEscrowAdminPhone(String escrowAdminPhone) {
        this.escrowAdminPhone = escrowAdminPhone == null ? null : escrowAdminPhone.trim();
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel == null ? null : vehicleModel.trim();
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo == null ? null : vehicleNo.trim();
    }

    public String getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(String tankCapacity) {
        this.tankCapacity = tankCapacity == null ? null : tankCapacity.trim();
    }

    public String getOilCapacity() {
        return oilCapacity;
    }

    public void setOilCapacity(String oilCapacity) {
        this.oilCapacity = oilCapacity;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement == null ? null : displacement.trim();
    }

    public String getOilScaleDenominator() {
        return oilScaleDenominator;
    }

    public void setOilScaleDenominator(String oilScaleDenominator) {
        this.oilScaleDenominator = oilScaleDenominator == null ? null : oilScaleDenominator.trim();
    }

    public Integer getEngineType() {
        return engineType;
    }

    public void setEngineType(Integer engineType) {
        this.engineType = engineType;
    }

    public BigDecimal getOilPrice() {
        return oilPrice;
    }

    public void setOilPrice(BigDecimal oilPrice) {
        this.oilPrice = oilPrice;
    }

    public String getOilUnitPrice() {
        return oilUnitPrice;
    }

    public void setOilUnitPrice(String oilUnitPrice) {
        this.oilUnitPrice = oilUnitPrice == null ? null : oilUnitPrice.trim();
    }

    public String getDayMileage() {
        return dayMileage;
    }

    public void setDayMileage(String dayMileage) {
        this.dayMileage = dayMileage == null ? null : dayMileage.trim();
    }

    public BigDecimal getGuideDayPrice() {
        return guideDayPrice;
    }

    public void setGuideDayPrice(BigDecimal guideDayPrice) {
        this.guideDayPrice = guideDayPrice;
    }

    public Integer getDetectStatus() {
        return detectStatus;
    }

    public void setDetectStatus(Integer detectStatus) {
        this.detectStatus = detectStatus;
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