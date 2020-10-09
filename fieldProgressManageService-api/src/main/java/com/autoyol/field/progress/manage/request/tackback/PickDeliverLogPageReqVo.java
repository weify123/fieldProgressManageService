package com.autoyol.field.progress.manage.request.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class PickDeliverLogPageReqVo extends BasePage {
    private static final long serialVersionUID = -8716628579474408777L;
    /**
     * 取送车订单号
     */
    @AutoDocProperty(value = "取送车/交易订单号", required = true)
    @NotBlank(message = "取送车订单号不能为空")
    private String orderNo;

    /**
     * 模块类型0:取送订单信息、1:调度信息、2:取车信息、3:送车信息、4:订单信息、5:费用信息[取送车人员费用]
     */
    @AutoDocProperty(value = "模块类型0:取送订单信息、1:调度信息、2:取车信息、3:送车信息、4:订单信息、5:费用信息[取送车人员费用]", required = true)
    @NotNull(message = "模块类型不能为空")
    private Integer moduleType;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getModuleType() {
        return moduleType;
    }

    public void setModuleType(Integer moduleType) {
        this.moduleType = moduleType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
