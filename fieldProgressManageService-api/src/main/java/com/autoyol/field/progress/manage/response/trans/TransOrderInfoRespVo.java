package com.autoyol.field.progress.manage.response.trans;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TransOrderInfoRespVo implements Serializable {
    private static final long serialVersionUID = -6840312777900946577L;

    /**
     * 订单号
     */
    @AutoDocProperty(value = "订单号")
    private String orderNo;

    @AutoDocProperty(value = "续租订单编号")
    private String renewOrderNo;

    /**
     * 所属城市
     */
    @AutoDocProperty(value = "所属城市")
    private String belongCity;

    @AutoDocProperty(value = "交易来源;字典[trans_source]")
    private Integer sourceKey;

    @AutoDocProperty(value = "交易来源")
    private String sourceVal;

    @AutoDocProperty(value = "订单来源场景;字典[scene_source]")
    private Integer sceneSourceKey;

    @AutoDocProperty(value = "订单来源场景")
    private String sceneSourceVal;

    @AutoDocProperty(value = "线下订单类型;字典[offline_order_type]")
    private Integer offlineOrderTypeKey;

    @AutoDocProperty(value = "线下订单类型")
    private String offlineOrderTypeVal;

    @AutoDocProperty(value = "订单起租时间")
    private Date rentStartTime;

    @AutoDocProperty(value = "订单结束时间")
    private Date rentEntTime;

    @AutoDocProperty(value = "租金")
    private BigDecimal rentAmt;

    @AutoDocProperty(value = "天均价")
    private BigDecimal pricePerDay;

    @AutoDocProperty(value = "车辆押金支付时间")
    private Date depositPayTime;

    @AutoDocProperty(value = "超级补充全险")
    private String superSuppleRisk;

    @AutoDocProperty(value = "合作方")
    private String partner;

    @AutoDocProperty(value = "风控审核状态")
    private String riskControlAuditState;

    @AutoDocProperty(value = "客服备注")
    private String custNote;

    @AutoDocProperty(value = "编辑状态:0:未提交1,已提交")
    private Integer editStatus;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRenewOrderNo() {
        return renewOrderNo;
    }

    public void setRenewOrderNo(String renewOrderNo) {
        this.renewOrderNo = renewOrderNo;
    }

    public String getBelongCity() {
        return belongCity;
    }

    public void setBelongCity(String belongCity) {
        this.belongCity = belongCity;
    }

    public Integer getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(Integer sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getSourceVal() {
        return sourceVal;
    }

    public void setSourceVal(String sourceVal) {
        this.sourceVal = sourceVal;
    }

    public Integer getSceneSourceKey() {
        return sceneSourceKey;
    }

    public void setSceneSourceKey(Integer sceneSourceKey) {
        this.sceneSourceKey = sceneSourceKey;
    }

    public String getSceneSourceVal() {
        return sceneSourceVal;
    }

    public void setSceneSourceVal(String sceneSourceVal) {
        this.sceneSourceVal = sceneSourceVal;
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

    public Date getRentStartTime() {
        return rentStartTime;
    }

    public void setRentStartTime(Date rentStartTime) {
        this.rentStartTime = rentStartTime;
    }

    public Date getRentEntTime() {
        return rentEntTime;
    }

    public void setRentEntTime(Date rentEntTime) {
        this.rentEntTime = rentEntTime;
    }

    public BigDecimal getRentAmt() {
        return rentAmt;
    }

    public void setRentAmt(BigDecimal rentAmt) {
        this.rentAmt = rentAmt;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Date getDepositPayTime() {
        return depositPayTime;
    }

    public void setDepositPayTime(Date depositPayTime) {
        this.depositPayTime = depositPayTime;
    }

    public String getSuperSuppleRisk() {
        return superSuppleRisk;
    }

    public void setSuperSuppleRisk(String superSuppleRisk) {
        this.superSuppleRisk = superSuppleRisk;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getRiskControlAuditState() {
        return riskControlAuditState;
    }

    public void setRiskControlAuditState(String riskControlAuditState) {
        this.riskControlAuditState = riskControlAuditState;
    }

    public String getCustNote() {
        return custNote;
    }

    public void setCustNote(String custNote) {
        this.custNote = custNote;
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
