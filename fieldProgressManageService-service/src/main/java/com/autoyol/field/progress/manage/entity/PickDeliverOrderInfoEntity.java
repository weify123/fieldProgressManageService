package com.autoyol.field.progress.manage.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author feiyu.wei
 * @date 2020/02/25
 */
public class PickDeliverOrderInfoEntity {

    private Integer id;
    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 取送车订单号
     */
    private String pickDeliverOrderNo;

    /**
     * 服务分类(3:取车服务,4:还车服务)
     */
    private Integer serviceType;

    /**
     * 是否补单(0:否,1：是)
     */
    private Integer isSupply;

    /**
     * 操作类型(0:新增(取送车订单生成时默认为新增),1:变更(取送车订单有过回退操作的即为变更),2:取消：取送车订单被取消的(包括手动取消和订单取消)为取消)
     */
    private Integer oprType;

    /**
     * 调度状态
     */
    private Integer scheduleStatus;

    /**
     * 外勤app状态
     */
    private Integer fieldAppStatus;

    /**
     *是否直赔
     */
    private Integer isDirectCompensate;

    /**
     *渠道
     */
    private Integer channelKey;

    /**
     *服务类型
     */
    private Integer serviceClass;

    /**
     *是否免服务费
     */
    private Integer isFree;

    /**
     *服务费金额
     */
    private BigDecimal serviceFee;

    /**
     *报案号
     */
    private String reportNo;

    /**
     *金钥匙派单人
     */
    private String goldKey;

    /**
     *订单渠道来源
     */
    private Integer orderChannelSource;

    /**
     *预计服务时长
     */
    private String estimateServiceTime;

    /**
     * 老取车时间
     */
    private Date oldPickTime;

    /**
     * 取车时间
     */
    private Date pickTime;

    /**
     * 取车地址
     */
    private String pickAddr;

    /**
     * 取车经度
     */
    private BigDecimal pickLong;

    /**
     * 取车纬度
     */
    private BigDecimal pickLat;

    /**
     * 送达时间
     */
    private Date deliverTime;

    /**
     * 送达地址
     */
    private String deliverAddr;

    /**
     * 送达经度
     */
    private BigDecimal deliverLong;

    /**
     * 送达纬度
     */
    private BigDecimal deliverLat;

    /**
     * 车主备注(取车备注)
     */
    private String takeNote;

    /**
     * 维修店名称/联系人
     */
    private String repairShopContact;

    /**
     * 维修店联系人电话
     */
    private String repairShopPhone;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 门店电话
     */
    private String storePhone;

    /**
     * 是否上传录音:0:否;1,是
     */
    private Integer isUploadRecord;

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

    public Integer getIsSupply() {
        return isSupply;
    }

    public void setIsSupply(Integer isSupply) {
        this.isSupply = isSupply;
    }

    public Integer getOprType() {
        return oprType;
    }

    public void setOprType(Integer oprType) {
        this.oprType = oprType;
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

    public Integer getIsDirectCompensate() {
        return isDirectCompensate;
    }

    public void setIsDirectCompensate(Integer isDirectCompensate) {
        this.isDirectCompensate = isDirectCompensate;
    }

    public Integer getChannelKey() {
        return channelKey;
    }

    public void setChannelKey(Integer channelKey) {
        this.channelKey = channelKey;
    }

    public Integer getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Integer serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
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

    public Integer getOrderChannelSource() {
        return orderChannelSource;
    }

    public void setOrderChannelSource(Integer orderChannelSource) {
        this.orderChannelSource = orderChannelSource;
    }

    public String getEstimateServiceTime() {
        return estimateServiceTime;
    }

    public void setEstimateServiceTime(String estimateServiceTime) {
        this.estimateServiceTime = estimateServiceTime;
    }

    public Date getOldPickTime() {
        return oldPickTime;
    }

    public void setOldPickTime(Date oldPickTime) {
        this.oldPickTime = oldPickTime;
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
        this.pickAddr = pickAddr == null ? null : pickAddr.trim();
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

    public void setDeliverAddr(String deliverAddr) {
        this.deliverAddr = deliverAddr == null ? null : deliverAddr.trim();
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

    public String getTakeNote() {
        return takeNote;
    }

    public void setTakeNote(String takeNote) {
        this.takeNote = takeNote;
    }

    public String getRepairShopContact() {
        return repairShopContact;
    }

    public void setRepairShopContact(String repairShopContact) {
        this.repairShopContact = repairShopContact;
    }

    public String getRepairShopPhone() {
        return repairShopPhone;
    }

    public void setRepairShopPhone(String repairShopPhone) {
        this.repairShopPhone = repairShopPhone;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public Integer getIsUploadRecord() {
        return isUploadRecord;
    }

    public void setIsUploadRecord(Integer isUploadRecord) {
        this.isUploadRecord = isUploadRecord;
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