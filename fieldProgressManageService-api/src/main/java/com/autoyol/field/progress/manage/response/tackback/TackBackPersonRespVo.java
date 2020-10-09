package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class TackBackPersonRespVo extends PageResult {
    private static final long serialVersionUID = 3814918959271983256L;

    @AutoDocProperty("人员列表")
    private List<TackBackPersonVo> list;

    public List<TackBackPersonVo> getList() {
        return list;
    }

    public void setList(List<TackBackPersonVo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
