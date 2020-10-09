package com.autoyol.field.progress.manage.request.car;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class QueryCarServiceItemReqVo extends BasePage {
    private static final long serialVersionUID = -1112028832118416040L;
    /**
     * 服务id
     */
    @AutoDocProperty(value = "服务id")
    private Integer serviceId;
    /**
     * 服务分类：1保养 2美容 3养护 4清洗套餐 5年检 6车辆检测 7维修
     */
    @AutoDocProperty(value = "服务分类;字典类型名[service_type]")
    private Integer serviceTypeKey;
    /**
     * 服务产品名称
     */
    @AutoDocProperty(value = "服务产品名称")
    private String serviceProductName;
    /**
     * 是否有效
     */
    @AutoDocProperty(value = "是否有效;字典类型名[effective_type]")
    private Integer isEffectiveKey;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getServiceTypeKey() {
        return serviceTypeKey;
    }

    public void setServiceTypeKey(Integer serviceTypeKey) {
        this.serviceTypeKey = serviceTypeKey;
    }

    public String getServiceProductName() {
        return serviceProductName;
    }

    public void setServiceProductName(String serviceProductName) {
        this.serviceProductName = serviceProductName;
    }

    public Integer getIsEffectiveKey() {
        return isEffectiveKey;
    }

    public void setIsEffectiveKey(Integer isEffectiveKey) {
        this.isEffectiveKey = isEffectiveKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
