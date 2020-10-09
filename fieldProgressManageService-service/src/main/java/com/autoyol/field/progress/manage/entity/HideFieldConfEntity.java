package com.autoyol.field.progress.manage.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2020/04/28
 */
public class HideFieldConfEntity {
    /**
     * 
     */
    private Integer id;

    /**
     * 服务分类(3:取车服务,4:还车服务)
     */
    private Integer serviceType;

    /**
     * 业务类型
     */
    private String moduleType;

    /**
     * 
     */
    private Date createTime;

    /**
     * 隐藏字段
     */
    private String hideField;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType == null ? null : moduleType.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getHideField() {
        return hideField;
    }

    public void setHideField(String hideField) {
        this.hideField = hideField == null ? null : hideField.trim();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}