package com.autoyol.field.progress.manage.response.trans;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransVehicleInfoRespVo implements Serializable {
    private static final long serialVersionUID = -4798735477255395889L;

    @AutoDocProperty(value = "订单号")
    private String orderNo;

    @AutoDocProperty(value = "车辆类型;字典[vehicle_type]")
    private Integer vehicleTypeKey;

    @AutoDocProperty(value = "车辆类型")
    private String vehicleTypeVal;

    @AutoDocProperty(value = "代管车管理员")
    private String escrowAdmin;

    @AutoDocProperty(value = "代管车管理员手机号")
    private String escrowAdminPhone;

    @AutoDocProperty(value = "车型")
    private String vehicleModel;

    @AutoDocProperty(value = "车牌")
    private String vehicleNo;

    @AutoDocProperty(value = "油箱容量")
    private String tankCapacity;

    @AutoDocProperty(value = "机油容量")
    private String oilCapacity;

    @AutoDocProperty(value = "排量")
    private String displacement;

    @AutoDocProperty(value = "油量刻度分母")
    private String oilScaleDenominator;

    @AutoDocProperty(value = "油品型号;字典[engine_type]")
    private Integer engineTypeKey;

    @AutoDocProperty(value = "油品型号")
    private String engineTypeVal;

    @AutoDocProperty(value = "油费单价")
    private BigDecimal oilPrice;

    @AutoDocProperty(value = "油耗单价")
    private String oilUnitPrice;

    @AutoDocProperty(value = "日限里程")
    private String dayMileage;

    @AutoDocProperty(value = "日租金指导价")
    private BigDecimal guideDayPrice;

    @AutoDocProperty(value = "最新检测状态;字典[detect_status]")
    private Integer detectStatusKey;

    @AutoDocProperty(value = "最新检测状态")
    private String detectStatusVal;

    @AutoDocProperty(value = "车主备注")
    private String takeNote;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getVehicleTypeKey() {
        return vehicleTypeKey;
    }

    public void setVehicleTypeKey(Integer vehicleTypeKey) {
        this.vehicleTypeKey = vehicleTypeKey;
    }

    public String getVehicleTypeVal() {
        return vehicleTypeVal;
    }

    public void setVehicleTypeVal(String vehicleTypeVal) {
        this.vehicleTypeVal = vehicleTypeVal;
    }

    public String getEscrowAdmin() {
        return escrowAdmin;
    }

    public void setEscrowAdmin(String escrowAdmin) {
        this.escrowAdmin = escrowAdmin;
    }

    public String getEscrowAdminPhone() {
        return escrowAdminPhone;
    }

    public void setEscrowAdminPhone(String escrowAdminPhone) {
        this.escrowAdminPhone = escrowAdminPhone;
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

    public String getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(String tankCapacity) {
        this.tankCapacity = tankCapacity;
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
        this.displacement = displacement;
    }

    public String getOilScaleDenominator() {
        return oilScaleDenominator;
    }

    public void setOilScaleDenominator(String oilScaleDenominator) {
        this.oilScaleDenominator = oilScaleDenominator;
    }

    public Integer getEngineTypeKey() {
        return engineTypeKey;
    }

    public void setEngineTypeKey(Integer engineTypeKey) {
        this.engineTypeKey = engineTypeKey;
    }

    public String getEngineTypeVal() {
        return engineTypeVal;
    }

    public void setEngineTypeVal(String engineTypeVal) {
        this.engineTypeVal = engineTypeVal;
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
        this.oilUnitPrice = oilUnitPrice;
    }

    public String getDayMileage() {
        return dayMileage;
    }

    public void setDayMileage(String dayMileage) {
        this.dayMileage = dayMileage;
    }

    public BigDecimal getGuideDayPrice() {
        return guideDayPrice;
    }

    public void setGuideDayPrice(BigDecimal guideDayPrice) {
        this.guideDayPrice = guideDayPrice;
    }

    public Integer getDetectStatusKey() {
        return detectStatusKey;
    }

    public void setDetectStatusKey(Integer detectStatusKey) {
        this.detectStatusKey = detectStatusKey;
    }

    public String getDetectStatusVal() {
        return detectStatusVal;
    }

    public void setDetectStatusVal(String detectStatusVal) {
        this.detectStatusVal = detectStatusVal;
    }

    public String getTakeNote() {
        return takeNote;
    }

    public void setTakeNote(String takeNote) {
        this.takeNote = takeNote;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
