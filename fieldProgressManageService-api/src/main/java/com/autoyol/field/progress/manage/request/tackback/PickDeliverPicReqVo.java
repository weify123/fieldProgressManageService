package com.autoyol.field.progress.manage.request.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class PickDeliverPicReqVo implements Serializable {
    private static final long serialVersionUID = 3759437282531774508L;

    @AutoDocProperty(value = "取送车订单号/查录音时传交易单号", required = true)
    @NotBlank(message = "取送车订单号不能为空")
    private String pickDeliverOrderNo;

    @AutoDocProperty(value = "服务分类;字典[flow_server_type]", required = true)
    @NotNull(message = "服务分类不能为空")
    private Integer serverTypeKey;

    @AutoDocProperty(value = "取送车类型,0：取车，1：送车", required = true)
    @NotNull(message = "取送车类型不能为空")
    private Integer pickDeliverType;

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

    public Integer getPickDeliverType() {
        return pickDeliverType;
    }

    public void setPickDeliverType(Integer pickDeliverType) {
        this.pickDeliverType = pickDeliverType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
