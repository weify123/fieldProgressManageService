package com.autoyol.field.progress.manage.response;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class FeeAttrRespVo implements Serializable {
    private static final long serialVersionUID = 6083090135941284542L;

    @AutoDocProperty(value = "费用字段列表")
    private List<AttrFeeVo> list;

    public List<AttrFeeVo> getList() {
        return list;
    }

    public void setList(List<AttrFeeVo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
