package com.autoyol.field.progress.manage.request.city;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CityLevelUpdateReqVo extends BaseRequest {
    private static final long serialVersionUID = -7375095046382385281L;

    /**
     *
     */
    @AutoDocProperty(value = "城市id", required = true)
    @NotNull(message = "城市等级不能为空")
    private Integer cityId;

    /**
     * 城市等级
     */
    @AutoDocProperty(value = "城市等级;字典类型名[city_level_type]", required = true)
    @NotNull(message = "城市等级不能为空")
    private Integer cityLevelKey;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getCityLevelKey() {
        return cityLevelKey;
    }

    public void setCityLevelKey(Integer cityLevelKey) {
        this.cityLevelKey = cityLevelKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
