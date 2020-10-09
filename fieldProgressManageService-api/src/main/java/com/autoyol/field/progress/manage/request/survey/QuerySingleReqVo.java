package com.autoyol.field.progress.manage.request.survey;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class QuerySingleReqVo implements Serializable {
    private static final long serialVersionUID = -3629378346713736797L;
    /**
     * 人员id
     */
    @AutoDocProperty(value = "主键id", required = true)
    @NotNull(message = "id不能为空")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
