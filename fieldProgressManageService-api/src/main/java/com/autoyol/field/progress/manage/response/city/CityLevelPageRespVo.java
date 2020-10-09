package com.autoyol.field.progress.manage.response.city;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class CityLevelPageRespVo extends PageResult {
    private static final long serialVersionUID = -3643428054194503436L;

    @AutoDocProperty("城市列表")
    private List<CityLevelVo> cityList;

    public List<CityLevelVo> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityLevelVo> cityList) {
        this.cityList = cityList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
