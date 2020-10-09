package com.autoyol.field.progress.manage.request.user;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author feiyu.wei
 * @date 2019/12/13
 */
public class AppUserSetPassWordReqVo extends BaseRequest {

    private static final long serialVersionUID = 1417594508915173098L;

    /**
     * 人员id
     */
    @AutoDocProperty(value = "人员id", required = true)
    @NotNull(message = "userId不能为空")
    private Integer userId;

    /**
     * app登录密码
     */
    @AutoDocProperty(value = "app登录密码", required = true)
    @NotBlank(message = "app登录密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度为6-16位")
    private String passWord;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}