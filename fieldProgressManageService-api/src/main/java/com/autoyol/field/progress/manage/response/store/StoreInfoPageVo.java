package com.autoyol.field.progress.manage.response.store;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class StoreInfoPageVo implements Serializable {
    private static final long serialVersionUID = 941308260338583378L;
    /**
     *
     */
    @AutoDocProperty(value = "门店id")
    private Integer id;

    /**
     * 门店全称
     */
    @AutoDocProperty(value = "门店全称")
    private String storeFullName;

    /**
     * 门店简称
     */
    @AutoDocProperty(value = "门店简称")
    private String storeName;

    /**
     * 分公司
     */
    @AutoDocProperty(value = "分公司")
    private String branchCompany;

    /**
     * 城市id
     */
    @AutoDocProperty(value = "城市id")
    private Integer cityId;

    /**
     * 城市
     */
    @AutoDocProperty(value = "城市")
    private String city;

    /**
     * 城市
     */
    @AutoDocProperty(value = "城市等級")
    private String cityLevel;

    /**
     * 区县
     */
    @AutoDocProperty(value = "区县")
    private String districtCounty;

    /**
     * 门店地址
     */
    @AutoDocProperty(value = "门店地址")
    private String storeAddress;

    /**
     * 门店经度
     */
    @AutoDocProperty(value = "门店经度")
    private BigDecimal longitude;

    /**
     * 门店纬度
     */
    @AutoDocProperty(value = "门店纬度")
    private BigDecimal latitude;

    /**
     * 门店联系人
     */
    @AutoDocProperty(value = "门店联系人")
    private String storeContactName;

    /**
     * 门店联系电话
     */
    @AutoDocProperty(value = "门店联系电话")
    private String storeContactMobile;

    /**
     * 营业开始时间
     */
    @AutoDocProperty(value = "营业开始时间")
    private String startTime;

    /**
     * 营业结束时间
     */
    @AutoDocProperty(value = "营业结束时间")
    private String endTime;

    /**
     * 是否服务:0,否;1,是
     */
    @AutoDocProperty(value = "是否服务:0,1;字典类型名[serve_type]")
    private Integer serveStatusKey;

    @AutoDocProperty(value = "是否服务:否,是;字典类型名[serve_type]")
    private String serveStatusVal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoreFullName() {
        return storeFullName;
    }

    public void setStoreFullName(String storeFullName) {
        this.storeFullName = storeFullName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBranchCompany() {
        return branchCompany;
    }

    public void setBranchCompany(String branchCompany) {
        this.branchCompany = branchCompany;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(String cityLevel) {
        this.cityLevel = cityLevel;
    }

    public String getDistrictCounty() {
        return districtCounty;
    }

    public void setDistrictCounty(String districtCounty) {
        this.districtCounty = districtCounty;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
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

    public String getStoreContactName() {
        return storeContactName;
    }

    public void setStoreContactName(String storeContactName) {
        this.storeContactName = storeContactName;
    }

    public String getStoreContactMobile() {
        return storeContactMobile;
    }

    public void setStoreContactMobile(String storeContactMobile) {
        this.storeContactMobile = storeContactMobile;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getServeStatusKey() {
        return serveStatusKey;
    }

    public void setServeStatusKey(Integer serveStatusKey) {
        this.serveStatusKey = serveStatusKey;
    }

    public String getServeStatusVal() {
        return serveStatusVal;
    }

    public void setServeStatusVal(String serveStatusVal) {
        this.serveStatusVal = serveStatusVal;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
