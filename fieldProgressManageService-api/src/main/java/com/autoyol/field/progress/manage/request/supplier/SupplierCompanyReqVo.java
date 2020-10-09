package com.autoyol.field.progress.manage.request.supplier;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

public class SupplierCompanyReqVo extends BaseRequest {
    private static final long serialVersionUID = -7710183631426993942L;

    @AutoDocProperty(value = "交易订单号", required = true)
    @NotBlank(message = "交易订单号不能为空")
    private String transOrderNo;

    public String getTransOrderNo() {
        return transOrderNo;
    }

    public void setTransOrderNo(String transOrderNo) {
        this.transOrderNo = transOrderNo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
