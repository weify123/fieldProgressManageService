package com.autoyol.field.progress.manage.response.node;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class RemindPageRespVo extends PageResult {
    private static final long serialVersionUID = -4053676303202548065L;

    @AutoDocProperty(value = "数据列表")
    private List<RemindVo> remindList;

    public List<RemindVo> getRemindList() {
        return remindList;
    }

    public void setRemindList(List<RemindVo> remindList) {
        this.remindList = remindList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
