package com.autoyol.field.progress.manage.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2019/12/28
 */
public class StoreInfoEntity {
    /**
     * 
     */
    private Integer id;

    /**
     * 门店全称
     */
    private String storeFullName;

    /**
     * 门店简称
     */
    private String storeName;

    /**
     * 分公司
     */
    private String branchCompany;

    /**
     * 城市id
     */
    private Integer cityId;

    /**
     * 区县
     */
    private String districtCounty;

    /**
     * 门店地址
     */
    private String storeAddress;

    /**
     * 门店经度
     */
    private BigDecimal longitude;

    /**
     * 门店纬度
     */
    private BigDecimal latitude;

    /**
     * 门店联系人
     */
    private String storeContactName;

    /**
     * 门店联系电话
     */
    private String storeContactMobile;

    /**
     * 门店开始时间
     */
    private String startTime;

    /**
     * 营业结束时间
     */
    private String endTime;

    /**
     * 是否服务:0,否;1,是
     */
    private Integer serveStatus;

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
        this.storeFullName = storeFullName == null ? null : storeFullName.trim();
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

    public String getBranchCompany() {
        return branchCompany;
    }

    public void setBranchCompany(String branchCompany) {
        this.branchCompany = branchCompany == null ? null : branchCompany.trim();
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getDistrictCounty() {
        return districtCounty;
    }

    public void setDistrictCounty(String districtCounty) {
        this.districtCounty = districtCounty == null ? null : districtCounty.trim();
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress == null ? null : storeAddress.trim();
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
        this.storeContactName = storeContactName == null ? null : storeContactName.trim();
    }

    public String getStoreContactMobile() {
        return storeContactMobile;
    }

    public void setStoreContactMobile(String storeContactMobile) {
        this.storeContactMobile = storeContactMobile == null ? null : storeContactMobile.trim();
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public Integer getServeStatus() {
        return serveStatus;
    }

    public void setServeStatus(Integer serveStatus) {
        this.serveStatus = serveStatus;
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