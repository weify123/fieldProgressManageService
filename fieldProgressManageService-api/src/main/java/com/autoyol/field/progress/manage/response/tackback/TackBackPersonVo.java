package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class TackBackPersonVo implements Serializable {
    private static final long serialVersionUID = 6345426587996946948L;

    @AutoDocProperty("人员id")
    private Integer userId;

    @AutoDocProperty("用户姓名")
    private String userName;

    @AutoDocProperty(value = "取送车人员电话")
    private String userPhone;

    @JsonIgnore
    private String firstLetter;

    @AutoDocProperty("服务公司id")
    private Integer companyId;

    @AutoDocProperty("服务公司")
    private String companyName;

    @AutoDocProperty("星级:一星,二星,三星,四星,五星;字典类型名[star_level]")
    private String starVal;

    @AutoDocProperty("居住地址")
    private String address;

    @AutoDocProperty("岗位:取送车人员,GPS工程师;字典类型名[station_type]")
    private String stationVal;

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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStarVal() {
        return starVal;
    }

    public void setStarVal(String starVal) {
        this.starVal = starVal;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStationVal() {
        return stationVal;
    }

    public void setStationVal(String stationVal) {
        this.stationVal = stationVal;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
