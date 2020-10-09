package com.autoyol.field.progress.manage.response;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class ImportLogRespVo extends PageResult {
    private static final long serialVersionUID = 925317102322651862L;

    @AutoDocProperty("导入日志记录")
    private List<ImportLogVo> logList;

    public List<ImportLogVo> getLogList() {
        return logList;
    }

    public void setLogList(List<ImportLogVo> logList) {
        this.logList = logList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
