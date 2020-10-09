package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class PickDeliverPageRespVo extends PageResult {
    private static final long serialVersionUID = 4431462335176350322L;

    @AutoDocProperty(value = "list")
    private List<PickDeliverLogVo> list;

    public List<PickDeliverLogVo> getList() {
        return list;
    }

    public void setList(List<PickDeliverLogVo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
