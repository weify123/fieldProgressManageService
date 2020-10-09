package com.autoyol.field.progress.manage.request.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

public class TackBackPersonReqVo extends BasePage {
    private static final long serialVersionUID = -4292483371678350590L;

    @AutoDocProperty(value = "交易订单号", required = true)
    @NotBlank(message = "交易订单号不能为空")
    private String transOrderNo;

    @AutoDocProperty(value = "服务公司id")
    private Integer companyId;

    @AutoDocProperty(value = "用户姓名")
    private String userName;

    public String getTransOrderNo() {
        return transOrderNo;
    }

    public void setTransOrderNo(String transOrderNo) {
        this.transOrderNo = transOrderNo;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
