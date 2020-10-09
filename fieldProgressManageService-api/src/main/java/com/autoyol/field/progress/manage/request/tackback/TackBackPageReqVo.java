package com.autoyol.field.progress.manage.request.tackback;

import com.autoyol.doc.annotation.AutoDocIgnoreProperty;
import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class TackBackPageReqVo extends BasePage {
    private static final long serialVersionUID = 5717213798151592122L;

    @AutoDocProperty(value = "取送车订单号")
    private String pickDeliverOrderNo;

    @AutoDocProperty(value = "订单编号")
    private String transOrderNo;

    @AutoDocProperty(value = "服务分类;字典[flow_server_type]")
    private Integer serverTypeKey;

    @AutoDocProperty(value = "取车时间年,格式yyyy", required = true)
    @NotBlank(message = "取车时间年不能为空")
    private String pickTimeYear;

    @AutoDocProperty(value = "取车时间月日,格式MMdd")
    private String pickTimeMonthDay;

    @AutoDocProperty(value = "调度状态,字典[pick_deliver_schedule_status]")
    private Integer scheduleStatusKey;

    @AutoDocProperty(value = "取送车人员姓名")
    private String userName;

    @AutoDocProperty(value = "取送车人员电话")
    private String userPhone;

    @AutoDocProperty(value = "车牌")
    private String vehicleNo;

    @AutoDocProperty(value = "车辆类型;字典[vehicle_type]")
    private Integer vehicleTypeKey;

    @AutoDocProperty(value = "交易来源,字典[trans_source]")
    private Integer transSourceKey;

    @AutoDocProperty(value = "订单来源场景,字典[scene_source]")
    private Integer sceneSourceKey;

    @AutoDocProperty(value = "线下订单类型,字典[offline_order_type]")
    private Integer offlineOrderTypeKey;

    @AutoDocProperty(value = "是否上传录音;字典:若无其他区别，暂时使用[publish_type]")
    private Integer isUploadRecord;

    @AutoDocProperty("城市名列表([,]分割)")
    private String cityNameStr;

    @JsonIgnore
    @AutoDocIgnoreProperty
    private Date pickTime;

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

    public String getPickTimeYear() {
        return pickTimeYear;
    }

    public void setPickTimeYear(String pickTimeYear) {
        this.pickTimeYear = pickTimeYear;
    }

    public String getPickTimeMonthDay() {
        return pickTimeMonthDay;
    }

    public void setPickTimeMonthDay(String pickTimeMonthDay) {
        this.pickTimeMonthDay = pickTimeMonthDay;
    }

    public Integer getScheduleStatusKey() {
        return scheduleStatusKey;
    }

    public void setScheduleStatusKey(Integer scheduleStatusKey) {
        this.scheduleStatusKey = scheduleStatusKey;
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

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public Integer getVehicleTypeKey() {
        return vehicleTypeKey;
    }

    public void setVehicleTypeKey(Integer vehicleTypeKey) {
        this.vehicleTypeKey = vehicleTypeKey;
    }

    public Integer getTransSourceKey() {
        return transSourceKey;
    }

    public void setTransSourceKey(Integer transSourceKey) {
        this.transSourceKey = transSourceKey;
    }

    public Integer getSceneSourceKey() {
        return sceneSourceKey;
    }

    public void setSceneSourceKey(Integer sceneSourceKey) {
        this.sceneSourceKey = sceneSourceKey;
    }

    public Integer getOfflineOrderTypeKey() {
        return offlineOrderTypeKey;
    }

    public void setOfflineOrderTypeKey(Integer offlineOrderTypeKey) {
        this.offlineOrderTypeKey = offlineOrderTypeKey;
    }

    public Integer getIsUploadRecord() {
        return isUploadRecord;
    }

    public void setIsUploadRecord(Integer isUploadRecord) {
        this.isUploadRecord = isUploadRecord;
    }

    public String getCityNameStr() {
        return cityNameStr;
    }

    public void setCityNameStr(String cityNameStr) {
        this.cityNameStr = cityNameStr;
    }

    public Date getPickTime() {
        return pickTime;
    }

    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
