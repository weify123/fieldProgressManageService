package com.autoyol.field.progress.manage.response.userinfo;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class AppUserInfoPageRespVo extends PageResult {
    private static final long serialVersionUID = 3948492859536552653L;

    @AutoDocProperty("用户信息列表")
    private List<AppUserInfoVo> userInfoList;

    public List<AppUserInfoVo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<AppUserInfoVo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
