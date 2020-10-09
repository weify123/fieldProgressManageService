package com.autoyol.field.progress.manage.request.supplier;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import com.autoyol.field.progress.manage.util.Email;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SupplierUpdateVo extends BaseRequest {
    private static final long serialVersionUID = 6755975768000972355L;

    @AutoDocProperty("id")
    @NotNull(message = "id不能为空")
    private Integer id;

    /*@AutoDocProperty("服务公司id")
    @NotNull(message = "公司不能为空")
    private Integer companyId;*/

    /*@AutoDocProperty("接收通知邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Length(max = 100, message = "邮箱最长100位")
    private String email;*/

    @AutoDocProperty(value = "管理城市id【、】分割")
    @NotBlank(message = "管理城市不能为空")
    @Pattern(regexp = "^\\d+(、\\d+)*$", message = "城市格式错误")
    private String manageCityIdStr;

    @AutoDocProperty("是否启用：0启用、1停用;字典类型名[user_status]")
    @NotNull(message = "是否启用不能为空")
    private Integer statusKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /*public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }*/

    /*public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }*/

    public String getManageCityIdStr() {
        return manageCityIdStr;
    }

    public void setManageCityIdStr(String manageCityIdStr) {
        this.manageCityIdStr = manageCityIdStr;
    }

    public Integer getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(Integer statusKey) {
        this.statusKey = statusKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
