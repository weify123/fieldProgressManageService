package com.autoyol.field.progress.manage.request.order;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

public class CancelTransOrderReqVo implements Serializable {
    private static final long serialVersionUID = 5520323788993362540L;

    /**
     * 订单编号
     **/
    @NotBlank(message = "订单编号为空")
    private String ordernumber;
    /**
     * 服务类型（take:取车服务 back:还车服务 all:所有）
     **/
    @NotBlank(message = "服务类型为空")
    private String servicetype;
    /**
     * 签名
     **/
    private String sign;

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
