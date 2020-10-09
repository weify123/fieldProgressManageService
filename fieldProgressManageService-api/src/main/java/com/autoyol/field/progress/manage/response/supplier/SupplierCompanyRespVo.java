package com.autoyol.field.progress.manage.response.supplier;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class SupplierCompanyRespVo implements Serializable {
    private static final long serialVersionUID = -294816078042672707L;

    @AutoDocProperty("供应商服务公司列表")
    private List<SupplierCompanyVo> list;

    public List<SupplierCompanyVo> getList() {
        return list;
    }

    public void setList(List<SupplierCompanyVo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
