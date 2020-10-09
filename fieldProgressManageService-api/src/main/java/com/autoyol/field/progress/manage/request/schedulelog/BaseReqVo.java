package com.autoyol.field.progress.manage.request.schedulelog;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class BaseReqVo implements Serializable {
    private static final long serialVersionUID = -2002970628640124070L;

    /**
     * 取送车订单号
     */
    @AutoDocProperty(value = "取送车订单号", required = true)
    @NotBlank(message = "取送车订单号不能为空")
    private String pickDeliverOrderNo;

    public String getPickDeliverOrderNo() {
        return pickDeliverOrderNo;
    }

    public void setPickDeliverOrderNo(String pickDeliverOrderNo) {
        this.pickDeliverOrderNo = pickDeliverOrderNo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
