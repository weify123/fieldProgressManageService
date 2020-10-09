package com.autoyol.field.progress.manage.response.location;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class LocateVo implements Serializable {
    private static final long serialVersionUID = -7035506920588167528L;

    @AutoDocProperty("人员id")
    private Integer userId;

    @AutoDocProperty("用户姓名")
    private String userName;

    @JsonIgnore
    private String firstLetter;

    /**
     * 定位状态:1,空闲;2,工作中;3,即将完成;4,未定位
     */
    @AutoDocProperty(value = "定位状态key:1,空闲;2,工作中;3,即将完成;4,未定位;字典类型名[locate_status]")
    private Integer locateStatusKey;

    @AutoDocProperty(value = "定位状态val:1,空闲;2,工作中;3,即将完成;4,未定位;字典类型名[locate_status]")
    private String locateStatusVal;

    @JsonIgnore
    private Integer sortDis;

    @AutoDocProperty("当前位置经度")
    private BigDecimal longitude;

    @AutoDocProperty("当前位置维度")
    private BigDecimal latitude;

    @AutoDocProperty("距离(km)")
    private String distance;

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

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public Integer getLocateStatusKey() {
        return locateStatusKey;
    }

    public void setLocateStatusKey(Integer locateStatusKey) {
        this.locateStatusKey = locateStatusKey;
    }

    public String getLocateStatusVal() {
        return locateStatusVal;
    }

    public void setLocateStatusVal(String locateStatusVal) {
        this.locateStatusVal = locateStatusVal;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Integer getSortDis() {
        return sortDis;
    }

    public void setSortDis(Integer sortDis) {
        this.sortDis = sortDis;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
