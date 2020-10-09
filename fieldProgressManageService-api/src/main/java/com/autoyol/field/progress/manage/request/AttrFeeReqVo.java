package com.autoyol.field.progress.manage.request;

import com.autoyol.doc.annotation.AutoDocProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AttrFeeReqVo implements Serializable {
    private static final long serialVersionUID = 1833404182871072487L;

    @NotNull(message = "服务类型为空")
    @AutoDocProperty(value = "服务分类", required = true)
    private Integer serviceTypeKey;

    public Integer getServiceTypeKey() {
        return serviceTypeKey;
    }

    public void setServiceTypeKey(Integer serviceTypeKey) {
        this.serviceTypeKey = serviceTypeKey;
    }
}
