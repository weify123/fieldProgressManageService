package com.autoyol.field.progress.manage.request.store;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class StorePageReqVo extends BasePage {
    private static final long serialVersionUID = 9146109964685186592L;

    @AutoDocProperty(value = "门店id")
    private Integer storeId;

    @AutoDocProperty(value = "门店全称")
    private String storeFullName;

    @AutoDocProperty(value = "城市id")
    private Integer cityId;

    @AutoDocProperty(value = "是否服务:0,否;1,是;字典类型名[serve_type]")
    private Integer serveStatusKey;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreFullName() {
        return storeFullName;
    }

    public void setStoreFullName(String storeFullName) {
        this.storeFullName = storeFullName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getServeStatusKey() {
        return serveStatusKey;
    }

    public void setServeStatusKey(Integer serveStatusKey) {
        this.serveStatusKey = serveStatusKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
