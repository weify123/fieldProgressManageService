package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PickOrderInfoVo implements Serializable {
    private static final long serialVersionUID = 30967101508140192L;

    @AutoDocProperty(value = "订单号")
    private String orderNo;
    /**
     * 订单类型（0,普通订单 1,代步车订单 2,携程套餐订单 3,携程到店订单 4,同程套餐订单 5,安联代步车订单 6,普通套餐订单 7,VIP订单
     * ）
     **/
    @AutoDocProperty(value = "订单类型")
    private String orderType;

    @AutoDocProperty(value = "取送车订单号")
    private String pickDeliverOrderNo;

    @AutoDocProperty(value = "服务分类;字典[flow_server_type]")
    private Integer serverTypeKey;

    @AutoDocProperty(value = "服务分类")
    private String serverTypeVal;

    @AutoDocProperty(value = "是否补单;字典:若无其他区别，暂时使用[publish_type]")
    private Integer isSupplyKey;

    @AutoDocProperty(value = "是否补单")
    private String isSupplyVal;

    @AutoDocProperty(value = "操作类型;字典:[pick_back_opr_type]")
    private Integer oprTypeKey;

    @AutoDocProperty(value = "操作类型")
    private String oprTypeVal;

    @AutoDocProperty("调度状态Key;字典类型名[pick_deliver_schedule_status]")
    private Integer scheduleStatusKey;

    @AutoDocProperty("调度状态val;")
    private String scheduleStatusVal;

    @AutoDocProperty("调度状态val;字典类型名[flow_node_name_type]")
    private String fieldAppStatusVal;

    @AutoDocProperty(value = "是否直赔;字典[publish_type]若无其他区别，暂时使用")
    private String isDirectCompensateVal;

    @AutoDocProperty(value = "渠道;字典[channel]")
    private String channelVal;

    @AutoDocProperty(value = "服务类型;字典[service_class]")
    private String serviceClassVal;

    @AutoDocProperty(value = "是否免服务费;字典[publish_type]若无其他区别，暂时使用")
    private String isFreeVal;

    @AutoDocProperty(value = "服务费金额")
    private BigDecimal serviceFee;

    @AutoDocProperty(value = "报案号")
    private String reportNo;

    @AutoDocProperty(value = "金钥匙派单人")
    private String goldKey;

    @AutoDocProperty(value = "订单渠道来源;字典[order_channel_source]")
    private String orderChannelSourceVal;

    @AutoDocProperty(value = "预计服务时长")
    private String estimateServiceTime;

    @AutoDocProperty(value = "取车时间")
    private Date pickTime;

    @AutoDocProperty(value = "取车地址")
    private String pickAddr;

    @AutoDocProperty(value = "取车经度")
    private BigDecimal pickLong;

    @AutoDocProperty(value = "取车纬度")
    private BigDecimal pickLat;

    @AutoDocProperty(value = "送达时间")
    private Date deliverTime;

    @AutoDocProperty(value = "所属城市")
    private String belongCity;

    @AutoDocProperty(value = "送达地址")
    private String deliverAddr;

    @AutoDocProperty(value = "送达经度")
    private BigDecimal deliverLong;

    @AutoDocProperty(value = "送达纬度")
    private BigDecimal deliverLat;

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPickDeliverOrderNo() {
        return pickDeliverOrderNo;
    }

    public void setPickDeliverOrderNo(String pickDeliverOrderNo) {
        this.pickDeliverOrderNo = pickDeliverOrderNo;
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

    public Integer getOprTypeKey() {
        return oprTypeKey;
    }

    public void setOprTypeKey(Integer oprTypeKey) {
        this.oprTypeKey = oprTypeKey;
    }

    public String getOprTypeVal() {
        return oprTypeVal;
    }

    public void setOprTypeVal(String oprTypeVal) {
        this.oprTypeVal = oprTypeVal;
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

    public String getFieldAppStatusVal() {
        return fieldAppStatusVal;
    }

    public void setFieldAppStatusVal(String fieldAppStatusVal) {
        this.fieldAppStatusVal = fieldAppStatusVal;
    }

    public String getIsDirectCompensateVal() {
        return isDirectCompensateVal;
    }

    public void setIsDirectCompensateVal(String isDirectCompensateVal) {
        this.isDirectCompensateVal = isDirectCompensateVal;
    }

    public String getChannelVal() {
        return channelVal;
    }

    public void setChannelVal(String channelVal) {
        this.channelVal = channelVal;
    }

    public String getServiceClassVal() {
        return serviceClassVal;
    }

    public void setServiceClassVal(String serviceClassVal) {
        this.serviceClassVal = serviceClassVal;
    }

    public String getIsFreeVal() {
        return isFreeVal;
    }

    public void setIsFreeVal(String isFreeVal) {
        this.isFreeVal = isFreeVal;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getGoldKey() {
        return goldKey;
    }

    public void setGoldKey(String goldKey) {
        this.goldKey = goldKey;
    }

    public String getOrderChannelSourceVal() {
        return orderChannelSourceVal;
    }

    public void setOrderChannelSourceVal(String orderChannelSourceVal) {
        this.orderChannelSourceVal = orderChannelSourceVal;
    }

    public String getEstimateServiceTime() {
        return estimateServiceTime;
    }

    public void setEstimateServiceTime(String estimateServiceTime) {
        this.estimateServiceTime = estimateServiceTime;
    }

    public Date getPickTime() {
        return pickTime;
    }

    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
    }

    public String getPickAddr() {
        return pickAddr;
    }

    public void setPickAddr(String pickAddr) {
        this.pickAddr = pickAddr;
    }

    public BigDecimal getPickLong() {
        return pickLong;
    }

    public void setPickLong(BigDecimal pickLong) {
        this.pickLong = pickLong;
    }

    public BigDecimal getPickLat() {
        return pickLat;
    }

    public void setPickLat(BigDecimal pickLat) {
        this.pickLat = pickLat;
    }

    public Date getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getDeliverAddr() {
        return deliverAddr;
    }

    public String getBelongCity() {
        return belongCity;
    }

    public void setBelongCity(String belongCity) {
        this.belongCity = belongCity;
    }

    public void setDeliverAddr(String deliverAddr) {
        this.deliverAddr = deliverAddr;
    }

    public BigDecimal getDeliverLong() {
        return deliverLong;
    }

    public void setDeliverLong(BigDecimal deliverLong) {
        this.deliverLong = deliverLong;
    }

    public BigDecimal getDeliverLat() {
        return deliverLat;
    }

    public void setDeliverLat(BigDecimal deliverLat) {
        this.deliverLat = deliverLat;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
