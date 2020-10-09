package com.autoyol.field.progress.manage.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class UserInfoVo implements Serializable {
    private static final long serialVersionUID = 9150391734493079106L;

    private Integer userId;

    private String contactMobile;

    private String idNo;

    public UserInfoVo() {
    }

    public UserInfoVo(Integer userId, String contactMobile, String idNo) {
        this.userId = userId;
        this.contactMobile = contactMobile;
        this.idNo = idNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public String getIdNo() {
        return idNo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
