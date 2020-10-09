package com.autoyol.field.progress.manage.request.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class TransOrderReqVo extends BaseTackReqVo {
    private static final long serialVersionUID = 5838583130328323426L;

    @AutoDocProperty(value = "交易订单号", required = true)
    @NotBlank(message = "交易订单号不能为空")
    private String transOrderNo;

    @AutoDocProperty(value = "续租订单编号", required = true)
    private String renewOrderNo;

    @AutoDocProperty(value = "编辑状态:0:未提交,1:已提交", required = true)
    @NotNull(message = "编辑状态不能为空")
    private Integer editStatus;

    public String getTransOrderNo() {
        return transOrderNo;
    }

    public void setTransOrderNo(String transOrderNo) {
        this.transOrderNo = transOrderNo;
    }

    public String getRenewOrderNo() {
        return renewOrderNo;
    }

    public void setRenewOrderNo(String renewOrderNo) {
        this.renewOrderNo = renewOrderNo;
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
