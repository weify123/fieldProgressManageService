package com.autoyol.field.progress.manage.request.location;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class LocatePageReqVo extends BasePage {
    private static final long serialVersionUID = -1703036693882414077L;

    @AutoDocProperty("城市id")
    @NotNull(message = "城市id不能为空")
    private Integer cityId;

    @AutoDocProperty("公司id列表,多个使用[,]分隔")
    @NotBlank(message = "公司id列表不能为空")
    private String companyIdStr;

    /**
     * 定位状态:1,空闲;2,工作中;3,即将完成;4,未定位
     */
    @AutoDocProperty(value = "定位状态key:1,空闲;2,工作中;3,即将完成;4,未定位;字典类型名[locate_status]")
    private Integer locateStatusKey;

    @AutoDocProperty(value = "地址经度")
    private BigDecimal longitude;

    @AutoDocProperty(value = "地址纬度")
    private BigDecimal latitude;

    @AutoDocProperty(value = "定位排序key:0,按当前位置排序;1,按送达位置排序;[locate_sort]")
    private Integer sortKey;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCompanyIdStr() {
        return companyIdStr;
    }

    public void setCompanyIdStr(String companyIdStr) {
        this.companyIdStr = companyIdStr;
    }

    public Integer getLocateStatusKey() {
        return locateStatusKey;
    }

    public void setLocateStatusKey(Integer locateStatusKey) {
        this.locateStatusKey = locateStatusKey;
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

    public Integer getSortKey() {
        return sortKey;
    }

    public void setSortKey(Integer sortKey) {
        this.sortKey = sortKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
