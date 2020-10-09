package com.autoyol.field.progress.manage.request;

import com.autoyol.doc.annotation.AutoDocProperty;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public class AddCarServiceItemReqVO extends BaseRequest{
    private static final long serialVersionUID = 4757616439925390473L;
    /**
     * 服务分类：1保养 2美容 3养护 4清洗套餐 5年检 6车辆检测 7维修
     */
    @AutoDocProperty(value = "服务分类 1保养 2美容 3养护 4清洗套餐 5年检 6车辆检测 7维修;字典类型名[service_type]", required = true)
    @NotNull(message = "服务分类不能为空!")
    private Integer serviceTypeKey;

    /**
     * 服务产品名称
     */
    @AutoDocProperty(value = "服务产品名称",required = true)
    @NotBlank(message = "服务产品名称不能为空！")
    @Length(max = 20, message = "服务产品名称不能超过20位")
    private String serviceProductName;

    /**
     * 门店挂牌价格
     */
    @AutoDocProperty(value = "门店挂牌价格",required = true)
    @NotNull(message = "门店挂牌价格不能为空！")
    @Max(value = 9999999999L, message = "门店挂牌价格超过最大值")
    private BigDecimal storePrice;

    /**
     * 凹凸销售价格
     */
    @AutoDocProperty(value = "凹凸销售价格",required = true)
    @NotNull(message = "凹凸销售价格不能为空！")
    @Max(value = 9999999999L, message = "凹凸销售价格超过最大值")
    private BigDecimal aotuPrice;

    /**
     * 服务时长
     */
    @AutoDocProperty(value = "服务时长",required = true)
    @NotBlank(message = "服务时长不能为空！")
    @Length(max = 20, message = "服务时长不能超过20位")
    @Pattern(regexp = "^\\d*\\.{0,1}\\d{0,1}$", message = "服务时长格式错误")
    private String serviceTime;

    /**
     * 适用的车型
     */
    @AutoDocProperty(value = "适用的车型")
    @Length(max = 20, message = "适用的车型不能超过20位")
    private String applicableModel;

    /**
     * 是否有效
     */
    @AutoDocProperty(value = "是否有效 0是 1否;字典类型名[effective_type]",required = true)
    @NotNull(message = "是否有效不能为空！")
    private Integer isEffectiveKey;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
