package com.autoyol.field.progress.manage.response.store;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.constraint.ExcelElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class StoreExportRespVo implements Serializable {
    private static final long serialVersionUID = 487375690690242618L;

    /**
     *
     */
    @AutoDocProperty(value = "门店id")
    @ExcelElement(field="门店id",index=1)
    private Integer id;

    /**
     * 门店全称
     */
    @AutoDocProperty(value = "门店全称")
    @ExcelElement(field="门店全称",index=3)
    private String storeFullName;

    /**
     * 门店简称
     */
    @AutoDocProperty(value = "门店简称")
    @ExcelElement(field="门店简称",index=2)
    private String storeName;

    /**
     * 分公司
     */
    @AutoDocProperty(value = "分公司")
    @ExcelElement(field="分公司",index=4)
    private String branchCompany;

    /**
     * 城市
     */
    @AutoDocProperty(value = "城市")
    @ExcelElement(field="城市",index=5)
    private String city;

    /**
     * 城市
     */
    @AutoDocProperty(value = "城市等級")
    @ExcelElement(field="城市等級",index=6)
    private String cityLevel;

    /**
     * 区县
     */
    @AutoDocProperty(value = "区县")
    @ExcelElement(field="区县",index=7)
    private String districtCounty;

    /**
     * 门店地址
     */
    @AutoDocProperty(value = "门店地址")
    @ExcelElement(field="门店地址",index=8)
    private String storeAddress;

    /**
     * 门店经度
     */
    @AutoDocProperty(value = "门店经度")
    @ExcelElement(field="门店经度",index=9)
    private BigDecimal longitude;

    /**
     * 门店纬度
     */
    @AutoDocProperty(value = "门店纬度")
    @ExcelElement(field="门店纬度",index=10)
    private BigDecimal latitude;

    /**
     * 门店联系人
     */
    @AutoDocProperty(value = "门店联系人")
    @ExcelElement(field="门店联系人",index=11)
    private String storeContactName;

    /**
     * 门店联系电话
     */
    @AutoDocProperty(value = "门店联系电话")
    @ExcelElement(field="门店联系电话",index=12)
    private String storeContactMobile;

    /**
     * 营业开始时间
     */
    @AutoDocProperty(value = "营业开始时间")
    @ExcelElement(field="营业开始时间",index=13)
    private String startTime;

    /**
     * 营业结束时间
     */
    @AutoDocProperty(value = "营业结束时间")
    @ExcelElement(field="营业结束时间",index=14)
    private String endTime;

    /**
     * 是否服务:0,否;1,是
     */
    @AutoDocProperty(value = "是否服务:否,是")
    @ExcelElement(field="是否服务",index=15)
    private String serveStatus;

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

    public String getServeStatus() {
        return serveStatus;
    }

    public void setServeStatus(String serveStatus) {
        this.serveStatus = serveStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
