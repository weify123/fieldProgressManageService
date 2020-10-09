package com.autoyol.field.progress.manage.request.store;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public class StoreAddReqVo extends BaseRequest {
    private static final long serialVersionUID = -8043557074881653894L;

    /**
     * 门店全称
     */
    @AutoDocProperty(value = "门店全称")
    @NotBlank(message = "门店全称不能为空")
    @Length(max = 20, message = "门店全称最大长度为20")
    private String storeFullName;

    /**
     * 门店简称
     */
    @AutoDocProperty(value = "门店简称")
    @NotBlank(message = "门店简称不能为空")
    @Length(max = 10, message = "门店简称最大长度为10")
    private String storeName;

    /**
     * 分公司
     */
    @AutoDocProperty(value = "分公司")
    @Length(max = 15, message = "分公司最大长度为15")
    private String branchCompany;

    /**
     * 城市id
     */
    @AutoDocProperty(value = "城市id")
    @NotNull(message = "cityId不能为空")
    private Integer cityId;

    /**
     * 区县
     */
    @AutoDocProperty(value = "区县")
    @NotBlank(message = "区县不能为空")
    @Length(max = 10, message = "区县最大长度为10")
    private String districtCounty;

    /**
     * 门店地址
     */
    @AutoDocProperty(value = "门店地址")
    @NotBlank(message = "门店地址不能为空")
    @Length(max = 100, message = "门店地址最长100位")
    private String storeAddress;

    /**
     * 门店经度
     */
    @AutoDocProperty(value = "门店经度")
    @NotNull(message = "门店经度不能为空")
    private BigDecimal longitude;

    /**
     * 门店纬度
     */
    @AutoDocProperty(value = "门店纬度")
    @NotNull(message = "门店纬度不能为空")
    private BigDecimal latitude;

    /**
     * 门店联系人
     */
    @AutoDocProperty(value = "门店联系人")
    @Length(max = 10, message = "门店联系人最大长度为10")
    private String storeContactName;

    /**
     * 门店联系电话
     */
    @AutoDocProperty(value = "门店联系电话")
    @NotBlank(message = "门店联系电话不能为空")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "请输入有效手机号")
    private String storeContactMobile;

    /**
     * 营业开始时间
     */
    @AutoDocProperty(value = "营业开始时间;格式HH:mm(如18:00)")
    @NotBlank(message = "营业开始时间不能为空")
    @Length(max = 5, message = "营业开始时间最长5位")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3])(?::[0-5]\\d)$", message = "营业开始时间格式错误")
    private String startTime;

    /**
     * 营业结束时间
     */
    @AutoDocProperty(value = "营业结束时间;格式HH:mm(如18:00)")
    @NotBlank(message = "营业结束时间不能为空")
    @Length(max = 5, message = "营业结束时间最长5位")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3])(?::[0-5]\\d)$", message = "营业结束时间格式错误")
    private String endTime;

    /**
     * 是否服务:0,否;1,是
     */
    @AutoDocProperty(value = "是否服务:0,1;字典类型名[serve_type]")
    @NotNull(message = "是否服务不能为空")
    private Integer serveStatusKey;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
