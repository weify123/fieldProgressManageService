package com.autoyol.field.progress.manage.response.supplier;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Objects;

public class SupplierCompanyVo implements Serializable {
    private static final long serialVersionUID = -2941632784624353444L;

    @AutoDocProperty("服务公司id")
    private Integer companyId;

    @AutoDocProperty(value = "公司二级分类")
    private String secondCategory;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getSecondCategory() {
        return secondCategory;
    }

    public void setSecondCategory(String secondCategory) {
        this.secondCategory = secondCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SupplierCompanyVo that = (SupplierCompanyVo) o;
        return Objects.equals(companyId, that.companyId) &&
                Objects.equals(secondCategory, that.secondCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, secondCategory);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
