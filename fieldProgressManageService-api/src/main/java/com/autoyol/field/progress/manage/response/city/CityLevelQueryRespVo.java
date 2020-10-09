package com.autoyol.field.progress.manage.response.city;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class CityLevelQueryRespVo implements Serializable {
    private static final long serialVersionUID = 4348813828588955140L;
    /**
     *
     */
    @AutoDocProperty("城市id")
    private Integer cityId;

    /**
     * 城市
     */
    @AutoDocProperty("城市")
    private String city;

    /**
     * 城市等级
     */
    @AutoDocProperty("城市等级key:0,1,2,3,4;字典类型名[city_level_type]")
    private Integer cityLevelKey;

    @AutoDocProperty("城市等级val:1,2,3,4,5")
    private String cityLevelVal;

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

    public Integer getCityLevelKey() {
        return cityLevelKey;
    }

    public void setCityLevelKey(Integer cityLevelKey) {
        this.cityLevelKey = cityLevelKey;
    }

    public String getCityLevelVal() {
        return cityLevelVal;
    }

    public void setCityLevelVal(String cityLevelVal) {
        this.cityLevelVal = cityLevelVal;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
