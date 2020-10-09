package com.autoyol.field.progress.manage.request.user;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author feiyu.wei
 * @date 2019/12/19
 */
public class ImportUserInfoReqVo implements Serializable {
    private static final long serialVersionUID = -3034155882599278810L;

    private Integer userId;

    /**
     * 用户姓名
     */
    private String userName;

    private String companyName;

    private String cityName;

    private String mobile;

    private String idNo;

    /**
     * 如果根据经纬度可查地址，该字段可删除
     */
    private String address;

    private String contactName;

    private String contactMobile;

    private String employment;

    private String star;

    private String station;

    private String distribute;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
        this.address = address;
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

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDistribute() {
        return distribute;
    }

    public void setDistribute(String distribute) {
        this.distribute = distribute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImportUserInfoReqVo that = (ImportUserInfoReqVo) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(companyName, that.companyName) &&
                Objects.equals(cityName, that.cityName) &&
                Objects.equals(mobile, that.mobile) &&
                Objects.equals(idNo, that.idNo) &&
                Objects.equals(contactName, that.contactName) &&
                Objects.equals(contactMobile, that.contactMobile) &&
                Objects.equals(employment, that.employment) &&
                Objects.equals(star, that.star) &&
                Objects.equals(station, that.station) &&
                Objects.equals(distribute, that.distribute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, companyName, cityName, mobile, idNo, contactName, contactMobile, employment, star, station, distribute);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}