package com.autoyol.field.progress.manage.response.userinfo;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.response.user.AppUserVo;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author feiyu.wei
 * @date 2019/12/13
 */
public class AppUserInfoVo extends AppUserVo {
    private static final long serialVersionUID = 5215316343903408368L;

    /**
     * 联系人姓名
     */
    @AutoDocProperty("联系人姓名")
    private String contactName;

    /**
     * 联系人手机
     */
    @AutoDocProperty("联系人手机")
    private String contactMobile;

    @AutoDocProperty("身份证号")
    private String idNo;

    /**
     * 如果根据经纬度可查地址，该字段可删除
     */
    @AutoDocProperty("如果根据经纬度可查地址，该字段可删除")
    private String address;

    /**
     * 经度
     */
    @AutoDocProperty("经度")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @AutoDocProperty("纬度")
    private BigDecimal latitude;

    /**
     * 任职情况:0,专职;1,兼职
     */
    @AutoDocProperty("任职情况:0,1;字典类型名[employ_type]")
    private Integer employmentKey;
    @AutoDocProperty("任职情况:专职,兼职;字典类型名[employ_type]")
    private String employmentVal;

    /**
     * 星级:1,一星;2,二星;3,三星;4,四星;5,五星;
     */
    @AutoDocProperty("星级:1,2,3,4,5;字典类型名[star_level]")
    private Integer starKey;
    @AutoDocProperty("星级:一星,二星,三星,四星,五星;字典类型名[star_level]")
    private String starVal;

    /**
     * 岗位:0,取送车人员;1,GPS工程师;
     */
    @AutoDocProperty("岗位:0,1;字典类型名[station_type]")
    private String stationKey;
    @AutoDocProperty("岗位:取送车人员,GPS工程师;字典类型名[station_type]")
    private String stationVal;

    /**
     * 邮箱
     */
    @AutoDocProperty("邮箱")
    private String email;

    /**
     * 能否派单:1,能;0,否
     */
    @AutoDocProperty("能否派单:0,1;字典类型名[distribute_type]")
    private Integer distributeKey;
    @AutoDocProperty("岗位:能,否;字典类型名[distribute_type]")
    private String distributeVal;

    /**
     *
     */
    private String createOp;

    /**
     *
     */
    private Date createTime;

    @AutoDocProperty("创建时间, 格式化后的")
    private String createTimeStr;

    /**
     *
     */
    private String updateOp;

    /**
     *
     */
    private Date updateTime;

    @AutoDocProperty("修改时间, 格式化后的")
    private String updateTimeStr;

    @AutoDocProperty("头像路径")
    private String avatarPath;

    @AutoDocProperty("身份证正面路径")
    private String idCardFrontPath;

    @AutoDocProperty("身份证反面路径")
    private String idCardBackPath;

    @AutoDocProperty("驾驶证正面路径")
    private String driverFrontPath;

    @AutoDocProperty("驾驶证反面路径")
    private String driverBackPath;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getEmploymentKey() {
        return employmentKey;
    }

    public void setEmploymentKey(Integer employmentKey) {
        this.employmentKey = employmentKey;
    }

    public String getEmploymentVal() {
        return employmentVal;
    }

    public void setEmploymentVal(String employmentVal) {
        this.employmentVal = employmentVal;
    }

    public Integer getStarKey() {
        return starKey;
    }

    public void setStarKey(Integer starKey) {
        this.starKey = starKey;
    }

    public String getStarVal() {
        return starVal;
    }

    public void setStarVal(String starVal) {
        this.starVal = starVal;
    }

    public String getStationKey() {
        return stationKey;
    }

    public void setStationKey(String stationKey) {
        this.stationKey = stationKey;
    }

    public String getStationVal() {
        return stationVal;
    }

    public void setStationVal(String stationVal) {
        this.stationVal = stationVal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getDistributeKey() {
        return distributeKey;
    }

    public void setDistributeKey(Integer distributeKey) {
        this.distributeKey = distributeKey;
    }

    public String getDistributeVal() {
        return distributeVal;
    }

    public void setDistributeVal(String distributeVal) {
        this.distributeVal = distributeVal;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getIdCardFrontPath() {
        return idCardFrontPath;
    }

    public void setIdCardFrontPath(String idCardFrontPath) {
        this.idCardFrontPath = idCardFrontPath;
    }

    public String getIdCardBackPath() {
        return idCardBackPath;
    }

    public void setIdCardBackPath(String idCardBackPath) {
        this.idCardBackPath = idCardBackPath;
    }

    public String getDriverFrontPath() {
        return driverFrontPath;
    }

    public void setDriverFrontPath(String driverFrontPath) {
        this.driverFrontPath = driverFrontPath;
    }

    public String getDriverBackPath() {
        return driverBackPath;
    }

    public void setDriverBackPath(String driverBackPath) {
        this.driverBackPath = driverBackPath;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}