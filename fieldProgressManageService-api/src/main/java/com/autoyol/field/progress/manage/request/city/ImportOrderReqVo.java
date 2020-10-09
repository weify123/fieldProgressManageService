package com.autoyol.field.progress.manage.request.city;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ImportOrderReqVo implements Serializable {
    private static final long serialVersionUID = -407973616100335332L;

    private Integer cityId;

    @AutoDocProperty("城市")
    private String city;

    @AutoDocProperty("租金")
    private BigDecimal rent;

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

    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImportOrderReqVo that = (ImportOrderReqVo) o;
        return Objects.equals(cityId, that.cityId) &&
                Objects.equals(city, that.city) &&
                Objects.equals(rent, that.rent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId, city, rent);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
