package com.autoyol.field.progress.manage.response.version;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class VersionMaxRespVo implements Serializable {
    private static final long serialVersionUID = 7327973140068049861L;

    @AutoDocProperty(value = "内部最大版本号")
    private String innerMaxVersion;

    @AutoDocProperty(value = "外部最大版本号")
    private String outerMaxVersion;

    public String getInnerMaxVersion() {
        return innerMaxVersion;
    }

    public void setInnerMaxVersion(String innerMaxVersion) {
        this.innerMaxVersion = innerMaxVersion;
    }

    public String getOuterMaxVersion() {
        return outerMaxVersion;
    }

    public void setOuterMaxVersion(String outerMaxVersion) {
        this.outerMaxVersion = outerMaxVersion;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
