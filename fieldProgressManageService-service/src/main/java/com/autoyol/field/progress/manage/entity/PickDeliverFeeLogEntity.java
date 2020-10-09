package com.autoyol.field.progress.manage.entity;

import java.util.Date;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2020/02/25
 */
public class PickDeliverFeeLogEntity {
    /**
     * 
     */
    private Integer id;

    /**
     * 取送车订单号
     */
    private String pickDeliverOrderNo;

    /**
     * 服务分类(3:取车服务,4:还车服务)
     */
    private Integer serviceType;

    /**
     * 承担方(0：平台、1：租客、2：车主:3：三方)
     */
    private Integer bearType;

    /**
     * 车主补贴原因
     */
    private String allowanceOwnerReasonType;

    /**
     * 租客补贴原因
     */
    private String allowanceTenantReasonType;

    /**
     * 给车主调价理由(0：油费补价、1：提前还车、2：车损补价:3：超出限定公里:4：油费和超里程费:5：其他)
     */
    private Integer adjustPriceOwnerReasonType;

    /**
     * 给租客调价理由(0：多油返还、1：租金返还:2：洗车费用)
     */
    private Integer adjustPriceTenantReasonType;


    /**
     * 是否删除:0:正常;1,已删除
     */
    private Integer isDelete;

    /**
     * 
     */
    private String createOp;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private String updateOp;

    /**
     * 
     */
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPickDeliverOrderNo() {
        return pickDeliverOrderNo;
    }

    public void setPickDeliverOrderNo(String pickDeliverOrderNo) {
        this.pickDeliverOrderNo = pickDeliverOrderNo == null ? null : pickDeliverOrderNo.trim();
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getBearType() {
        return bearType;
    }

    public void setBearType(Integer bearType) {
        this.bearType = bearType;
    }

    public String getAllowanceOwnerReasonType() {
        return allowanceOwnerReasonType;
    }

    public void setAllowanceOwnerReasonType(String allowanceOwnerReasonType) {
        this.allowanceOwnerReasonType = allowanceOwnerReasonType;
    }

    public String getAllowanceTenantReasonType() {
        return allowanceTenantReasonType;
    }

    public void setAllowanceTenantReasonType(String allowanceTenantReasonType) {
        this.allowanceTenantReasonType = allowanceTenantReasonType;
    }

    public Integer getAdjustPriceOwnerReasonType() {
        return adjustPriceOwnerReasonType;
    }

    public void setAdjustPriceOwnerReasonType(Integer adjustPriceOwnerReasonType) {
        this.adjustPriceOwnerReasonType = adjustPriceOwnerReasonType;
    }

    public Integer getAdjustPriceTenantReasonType() {
        return adjustPriceTenantReasonType;
    }

    public void setAdjustPriceTenantReasonType(Integer adjustPriceTenantReasonType) {
        this.adjustPriceTenantReasonType = adjustPriceTenantReasonType;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateOp() {
        return createOp;
    }

    public void setCreateOp(String createOp) {
        this.createOp = createOp == null ? null : createOp.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateOp() {
        return updateOp;
    }

    public void setUpdateOp(String updateOp) {
        this.updateOp = updateOp == null ? null : updateOp.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}