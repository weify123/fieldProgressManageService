package com.autoyol.field.progress.manage.response.city;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.constraint.ExcelElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class OrderSetVo implements Serializable {
    private static final long serialVersionUID = -4716143914478878265L;

    /**
     *
     */
    @AutoDocProperty("城市id")
    @ExcelElement(field = "城市id", index = 1)
    private Integer cityId;

    /**
     * 城市
     */
    @AutoDocProperty("城市")
    @ExcelElement(field = "城市", index = 2)
    private String city;

    /**
     * 租金
     */
    @AutoDocProperty("租金(大于等于)")
    @ExcelElement(field = "租金(大于等于)", index = 3)
    private String rent;

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

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
