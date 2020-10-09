package com.autoyol.field.progress.manage.request.city;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class OrderUpdateReqVo extends BaseRequest {
    private static final long serialVersionUID = -1956919774561044474L;
    /**
     *
     */
    @AutoDocProperty(value = "城市id", required = true)
    @NotNull(message = "城市等级不能为空")
    private Integer cityId;

    @AutoDocProperty(value = "租金", required = true)
    @NotNull(message = "租金不能为空")
    @Pattern(regexp = "^[1-9]\\d{0,13}|0$", message = "租金格式错误")
    private String rent;

    @AutoDocProperty(value = "是否为新增;true:新增,false:修改", required = true)
    @NotNull(message = "是否为新增不能为空")
    private Boolean isAdd;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public Boolean getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(Boolean isAdd) {
        this.isAdd = isAdd;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
