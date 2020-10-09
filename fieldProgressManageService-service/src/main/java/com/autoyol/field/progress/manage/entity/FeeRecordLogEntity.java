package com.autoyol.field.progress.manage.entity;

import java.math.BigDecimal;
import java.util.Date;

public class FeeRecordLogEntity {

    private Integer id;
    /**
     * 取送车人员费用id
     */
    private Integer pickDeliverFeeId;

    /**
     * 费用标签id
     */
    private Integer feeLabelId;

    /**
     * 报销类型
     */
    private String expenseType;

    /**
     * 费用类型:(报销类型不同，选项不同，具体参考产品rp文档)
     */
    private Integer feeType;

    /**
     * 费用code
     */
    private String attrCode;

    /**
     * 费用
     */
    private BigDecimal feeValue;

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

    public Integer getPickDeliverFeeId() {
        return pickDeliverFeeId;
    }

    public void setPickDeliverFeeId(Integer pickDeliverFeeId) {
        this.pickDeliverFeeId = pickDeliverFeeId;
    }

    public Integer getFeeLabelId() {
        return feeLabelId;
    }

    public void setFeeLabelId(Integer feeLabelId) {
        this.feeLabelId = feeLabelId;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public Integer getFeeType() {
        return feeType;
    }

    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    public String getAttrCode() {
        return attrCode;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    public BigDecimal getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(BigDecimal feeValue) {
        this.feeValue = feeValue;
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
