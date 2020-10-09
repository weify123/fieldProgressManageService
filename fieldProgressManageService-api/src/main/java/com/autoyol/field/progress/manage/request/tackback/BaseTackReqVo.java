package com.autoyol.field.progress.manage.request.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class BaseTackReqVo extends BaseRequest {
    private static final long serialVersionUID = 1641381143445058530L;

    @AutoDocProperty(value = "取送车订单号", required = true)
    @NotBlank(message = "取送车订单号不能为空")
    private String pickDeliverOrderNo;

    @AutoDocProperty(value = "服务分类;字典[flow_server_type]", required = true)
    @NotNull(message = "服务分类不能为空")
    private Integer serverTypeKey;

    @AutoDocProperty(value = "取车时间年,格式yyyyMMdd", required = true)
    @NotBlank(message = "取车时间不能为空")
    private String pickTime;

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

    public String getPickTime() {
        return pickTime;
    }

    public void setPickTime(String pickTime) {
        this.pickTime = pickTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
