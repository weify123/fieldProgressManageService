package com.autoyol.field.progress.manage.request;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UploadReqVo implements Serializable {
    private static final long serialVersionUID = -3048649472632055859L;

    @AutoDocProperty(value = "业务类型;0,证件上传(如头像、驾驶证、身份证);1,app相关文件;2,取车服务取车;3,取车服务送车;4,还车服务取车;5,还车服务送车", required = true)
    @NotNull(message = "业务类型不能为空")
    private Integer businessKey;

    public Integer getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(Integer businessKey) {
        this.businessKey = businessKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
