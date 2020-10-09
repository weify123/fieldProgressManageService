package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TackDeliverInfoVo implements Serializable {
    private static final long serialVersionUID = 4858673505443631209L;

    @AutoDocProperty(value = "取送车订单号")
    private String pickDeliverOrderNo;

    @AutoDocProperty(value = "实际送达时间")
    private Date realDeliverTime;

    @AutoDocProperty(value = "送达油量")
    private BigDecimal deliverOil;

    @AutoDocProperty(value = "送达公里数")
    private BigDecimal deliverKilo;

    @AutoDocProperty(value = "变更送达地址")
    private String changeDeliverAddress;

    @AutoDocProperty(value = "变更送达经度")
    private BigDecimal changeDeliverLong;

    @AutoDocProperty(value = "变更送达纬度")
    private BigDecimal changeDeliverLat;

    @AutoDocProperty(value = "延期送达时间")
    private Date delayDeliverTime;

    @AutoDocProperty(value = "送达迟到原因;字典[delay_reason]")
    private Integer delayReasonKey;

    @AutoDocProperty(value = "送达迟到原因")
    private String delayReasonVal;

    @AutoDocProperty(value = "编辑状态:0:未提交1,已提交")
    private Integer editStatus;

    public String getPickDeliverOrderNo() {
        return pickDeliverOrderNo;
    }

    public void setPickDeliverOrderNo(String pickDeliverOrderNo) {
        this.pickDeliverOrderNo = pickDeliverOrderNo;
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
        this.changeDeliverAddress = changeDeliverAddress;
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

    public Integer getDelayReasonKey() {
        return delayReasonKey;
    }

    public void setDelayReasonKey(Integer delayReasonKey) {
        this.delayReasonKey = delayReasonKey;
    }

    public String getDelayReasonVal() {
        return delayReasonVal;
    }

    public void setDelayReasonVal(String delayReasonVal) {
        this.delayReasonVal = delayReasonVal;
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
