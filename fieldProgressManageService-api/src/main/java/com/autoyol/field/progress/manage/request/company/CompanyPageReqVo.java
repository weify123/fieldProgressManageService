package com.autoyol.field.progress.manage.request.company;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CompanyPageReqVo extends BasePage {
    private static final long serialVersionUID = -4366457345781025958L;

    /**
     *
     */
    @AutoDocProperty(value = "公司一级分类")
    private String firstCategory;

    /**
     *
     */
    @AutoDocProperty(value = "公司二级分类")
    private String secondCategory;

    /**
     * 是否有效:0,无效;1,有效
     */
    @AutoDocProperty(value = "是否有效:0,无效;1,有效;字典类型名[effective_type]")
    private Integer effectivedKey;

    public String getFirstCategory() {
        return firstCategory;
    }

    public void setFirstCategory(String firstCategory) {
        this.firstCategory = firstCategory;
    }

    public String getSecondCategory() {
        return secondCategory;
    }

    public void setSecondCategory(String secondCategory) {
        this.secondCategory = secondCategory;
    }

    public Integer getEffectivedKey() {
        return effectivedKey;
    }

    public void setEffectivedKey(Integer effectivedKey) {
        this.effectivedKey = effectivedKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
