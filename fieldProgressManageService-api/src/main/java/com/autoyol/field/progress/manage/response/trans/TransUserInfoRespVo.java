package com.autoyol.field.progress.manage.response.trans;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class TransUserInfoRespVo implements Serializable {
    private static final long serialVersionUID = 9103167048530572645L;

    @AutoDocProperty(value = "订单号")
    private String orderNo;

    @AutoDocProperty(value = "车主会员号")
    private String ownerNo;

    @AutoDocProperty(value = "租客会员号")
    private String renterNo;

    @AutoDocProperty(value = "车主姓名")
    private String ownerName;

    @AutoDocProperty(value = "租客姓名")
    private String renterName;

    @AutoDocProperty(value = "车主电话")
    private String ownerPhone;

    @AutoDocProperty(value = "租客电话")
    private String renterPhone;

    @AutoDocProperty(value = "车主等级")
    private String ownerLevel;

    @AutoDocProperty(value = "租客等级")
    private String renterLevel;

    @AutoDocProperty(value = "车主标签")
    private String ownerLabel;

    @AutoDocProperty(value = "租客标签")
    private String renterLabel;

    @AutoDocProperty(value = "会员标识")
    private String memFlag;

    @AutoDocProperty(value = "租客使用取还车次数")
    private String renterUseTakeBackTime;

    @AutoDocProperty(value = "维修店名称/联系人")
    private String repairShopContact;

    @AutoDocProperty(value = "维修店联系人电话")
    private String repairShopPhone;

    @AutoDocProperty(value = "门店名称")
    private String storeName;

    @AutoDocProperty(value = "门店电话")
    private String storePhone;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getRenterNo() {
        return renterNo;
    }

    public void setRenterNo(String renterNo) {
        this.renterNo = renterNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterName(String renterName) {
        this.renterName = renterName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getRenterPhone() {
        return renterPhone;
    }

    public void setRenterPhone(String renterPhone) {
        this.renterPhone = renterPhone;
    }

    public String getOwnerLevel() {
        return ownerLevel;
    }

    public void setOwnerLevel(String ownerLevel) {
        this.ownerLevel = ownerLevel;
    }

    public String getRenterLevel() {
        return renterLevel;
    }

    public void setRenterLevel(String renterLevel) {
        this.renterLevel = renterLevel;
    }

    public String getOwnerLabel() {
        return ownerLabel;
    }

    public void setOwnerLabel(String ownerLabel) {
        this.ownerLabel = ownerLabel;
    }

    public String getRenterLabel() {
        return renterLabel;
    }

    public void setRenterLabel(String renterLabel) {
        this.renterLabel = renterLabel;
    }

    public String getMemFlag() {
        return memFlag;
    }

    public void setMemFlag(String memFlag) {
        this.memFlag = memFlag;
    }

    public String getRenterUseTakeBackTime() {
        return renterUseTakeBackTime;
    }

    public void setRenterUseTakeBackTime(String renterUseTakeBackTime) {
        this.renterUseTakeBackTime = renterUseTakeBackTime;
    }

    public String getRepairShopContact() {
        return repairShopContact;
    }

    public void setRepairShopContact(String repairShopContact) {
        this.repairShopContact = repairShopContact;
    }

    public String getRepairShopPhone() {
        return repairShopPhone;
    }

    public void setRepairShopPhone(String repairShopPhone) {
        this.repairShopPhone = repairShopPhone;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
