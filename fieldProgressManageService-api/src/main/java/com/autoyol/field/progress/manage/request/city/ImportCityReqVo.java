package com.autoyol.field.progress.manage.request.city;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Objects;

public class ImportCityReqVo implements Serializable {
    private static final long serialVersionUID = -7792614748080321581L;

    private Integer cityId;

    /**
     * 城市
     */
    @AutoDocProperty("城市")
    private String city;

    /**
     * 城市等级
     */
    @AutoDocProperty("城市等级")
    private Integer cityLevel;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(Integer cityLevel) {
        this.cityLevel = cityLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImportCityReqVo that = (ImportCityReqVo) o;
        return Objects.equals(city, that.city) &&
                Objects.equals(cityLevel, that.cityLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, cityLevel);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
