package com.autoyol.field.progress.manage.response;

import com.autoyol.field.progress.manage.constraint.ExcelElement;

import java.io.Serializable;
import java.math.BigDecimal;

public class QueryCarServiceItemExcelVO implements Serializable {

    /**
     * 服务id
     */
    @ExcelElement(field="服务id",index=1)
    private Integer serviceId;

    /**
     * 服务分类：1保养 2美容 3养护 4清洗套餐 5年检 6车辆检测 7维修
     */
    @ExcelElement(field="服务分类",index=2)
    private String serviceType;

    /**
     * 服务产品名称
     */
    @ExcelElement(field="服务产品名称",index=3)
    private String serviceProductName;

    /**
     * 门店挂牌价格
     */
    @ExcelElement(field="MSRP（门店挂牌价格）",index=4)
    private BigDecimal storePrice;

    /**
     * 凹凸销售价格
     */
    @ExcelElement(field="凹凸销售价格",index=5)
    private BigDecimal aotuPrice;

    /**
     * 服务时长
     */
    @ExcelElement(field="服务时长",index=6)
    private String serviceTime;

    /**
     * 适用的车型
     */

    @ExcelElement(field="适用的车型",index=7)
    private String applicableModel;

    /**
     * 是否有效
     */
    @ExcelElement(field="是否有效",index=8)
    private String isEffective;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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

    public String getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(String isEffective) {
        this.isEffective = isEffective;
    }
}
