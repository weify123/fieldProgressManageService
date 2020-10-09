package com.autoyol.field.progress.manage.request.version;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class VersionPageReqVo extends BasePage {
    private static final long serialVersionUID = 3334315820641504948L;
    /**
     * 内部版本号
     */
    @AutoDocProperty("内部版本号")
    private String innerVersion;

    /**
     * 外部版本号
     */
    @AutoDocProperty("内部版本号")
    private String outerVersion;

    /**
     * 是否发布:0,否;1,是
     */
    @AutoDocProperty("是否发布0,否;1,是;字典类型名[publish_type]")
    private Integer publishedKey;

    public String getInnerVersion() {
        return innerVersion;
    }

    public void setInnerVersion(String innerVersion) {
        this.innerVersion = innerVersion;
    }

    public String getOuterVersion() {
        return outerVersion;
    }

    public void setOuterVersion(String outerVersion) {
        this.outerVersion = outerVersion;
    }

    public Integer getPublishedKey() {
        return publishedKey;
    }

    public void setPublishedKey(Integer publishedKey) {
        this.publishedKey = publishedKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
