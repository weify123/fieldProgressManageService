package com.autoyol.field.progress.manage.request.user;

import com.autoyol.doc.annotation.AutoDocIgnoreProperty;
import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.validation.constraints.Pattern;
import java.util.List;

public class AppUserQueryReqVo extends BaseUserQueryVo {

    @AutoDocProperty("用户姓名")
    private String userName;

    @AutoDocProperty("用户手机号")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "用户手机号号格式错误")
    private String userMobile;

    @AutoDocProperty("账户状态：0启用、1停用、2注销;字典类型名[user_status]")
    private Integer statusKey;

    @JsonIgnore
    @AutoDocIgnoreProperty
    private Integer userId;

    @JsonIgnore
    @AutoDocIgnoreProperty
    private List<Integer> cityIdList;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Integer getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(Integer statusKey) {
        this.statusKey = statusKey;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Integer> getCityIdList() {
        return cityIdList;
    }

    public void setCityIdList(List<Integer> cityIdList) {
        this.cityIdList = cityIdList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
