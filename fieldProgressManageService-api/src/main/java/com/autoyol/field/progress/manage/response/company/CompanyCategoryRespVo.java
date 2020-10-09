package com.autoyol.field.progress.manage.response.company;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.company.CategoryVo;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class CompanyCategoryRespVo implements Serializable {
    private static final long serialVersionUID = -5259646387982533939L;

    @AutoDocProperty(value = "公司分类列表")
    private List<CategoryVo> categoryList;

    public List<CategoryVo> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryVo> categoryList) {
        this.categoryList = categoryList;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
