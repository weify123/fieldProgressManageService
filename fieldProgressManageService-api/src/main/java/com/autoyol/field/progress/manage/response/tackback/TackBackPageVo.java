package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TackBackPageVo implements Serializable {
    private static final long serialVersionUID = -2426825002238611536L;

    @AutoDocProperty(value = "主键id")
    private Long id;

    @AutoDocProperty(value = "取送车订单号")
    private String pickDeliverOrderNo;

    @AutoDocProperty(value = "订单编号")
    private String transOrderNo;

    @AutoDocProperty(value = "服务分类;字典[flow_server_type]")
    private Integer serverTypeKey;

    @AutoDocProperty(value = "服务分类")
    private String serverTypeVal;

    @AutoDocProperty(value = "是否补单;字典:若无其他区别，暂时使用[publish_type]")
    private Integer isSupplyKey;

    @AutoDocProperty(value = "是否补单")
    private String isSupplyVal;

    @AutoDocProperty(value = "所属城市")
    private String belongCity;

    @AutoDocProperty(value = "取车时间")
    private Date pickTime;

    @AutoDocProperty(value = "车辆类型;字典[vehicle_type]")
    private Integer vehicleTypeKey;

    @AutoDocProperty(value = "车辆类型")
    private String vehicleTypeVal;

    @AutoDocProperty(value = "线下订单类型,字典[offline_order_type]")
    private Integer offlineOrderTypeKey;

    @AutoDocProperty(value = "线下订单类型")
    private String offlineOrderTypeVal;

    @AutoDocProperty(value = "合作方")
    private String partner;

    @AutoDocProperty(value = "车牌")
    private String vehicleNo;

    @AutoDocProperty(value = "车型")
    private String vehicleModel;

    @AutoDocProperty(value = "取车地址")
    private String pickAddr;

    @AutoDocProperty(value = "送达地址")
    private String deliverAddr;

    @AutoDocProperty(value = "车主姓名")
    private String ownerName;

    @AutoDocProperty(value = "租客姓名")
    private String renterName;

    @AutoDocProperty(value = "会员标识")
    private String memFlag;

    @AutoDocProperty(value = "服务公司")
    private String serverCompany;

    @AutoDocProperty(value = "取送车人员")
    private String tackBackUserName;

    @AutoDocProperty(value = "是否安排自有人员:字典0:特殊自有,1:GPS自有[is_own_user]")
    private Integer isOwnUserKey;

    @AutoDocProperty(value = "是否安排自有人员")
    private String isOwnUserVal;

    @AutoDocProperty(value = "调度状态,字典[pick_deliver_schedule_status]")
    private Integer scheduleStatusKey;

    @AutoDocProperty(value = "调度状态")
    private String scheduleStatusVal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPickDeliverOrderNo() {
        return pickDeliverOrderNo;
    }

    public void setPickDeliverOrderNo(String pickDeliverOrderNo) {
        this.pickDeliverOrderNo = pickDeliverOrderNo;
    }

    public String getTransOrderNo() {
        return transOrderNo;
    }

    public void setTransOrderNo(String transOrderNo) {
        this.transOrderNo = transOrderNo;
    }

    public Integer getServerTypeKey() {
        return serverTypeKey;
    }

    public void setServerTypeKey(Integer serverTypeKey) {
        this.serverTypeKey = serverTypeKey;
    }

    public String getServerTypeVal() {
        return serverTypeVal;
    }

    public void setServerTypeVal(String serverTypeVal) {
        this.serverTypeVal = serverTypeVal;
    }

    public Integer getIsSupplyKey() {
        return isSupplyKey;
    }

    public void setIsSupplyKey(Integer isSupplyKey) {
        this.isSupplyKey = isSupplyKey;
    }

    public String getIsSupplyVal() {
        return isSupplyVal;
    }

    public void setIsSupplyVal(String isSupplyVal) {
        this.isSupplyVal = isSupplyVal;
    }

    public String getBelongCity() {
        return belongCity;
    }

    public void setBelongCity(String belongCity) {
        this.belongCity = belongCity;
    }

    public Date getPickTime() {
        return pickTime;
    }

    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
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

    public Integer getOfflineOrderTypeKey() {
        return offlineOrderTypeKey;
    }

    public void setOfflineOrderTypeKey(Integer offlineOrderTypeKey) {
        this.offlineOrderTypeKey = offlineOrderTypeKey;
    }

    public String getOfflineOrderTypeVal() {
        return offlineOrderTypeVal;
    }

    public void setOfflineOrderTypeVal(String offlineOrderTypeVal) {
        this.offlineOrderTypeVal = offlineOrderTypeVal;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getPickAddr() {
        return pickAddr;
    }

    public void setPickAddr(String pickAddr) {
        this.pickAddr = pickAddr;
    }

    public String getDeliverAddr() {
        return deliverAddr;
    }

    public void setDeliverAddr(String deliverAddr) {
        this.deliverAddr = deliverAddr;
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

    public String getServerCompany() {
        return serverCompany;
    }

    public void setServerCompany(String serverCompany) {
        this.serverCompany = serverCompany;
    }

    public String getTackBackUserName() {
        return tackBackUserName;
    }

    public void setTackBackUserName(String tackBackUserName) {
        this.tackBackUserName = tackBackUserName;
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

    public Integer getScheduleStatusKey() {
        return scheduleStatusKey;
    }

    public void setScheduleStatusKey(Integer scheduleStatusKey) {
        this.scheduleStatusKey = scheduleStatusKey;
    }

    public String getScheduleStatusVal() {
        return scheduleStatusVal;
    }

    public void setScheduleStatusVal(String scheduleStatusVal) {
        this.scheduleStatusVal = scheduleStatusVal;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
