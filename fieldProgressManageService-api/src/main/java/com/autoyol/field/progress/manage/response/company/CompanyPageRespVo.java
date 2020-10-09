package com.autoyol.field.progress.manage.response.company;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class CompanyPageRespVo extends PageResult {
    private static final long serialVersionUID = -773068456583581872L;

    @AutoDocProperty("公司列表")
    private List<CompanyVo> companyList;

    public List<CompanyVo> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<CompanyVo> companyList) {
        this.companyList = companyList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
