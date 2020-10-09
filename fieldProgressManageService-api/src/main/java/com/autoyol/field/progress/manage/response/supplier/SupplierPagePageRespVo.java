package com.autoyol.field.progress.manage.response.supplier;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class SupplierPagePageRespVo extends PageResult {
    private static final long serialVersionUID = -1432440650285743867L;

    @AutoDocProperty("供应商列表")
    private List<SupplierVo> supplierVos;

    public List<SupplierVo> getSupplierVos() {
        return supplierVos;
    }

    public void setSupplierVos(List<SupplierVo> supplierVos) {
        this.supplierVos = supplierVos;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
