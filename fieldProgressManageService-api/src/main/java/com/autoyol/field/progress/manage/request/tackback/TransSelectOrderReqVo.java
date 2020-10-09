package com.autoyol.field.progress.manage.request.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class TransSelectOrderReqVo implements Serializable {
    private static final long serialVersionUID = 5102561043660927097L;

    @AutoDocProperty(value = "交易订单号", required = true)
    @NotBlank(message = "交易订单号不能为空")
    private String transOrderNo;

    @AutoDocProperty(value = "取送车订单号", required = true)
    @NotBlank(message = "取送车订单号不能为空")
    private String pickDeliverOrderNo;

    @AutoDocProperty(value = "服务分类;字典[flow_server_type]", required = true)
    @NotNull(message = "服务分类不能为空")
    private Integer serverTypeKey;

    public String getTransOrderNo() {
        return transOrderNo;
    }

    public void setTransOrderNo(String transOrderNo) {
        this.transOrderNo = transOrderNo;
    }

    public String getPickDeliverOrderNo() {
        return pickDeliverOrderNo;
    }

    public void setPickDeliverOrderNo(String pickDeliverOrderNo) {
        this.pickDeliverOrderNo = pickDeliverOrderNo;
    }

    public Integer getServerTypeKey() {
        return serverTypeKey;
    }

    public void setServerTypeKey(Integer serverTypeKey) {
        this.serverTypeKey = serverTypeKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
