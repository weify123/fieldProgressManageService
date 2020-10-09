package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PickBackFeeRespVo implements Serializable {
    private static final long serialVersionUID = -651035737369222620L;

    @AutoDocProperty(value = "取送车订单号")
    private String pickDeliverOrderNo;

    /**
     * 承担方(0：平台、1：租客、2：车主:3：三方)
     */
    @AutoDocProperty(value = "承担方;字典[bear_type]")
    private Integer bearTypeKey;

    @AutoDocProperty(value = "承担方")
    private String bearTypeVal;

    @AutoDocProperty(value = "车主补贴原因;字典[subsidy_owner_reason_type]")
    private String allowanceOwnerReasonTypeVal;

    @AutoDocProperty(value = "租客补贴原因;字典[subsidy_rent_reason_type]")
    private String allowanceTenantReasonTypeVal;

    @AutoDocProperty(value = "给车主调价理由;字典[adj_price_owner_reason_type]")
    private Integer adjPriceOwnerReasonTypeKey;

    @AutoDocProperty(value = "给车主调价理由")
    private String adjPriceOwnerReasonTypeVal;

    @AutoDocProperty(value = "给租客调价理由;字典[adj_price_rent_reason_type]")
    private Integer adjPriceTenantReasonTypeKey;

    @AutoDocProperty(value = "给租客调价理由")
    private String adjPriceTenantReasonTypeVal;

    /**
     * 费用备注
     */
    @AutoDocProperty(value = "费用备注")
    private String feeMemo;

    /**
     * 报备备注
     */
    @AutoDocProperty(value = "报备备注")
    private String reportMemo;

    /**
     * 罚款备注
     */
    @AutoDocProperty(value = "罚款备注")
    private String fineMemo;

    @AutoDocProperty(value = "費用")
    private Map<String, FeeRecordVo> feeRecordMap;

    /**
     * 编辑状态:0:未提交1,已提交
     */
    @AutoDocProperty(value = "编辑状态:0:未提交1,已提交")
    private Integer editStatus;

    public String getPickDeliverOrderNo() {
        return pickDeliverOrderNo;
    }

    public void setPickDeliverOrderNo(String pickDeliverOrderNo) {
        this.pickDeliverOrderNo = pickDeliverOrderNo;
    }

    public Integer getBearTypeKey() {
        return bearTypeKey;
    }

    public void setBearTypeKey(Integer bearTypeKey) {
        this.bearTypeKey = bearTypeKey;
    }

    public String getBearTypeVal() {
        return bearTypeVal;
    }

    public void setBearTypeVal(String bearTypeVal) {
        this.bearTypeVal = bearTypeVal;
    }

    public String getAllowanceOwnerReasonTypeVal() {
        return allowanceOwnerReasonTypeVal;
    }

    public void setAllowanceOwnerReasonTypeVal(String allowanceOwnerReasonTypeVal) {
        this.allowanceOwnerReasonTypeVal = allowanceOwnerReasonTypeVal;
    }

    public String getAllowanceTenantReasonTypeVal() {
        return allowanceTenantReasonTypeVal;
    }

    public void setAllowanceTenantReasonTypeVal(String allowanceTenantReasonTypeVal) {
        this.allowanceTenantReasonTypeVal = allowanceTenantReasonTypeVal;
    }

    public Integer getAdjPriceOwnerReasonTypeKey() {
        return adjPriceOwnerReasonTypeKey;
    }

    public void setAdjPriceOwnerReasonTypeKey(Integer adjPriceOwnerReasonTypeKey) {
        this.adjPriceOwnerReasonTypeKey = adjPriceOwnerReasonTypeKey;
    }

    public String getAdjPriceOwnerReasonTypeVal() {
        return adjPriceOwnerReasonTypeVal;
    }

    public void setAdjPriceOwnerReasonTypeVal(String adjPriceOwnerReasonTypeVal) {
        this.adjPriceOwnerReasonTypeVal = adjPriceOwnerReasonTypeVal;
    }

    public Integer getAdjPriceTenantReasonTypeKey() {
        return adjPriceTenantReasonTypeKey;
    }

    public void setAdjPriceTenantReasonTypeKey(Integer adjPriceTenantReasonTypeKey) {
        this.adjPriceTenantReasonTypeKey = adjPriceTenantReasonTypeKey;
    }

    public String getAdjPriceTenantReasonTypeVal() {
        return adjPriceTenantReasonTypeVal;
    }

    public void setAdjPriceTenantReasonTypeVal(String adjPriceTenantReasonTypeVal) {
        this.adjPriceTenantReasonTypeVal = adjPriceTenantReasonTypeVal;
    }

    public String getFeeMemo() {
        return feeMemo;
    }

    public void setFeeMemo(String feeMemo) {
        this.feeMemo = feeMemo;
    }

    public String getReportMemo() {
        return reportMemo;
    }

    public void setReportMemo(String reportMemo) {
        this.reportMemo = reportMemo;
    }

    public String getFineMemo() {
        return fineMemo;
    }

    public void setFineMemo(String fineMemo) {
        this.fineMemo = fineMemo;
    }

    public Map<String, FeeRecordVo> getFeeRecordMap() {
        return feeRecordMap;
    }

    public void setFeeRecordMap(Map<String, FeeRecordVo> feeRecordMap) {
        this.feeRecordMap = feeRecordMap;
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
