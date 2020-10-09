package com.autoyol.field.progress.manage.request.user;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class UserInfoSearchReqVo extends BaseUserQueryVo {
    private static final long serialVersionUID = 1687621972075809077L;

    @AutoDocProperty("用户姓名")
    private String userName;

    @AutoDocProperty("用户手机号")
    private String userMobile;

    @AutoDocProperty("账户状态：0启用、1停用、2注销;字典类型名[user_status]")
    private Integer statusKey;

    @AutoDocProperty("用户姓名")
    private String userId;

    @AutoDocProperty("联系人用户姓名")
    private String contactName;

    @AutoDocProperty("联系人手机号")
    private String contactMobile;

    @AutoDocProperty("身份证号")
    private String idNo;

    @AutoDocProperty("岗位列表;[,]分割")
    private String station;

    @AutoDocProperty("星级列表;[,]分割")
    private String star;

    @AutoDocProperty("任职情况")
    private String employment;

    @AutoDocProperty("是否上传身份证:0,未上传、1,已上传")
    private Integer idCardUploaded;

    @AutoDocProperty("是否上传驾驶证:0,未上传、1,已上传")
    private Integer driverUploaded;

    @AutoDocProperty("是否上传头像:0,未上传、1,已上传")
    private Integer avatarUploaded;

    @AutoDocProperty("能否派单:0,否;1,能;字典类型名[distribute_type]")
    private Integer distributable;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public Integer getIdCardUploaded() {
        return idCardUploaded;
    }

    public void setIdCardUploaded(Integer idCardUploaded) {
        this.idCardUploaded = idCardUploaded;
    }

    public Integer getDriverUploaded() {
        return driverUploaded;
    }

    public void setDriverUploaded(Integer driverUploaded) {
        this.driverUploaded = driverUploaded;
    }

    public Integer getAvatarUploaded() {
        return avatarUploaded;
    }

    public void setAvatarUploaded(Integer avatarUploaded) {
        this.avatarUploaded = avatarUploaded;
    }

    public Integer getDistributable() {
        return distributable;
    }

    public void setDistributable(Integer distributable) {
        this.distributable = distributable;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
