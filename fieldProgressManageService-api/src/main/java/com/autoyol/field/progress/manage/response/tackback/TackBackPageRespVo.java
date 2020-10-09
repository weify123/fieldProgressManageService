package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class TackBackPageRespVo extends PageResult {
    private static final long serialVersionUID = -4147294667461414228L;

    @AutoDocProperty(value = "list")
    private List<TackBackPageVo> list;

    public List<TackBackPageVo> getList() {
        return list;
    }

    public void setList(List<TackBackPageVo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
