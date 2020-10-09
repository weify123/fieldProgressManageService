package com.autoyol.field.progress.manage.response.city;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class CityPrefixVo implements Serializable {
    private static final long serialVersionUID = 5094291152079048945L;

    /**
     * 城市首字符
     */
    @AutoDocProperty("城市首字符")
    private String cityPrefix;

    public CityPrefixVo() {
    }

    public CityPrefixVo(String cityPrefix) {
        this.cityPrefix = cityPrefix;
    }

    public String getCityPrefix() {
        return cityPrefix;
    }

    public void setCityPrefix(String cityPrefix) {
        this.cityPrefix = cityPrefix;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
