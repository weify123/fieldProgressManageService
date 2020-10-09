package com.autoyol.field.progress.manage.response;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class AttrFeeVo implements Serializable {
    private static final long serialVersionUID = 3526671219642122595L;

    /**
     * 费用code
     */
    @AutoDocProperty(value = "费用code")
    private String attrCode;

    /**
     * 费用名称
     */
    @AutoDocProperty(value = "费用名称")
    private String attrName;


    public String getAttrCode() {
        return attrCode;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
