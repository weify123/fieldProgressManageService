package com.autoyol.field.progress.manage.response.city;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.constraint.ExcelElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class DownLoadOrderTemplateRespVo implements Serializable {
    private static final long serialVersionUID = -9186642278117377136L;
    /**
     * 城市
     */
    @AutoDocProperty("城市")
    @ExcelElement(field="城市",index=1)
    private String city;

    /**
     * 租金
     */
    @AutoDocProperty("租金")
    @ExcelElement(field="租金(大于等于)",index=2)
    private String rent;

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
