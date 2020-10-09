package com.autoyol.field.progress.manage.request.user;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author feiyu.wei
 * @date 2019/12/13
 */
public class AppUserAddReqVo extends BaseRequest {

    private static final long serialVersionUID = 4273029204225928975L;
    /**
     * 用户姓名
     */
    @AutoDocProperty(value = "用户姓名", required = true)
    @NotBlank(message = "用户姓名不能为空")
    @Size(min = 1, max = 10, message = "用户姓名长度最多10位")
    private String userName;

    /**
     * 用户手机号
     */
    @AutoDocProperty(value = "用户手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号格式错误")
    private String mobile;

    /**
     * 服务公司
     */
    @AutoDocProperty(value = "服务公司Id", required = true)
    @NotNull(message = "服务公司不能为空")
    private Integer companyId;

    /**
     * 城市
     */
    @AutoDocProperty(value = "城市Id", required = true)
    @NotNull(message = "城市不能为空")
    private Integer cityId;

    /**
     * 当前用户的账户状态：0启用、1停用、2注销
     */
    @AutoDocProperty(value = "当前用户的账户状态：0启用、1停用、2注销;字典类型名[user_status]", required = true)
    @NotNull(message = "账户状态不能为空")
    private Integer statusKey;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
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