package com.autoyol.field.progress.manage.request.dict;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class DictReqVo implements Serializable {
    private static final long serialVersionUID = 6584852696779502422L;

    /**
     * 人员id
     */
    @AutoDocProperty(value = "类型名,多个使用[,]分割", required = true)
    @NotBlank(message = "类型名不能为空")
    private String typeNameStr;

    public DictReqVo() {
    }

    public DictReqVo(String typeNameStr) {
        this.typeNameStr = typeNameStr;
    }

    public String getTypeNameStr() {
        return typeNameStr;
    }

    public void setTypeNameStr(String typeNameStr) {
        this.typeNameStr = typeNameStr;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
