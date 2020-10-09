package com.autoyol.field.progress.manage.response;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class QueryCarServiceItemRespVO implements Serializable {
    private static final long serialVersionUID = 8577070848949447394L;

    /**
     * 服务id
     */
    @AutoDocProperty(value = "服务id")
    private Integer serviceId;

    /**
     * 服务分类：1保养 2美容 3养护 4清洗套餐 5年检 6车辆检测 7维修
     */
    @AutoDocProperty(value = "服务分类 0,1,2,3,4,5,6;字典类型名[service_type]")
    private Integer serviceTypeKey;

    @AutoDocProperty(value = "服务分类 保养,美容,养护,清洗套餐,年检,车辆检测,维修;字典类型名[service_type]")
    private String serviceTypeVal;

    /**
     * 服务产品名称
     */
    @AutoDocProperty(value = "服务产品名称")
    private String serviceProductName;

    /**
     * 门店挂牌价格
     */
    @AutoDocProperty(value = "MSRP（门店挂牌价格）")
    private BigDecimal storePrice;

    /**
     * 凹凸销售价格
     */
    @AutoDocProperty(value = "凹凸销售价格")
    private BigDecimal aotuPrice;

    /**
     * 服务时长
     */
    @AutoDocProperty(value = "服务时长")
    private String serviceTime;

    /**
     * 适用的车型
     */
    @AutoDocProperty(value = "适用的车型")
    private String applicableModel;

    /**
     * 是否有效
     */
    @AutoDocProperty(value = "是否有效;字典类型名[effective_type]")
    private Integer isEffectiveKey;

    @AutoDocProperty(value = "是否有效;字典类型名[effective_type]")
    private String isEffectiveVal;

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

    public String getServiceTypeVal() {
        return serviceTypeVal;
    }

    public void setServiceTypeVal(String serviceTypeVal) {
        this.serviceTypeVal = serviceTypeVal;
    }

    public String getServiceProductName() {
        return serviceProductName;
    }

    public void setServiceProductName(String serviceProductName) {
        this.serviceProductName = serviceProductName;
    }

    public BigDecimal getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(BigDecimal storePrice) {
        this.storePrice = storePrice;
    }

    public BigDecimal getAotuPrice() {
        return aotuPrice;
    }

    public void setAotuPrice(BigDecimal aotuPrice) {
        this.aotuPrice = aotuPrice;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getApplicableModel() {
        return applicableModel;
    }

    public void setApplicableModel(String applicableModel) {
        this.applicableModel = applicableModel;
    }

    public Integer getIsEffectiveKey() {
        return isEffectiveKey;
    }

    public void setIsEffectiveKey(Integer isEffectiveKey) {
        this.isEffectiveKey = isEffectiveKey;
    }

    public String getIsEffectiveVal() {
        return isEffectiveVal;
    }

    public void setIsEffectiveVal(String isEffectiveVal) {
        this.isEffectiveVal = isEffectiveVal;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
