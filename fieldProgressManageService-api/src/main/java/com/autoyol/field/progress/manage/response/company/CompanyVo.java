package com.autoyol.field.progress.manage.response.company;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.constraint.ExcelElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class CompanyVo implements Serializable {
    private static final long serialVersionUID = -441857290455925428L;

    /**
     *
     */
    @AutoDocProperty(value = "公司id")
    @ExcelElement(field = "公司id", index = 1)
    private Integer companyId;

    /**
     *
     */
    @AutoDocProperty(value = "公司一级分类")
    @ExcelElement(field = "服务公司一级分类", index = 2)
    private String firstCategory;

    /**
     *
     */
    @AutoDocProperty(value = "公司二级分类")
    @ExcelElement(field = "服务公司二级分类", index = 3)
    private String secondCategory;

    /**
     *
     */
    @AutoDocProperty(value = "邮箱")
    @ExcelElement(field = "联系邮箱", index = 4)
    private String email;

    /**
     * 是否有效:0,无效;1,有效
     */
    private Integer effectivedKey;

    @AutoDocProperty(value = "是否有效:0,无效;1,有效;字典类型名[effective_type]")
    @ExcelElement(field = "是否有效", index = 5)
    private String effectivedVal;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEffectivedKey() {
        return effectivedKey;
    }

    public void setEffectivedKey(Integer effectivedKey) {
        this.effectivedKey = effectivedKey;
    }

    public String getEffectivedVal() {
        return effectivedVal;
    }

    public void setEffectivedVal(String effectivedVal) {
        this.effectivedVal = effectivedVal;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
