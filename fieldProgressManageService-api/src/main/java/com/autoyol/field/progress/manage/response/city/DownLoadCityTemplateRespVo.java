package com.autoyol.field.progress.manage.response.city;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.constraint.ExcelElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class DownLoadCityTemplateRespVo implements Serializable {
    private static final long serialVersionUID = -2965061529657139155L;
    /**
     * 城市
     */
    @AutoDocProperty("城市")
    @ExcelElement(field="城市",index=1)
    private String city;

    /**
     * 城市等级
     */
    @AutoDocProperty("城市等级")
    @ExcelElement(field="城市等级",index=2)
    private Integer cityLevel;

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
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
