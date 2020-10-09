package com.autoyol.field.progress.manage.response.user;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class AppUserPageRespVo extends PageResult {
    private static final long serialVersionUID = -6291796143188544638L;

    @AutoDocProperty("用户列表")
    private List<AppUserVo> userList;

    public List<AppUserVo> getUserList() {
        return userList;
    }

    public void setUserList(List<AppUserVo> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
