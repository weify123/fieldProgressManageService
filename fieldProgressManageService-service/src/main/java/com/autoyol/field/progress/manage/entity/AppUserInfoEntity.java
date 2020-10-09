package com.autoyol.field.progress.manage.entity;

import com.autoyol.doc.annotation.AutoDocIgnoreProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2019/12/17
 */
public class AppUserInfoEntity {
    /**
     * 
     */
    private Integer id;

    private int pageSize;

    private int start;

    /**
     * 人员id
     */
    private Integer userId;

    private String userName;

    private String userMobile;

    private Integer companyId;

    private Integer cityId;

    private Integer statusKey;

    private Integer role;

    private List<Integer> cityIdList;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人手机
     */
    private String contactMobile;

    /**
     * 身份证号
     */
    private String idNo;

    /**
     * 如果根据经纬度可查地址，该字段可删除
     */
    private String address;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 任职情况:0,专职;1,兼职
     */
    private Integer employment;

    /**
     * 星级:1,一星;2,二星;3,三星;4,四星;5,五星;
     */
    private Integer star;

    /**
     * 岗位:0,取送车人员;1,GPS工程师;
     */
    private String station;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 能否派单:1,能;0,否
     */
    private Integer distributable;

    /**
     * 是否上传身份证:0,未上传、1,已上传
     */
    private Integer idCardUploaded;

    /**
     * 是否上传驾驶证:0,未上传、1,已上传
     */
    private Integer driverUploaded;

    /**
     * 是否上传头像:0,未上传、1,已上传
     */
    private Integer avatarUploaded;

    /**
     * 是否删除:0:正常;1,已删除
     */
    private Integer isDelete;

    /**
     * 
     */
    private String createOp;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private String updateOp;

    /**
     * 
     */
    private Date updateTime;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

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

    public List<Integer> getCityIdList() {
        return cityIdList;
    }

    public void setCityIdList(List<Integer> cityIdList) {
        this.cityIdList = cityIdList;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName == null ? null : contactName.trim();
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile == null ? null : contactMobile.trim();
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Integer getEmployment() {
        return employment;
    }

    public void setEmployment(Integer employment) {
        this.employment = employment;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getStation() {
        return station;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStation(String station) {
        this.station = station == null ? null : station.trim();
    }

    public Integer getDistributable() {
        return distributable;
    }

    public void setDistributable(Integer distributable) {
        this.distributable = distributable;
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

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateOp() {
        return createOp;
    }

    public void setCreateOp(String createOp) {
        this.createOp = createOp == null ? null : createOp.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateOp() {
        return updateOp;
    }

    public void setUpdateOp(String updateOp) {
        this.updateOp = updateOp == null ? null : updateOp.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}