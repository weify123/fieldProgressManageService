package com.autoyol.field.progress.manage.entity;

import java.util.Date;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2020/02/25
 */
public class TransRentUserInfoEntity {

    private Integer id;
    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 车主会员号
     */
    private String ownerNo;

    /**
     * 租客会员号
     */
    private String renterNo;

    /**
     * 车主姓名
     */
    private String ownerName;

    /**
     * 租客姓名
     */
    private String renterName;

    /**
     * 车主电话
     */
    private String ownerPhone;

    /**
     * 租客电话
     */
    private String renterPhone;

    /**
     * 车主等级
     */
    private String ownerLevel;

    /**
     * 租客等级
     */
    private String renterLevel;

    /**
     * 车主标签
     */
    private String ownerLabel;

    /**
     * 租客标签
     */
    private String renterLabel;

    /**
     * 会员标识
     */
    private String memFlag;

    /**
     * 租客使用取还车次数
     */
    private String renterUseTakeBackTime;

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo == null ? null : ownerNo.trim();
    }

    public String getRenterNo() {
        return renterNo;
    }

    public void setRenterNo(String renterNo) {
        this.renterNo = renterNo == null ? null : renterNo.trim();
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName == null ? null : ownerName.trim();
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterName(String renterName) {
        this.renterName = renterName == null ? null : renterName.trim();
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone == null ? null : ownerPhone.trim();
    }

    public String getRenterPhone() {
        return renterPhone;
    }

    public void setRenterPhone(String renterPhone) {
        this.renterPhone = renterPhone == null ? null : renterPhone.trim();
    }

    public String getOwnerLevel() {
        return ownerLevel;
    }

    public void setOwnerLevel(String ownerLevel) {
        this.ownerLevel = ownerLevel == null ? null : ownerLevel.trim();
    }

    public String getRenterLevel() {
        return renterLevel;
    }

    public void setRenterLevel(String renterLevel) {
        this.renterLevel = renterLevel == null ? null : renterLevel.trim();
    }

    public String getOwnerLabel() {
        return ownerLabel;
    }

    public void setOwnerLabel(String ownerLabel) {
        this.ownerLabel = ownerLabel == null ? null : ownerLabel.trim();
    }

    public String getRenterLabel() {
        return renterLabel;
    }

    public void setRenterLabel(String renterLabel) {
        this.renterLabel = renterLabel == null ? null : renterLabel.trim();
    }

    public String getMemFlag() {
        return memFlag;
    }

    public void setMemFlag(String memFlag) {
        this.memFlag = memFlag == null ? null : memFlag.trim();
    }

    public String getRenterUseTakeBackTime() {
        return renterUseTakeBackTime;
    }

    public void setRenterUseTakeBackTime(String renterUseTakeBackTime) {
        this.renterUseTakeBackTime = renterUseTakeBackTime == null ? null : renterUseTakeBackTime.trim();
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