package com.autoyol.field.progress.manage.response.store;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Objects;

public class ImportUpdateVo implements Serializable {
    private static final long serialVersionUID = -2318404592195695451L;

    private Integer id;
    /**
     * 门店简称
     */
    private String storeName;
    /**
     * 门店全称
     */
    private String storeFullName;
    /**
     * 分公司
     */
    private String branchCompany;
    /**
     * 区县
     */
    private String districtCounty;
    /**
     * 门店地址
     */
    private String storeAddress;
    /**
     * 门店联系人
     */
    private String storeContactName;
    /**
     * 门店联系电话
     */
    private String storeContactMobile;
    /**
     * 营业开始时间
     */
    private String startTime;
    /**
     * 营业结束时间
     */
    private String endTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreFullName() {
        return storeFullName;
    }

    public void setStoreFullName(String storeFullName) {
        this.storeFullName = storeFullName;
    }

    public String getBranchCompany() {
        return branchCompany;
    }

    public void setBranchCompany(String branchCompany) {
        this.branchCompany = branchCompany;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImportUpdateVo that = (ImportUpdateVo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(storeName, that.storeName) &&
                Objects.equals(storeFullName, that.storeFullName) &&
                Objects.equals(branchCompany, that.branchCompany) &&
                Objects.equals(districtCounty, that.districtCounty) &&
                Objects.equals(storeAddress, that.storeAddress) &&
                Objects.equals(storeContactName, that.storeContactName) &&
                Objects.equals(storeContactMobile, that.storeContactMobile) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storeName, storeFullName, branchCompany, districtCounty, storeAddress, storeContactName, storeContactMobile, startTime, endTime);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
