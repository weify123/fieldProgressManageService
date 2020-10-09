package com.autoyol.field.progress.manage.request.attendance;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import com.autoyol.field.progress.manage.request.user.BaseUserQueryVo;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class AttendanceQueryReqVo extends BaseUserQueryVo {
    private static final long serialVersionUID = 3618301462340176822L;

    @AutoDocProperty("人员id")
    private Integer userId;

    @AutoDocProperty("用户姓名")
    private String userName;

    @AutoDocProperty("账户状态：0启用、1停用、2注销;字典类型名[user_status]")
    private Integer statusKey;

    @AutoDocProperty("日期时间:如2019-12-23")
    private String dateTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(Integer statusKey) {
        this.statusKey = statusKey;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
