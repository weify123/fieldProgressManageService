package com.autoyol.field.progress.manage.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PickDeliverDeliverInfoLogEntity {

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
     * 实际送达时间
     */
    private Date realDeliverTime;

    /**
     * 送达油量
     */
    private BigDecimal deliverOil;

    /**
     * 送达公里数
     */
    private BigDecimal deliverKilo;

    /**
     * 变更送达地址
     */
    private String changeDeliverAddress;

    /**
     * 变更送达经度
     */
    private BigDecimal changeDeliverLong;

    /**
     * 变更送达纬度
     */
    private BigDecimal changeDeliverLat;

    /**
     * 延期送达时间
     */
    private Date delayDeliverTime;

    /**
     * 送达迟到原因;0、非租客原因迟到；1、租客原因迟到；2、实际准时送达
     */
    private Integer delaySendReason;

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

    public Date getRealDeliverTime() {
        return realDeliverTime;
    }

    public void setRealDeliverTime(Date realDeliverTime) {
        this.realDeliverTime = realDeliverTime;
    }

    public BigDecimal getDeliverOil() {
        return deliverOil;
    }

    public void setDeliverOil(BigDecimal deliverOil) {
        this.deliverOil = deliverOil;
    }

    public BigDecimal getDeliverKilo() {
        return deliverKilo;
    }

    public void setDeliverKilo(BigDecimal deliverKilo) {
        this.deliverKilo = deliverKilo;
    }

    public String getChangeDeliverAddress() {
        return changeDeliverAddress;
    }

    public void setChangeDeliverAddress(String changeDeliverAddress) {
        this.changeDeliverAddress = changeDeliverAddress == null ? null : changeDeliverAddress.trim();
    }

    public BigDecimal getChangeDeliverLong() {
        return changeDeliverLong;
    }

    public void setChangeDeliverLong(BigDecimal changeDeliverLong) {
        this.changeDeliverLong = changeDeliverLong;
    }

    public BigDecimal getChangeDeliverLat() {
        return changeDeliverLat;
    }

    public void setChangeDeliverLat(BigDecimal changeDeliverLat) {
        this.changeDeliverLat = changeDeliverLat;
    }

    public Date getDelayDeliverTime() {
        return delayDeliverTime;
    }

    public void setDelayDeliverTime(Date delayDeliverTime) {
        this.delayDeliverTime = delayDeliverTime;
    }

    public Integer getDelaySendReason() {
        return delaySendReason;
    }

    public void setDelaySendReason(Integer delaySendReason) {
        this.delaySendReason = delaySendReason;
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
