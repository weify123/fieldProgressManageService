package com.autoyol.field.progress.manage.response.version;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class VersionPageRespVo extends PageResult {
    private static final long serialVersionUID = -1497520729628297752L;
    @AutoDocProperty(value = "版本数据列表")
    private List<VersionVo> versionList;

    public List<VersionVo> getVersionList() {
        return versionList;
    }

    public void setVersionList(List<VersionVo> versionList) {
        this.versionList = versionList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
