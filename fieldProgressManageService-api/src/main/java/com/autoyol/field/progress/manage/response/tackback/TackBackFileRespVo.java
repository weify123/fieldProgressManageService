package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class TackBackFileRespVo implements Serializable {
    private static final long serialVersionUID = 6389358376577501016L;

    @AutoDocProperty(value = "图片列表")
    private List<TackBackFileVo> list;

    public List<TackBackFileVo> getList() {
        return list;
    }

    public void setList(List<TackBackFileVo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
