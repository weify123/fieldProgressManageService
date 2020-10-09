package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TackPickInfoVo implements Serializable {
    private static final long serialVersionUID = -8423232859614948623L;

    @AutoDocProperty(value = "取送车订单号")
    private String pickDeliverOrderNo;

    @AutoDocProperty(value = "实际取车时间")
    private Date realPickTime;

    @AutoDocProperty(value = "取车油量")
    private BigDecimal pickOil;

    @AutoDocProperty(value = "预计取车公里数")
    private BigDecimal estimatePickKilo;

    @AutoDocProperty(value = "取车公里数")
    private Integer pickKilo;

    @AutoDocProperty(value = "车辆状况;字典[vehicle_status]")
    private Integer vehicleStatusKey;

    @AutoDocProperty(value = "车辆状况")
    private String vehicleStatusVal;

    @AutoDocProperty(value = "变更取车地址")
    private String changePickAddress;

    @AutoDocProperty(value = "变更取车经度")
    private BigDecimal changePickLong;

    @AutoDocProperty(value = "变更取车纬度")
    private BigDecimal changePickLat;

    @AutoDocProperty(value = "编辑状态:0:未提交1,已提交")
    private Integer editStatus;

    public String getPickDeliverOrderNo() {
        return pickDeliverOrderNo;
    }

    public void setPickDeliverOrderNo(String pickDeliverOrderNo) {
        this.pickDeliverOrderNo = pickDeliverOrderNo;
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

    public Integer getVehicleStatusKey() {
        return vehicleStatusKey;
    }

    public void setVehicleStatusKey(Integer vehicleStatusKey) {
        this.vehicleStatusKey = vehicleStatusKey;
    }

    public String getVehicleStatusVal() {
        return vehicleStatusVal;
    }

    public void setVehicleStatusVal(String vehicleStatusVal) {
        this.vehicleStatusVal = vehicleStatusVal;
    }

    public String getChangePickAddress() {
        return changePickAddress;
    }

    public void setChangePickAddress(String changePickAddress) {
        this.changePickAddress = changePickAddress;
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
