package com.autoyol.field.progress.manage.request.company;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import com.autoyol.field.progress.manage.util.Email;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class CompanyUpdateReqVo extends BaseRequest {
    private static final long serialVersionUID = 7462451872908980464L;

    @AutoDocProperty(value = "公司id", required = true)
    @NotNull(message = "公司id不能为空")
    private Integer companyId;

    @AutoDocProperty(value = "一级分类", required = true)
    @NotBlank(message = "一级分类不能为空")
    @Length(max = 50, message = "一级分类最长50位")
    private String firstCategory;

    @AutoDocProperty(value = "二级分类", required = true)
    @NotBlank(message = "二级分类不能为空")
    @Length(max = 50, message = "二级分类最长50位")
    private String secondCategory;
    /**
     * 邮箱
     */
    @AutoDocProperty(value = "邮箱")
    @Email(message = "邮箱格式不正确")
    @Length(max = 30, message = "邮箱最长30位")
    private String email;

    /**
     * 是否有效:0,无效;1,有效
     */
    @AutoDocProperty(value = "是否有效:0,无效;1,有效;字典类型名[effective_type]", required = true)
    @NotNull(message = "是否有效不能为空")
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
