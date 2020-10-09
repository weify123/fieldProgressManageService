package com.autoyol.field.progress.manage.request.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.response.tackback.FeeRecordVo;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class TackBackFeeInfoReqVo extends BaseTackReqVo {
    private static final long serialVersionUID = -6727503670298105649L;

    @AutoDocProperty(value = "给车主调价理由;字典[adj_price_owner_reason_type]")
    private Integer adjPriceOwnerReasonTypeKey;

    @AutoDocProperty(value = "给租客调价理由;字典[adj_price_rent_reason_type]")
    private Integer adjPriceTenantReasonTypeKey;

    @AutoDocProperty(value = "费用备注")
    private String feeMemo;

    @AutoDocProperty(value = "报备备注")
    private String reportMemo;

    @AutoDocProperty(value = "罚款备注")
    private String fineMemo;

    @AutoDocProperty(value = "承担方;字典[bear_type]")
    private Integer bearTypeKey;

    @AutoDocProperty(value = "費用列表")
    @Valid
    private List<FeeRecordVo> feeList;

    @AutoDocProperty(value = "编辑状态:0:未提交,1:已提交", required = true)
    @NotNull(message = "编辑状态不能为空")
    private Integer editStatus;

    public Integer getAdjPriceOwnerReasonTypeKey() {
        return adjPriceOwnerReasonTypeKey;
    }

    public void setAdjPriceOwnerReasonTypeKey(Integer adjPriceOwnerReasonTypeKey) {
        this.adjPriceOwnerReasonTypeKey = adjPriceOwnerReasonTypeKey;
    }

    public Integer getAdjPriceTenantReasonTypeKey() {
        return adjPriceTenantReasonTypeKey;
    }

    public void setAdjPriceTenantReasonTypeKey(Integer adjPriceTenantReasonTypeKey) {
        this.adjPriceTenantReasonTypeKey = adjPriceTenantReasonTypeKey;
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

    public Integer getBearTypeKey() {
        return bearTypeKey;
    }

    public void setBearTypeKey(Integer bearTypeKey) {
        this.bearTypeKey = bearTypeKey;
    }

    public List<FeeRecordVo> getFeeList() {
        return feeList;
    }

    public void setFeeList(List<FeeRecordVo> feeList) {
        this.feeList = feeList;
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
