package com.autoyol.field.progress.manage.request.company;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.response.company.CompanySecondCategory;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class CategoryVo implements Serializable {
    private static final long serialVersionUID = 1743777798843150259L;

    @AutoDocProperty(value = "分类")
    private String firstCategory;

    @AutoDocProperty(value = "二级分类列表")
    private List<CompanySecondCategory> secondCategory;

    public String getFirstCategory() {
        return firstCategory;
    }

    public void setFirstCategory(String firstCategory) {
        this.firstCategory = firstCategory;
    }

    public List<CompanySecondCategory> getSecondCategory() {
        return secondCategory;
    }

    public void setSecondCategory(List<CompanySecondCategory> secondCategory) {
        this.secondCategory = secondCategory;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
