package com.autoyol.field.progress.manage.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2020/02/25
 */
public class TransOrderInfoLogEntity {

    private Integer id;
    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单类型（0,普通订单 1,代步车订单 2,携程套餐订单 3,携程到店订单 4,同程套餐订单 5,安联代步车订单 6,普通套餐订单 7,VIP订单
     * ）
     **/
    private String orderType;

    /**
     * 续租订单编号
     */
    private String renewOrderNo;

    /**
     * 所属城市
     */
    private String belongCity;

    /**
     * 交易来源; 1：手机，2：网站，3：管理后台, 4:CP B2C, 5: CP UPOP，6：携程，7:返利网，8:机场租车,10:H5一键租车,12：凹凸微信,13：APP分享下单
     */
    private Integer source;

    /**
     * 订单来源场景
     */
    private Integer sceneSource;

    /**
     * 线下订单类型; 1:三方合同订单,2:托管车长租订单,3:旅游产品下单,4太保出险代步车(未满足)
     */
    private Integer offlineOrderType;

    /**
     * 取车时间
     */
    private Date pickTime;

    /**
     * 订单起租时间
     */
    private Date rentStartTime;

    /**
     * 订单结束时间(归还时间)
     */
    private Date rentEntTime;

    /**
     * 租金
     */
    private BigDecimal rentAmt;

    /**
     * 天均价
     */
    private BigDecimal pricePerDay;

    /**
     * 车辆押金支付时间
     */
    private Date depositPayTime;

    /**
     * 超级补充全险
     */
    private String superSuppleRisk;

    /**
     * 合作方
     */
    private String partner;

    /**
     * 风控审核状态
     */
    private String riskControlAuditState;

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

    /**
     * 客服备注
     */
    private String custNote;

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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getRenewOrderNo() {
        return renewOrderNo;
    }

    public void setRenewOrderNo(String renewOrderNo) {
        this.renewOrderNo = renewOrderNo == null ? null : renewOrderNo.trim();
    }

    public String getBelongCity() {
        return belongCity;
    }

    public void setBelongCity(String belongCity) {
        this.belongCity = belongCity == null ? null : belongCity.trim();
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getSceneSource() {
        return sceneSource;
    }

    public void setSceneSource(Integer sceneSource) {
        this.sceneSource = sceneSource;
    }

    public Integer getOfflineOrderType() {
        return offlineOrderType;
    }

    public void setOfflineOrderType(Integer offlineOrderType) {
        this.offlineOrderType = offlineOrderType;
    }

    public Date getPickTime() {
        return pickTime;
    }

    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
    }

    public Date getRentStartTime() {
        return rentStartTime;
    }

    public void setRentStartTime(Date rentStartTime) {
        this.rentStartTime = rentStartTime;
    }

    public Date getRentEntTime() {
        return rentEntTime;
    }

    public void setRentEntTime(Date rentEntTime) {
        this.rentEntTime = rentEntTime;
    }

    public BigDecimal getRentAmt() {
        return rentAmt;
    }

    public void setRentAmt(BigDecimal rentAmt) {
        this.rentAmt = rentAmt;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Date getDepositPayTime() {
        return depositPayTime;
    }

    public void setDepositPayTime(Date depositPayTime) {
        this.depositPayTime = depositPayTime;
    }

    public String getSuperSuppleRisk() {
        return superSuppleRisk;
    }

    public void setSuperSuppleRisk(String superSuppleRisk) {
        this.superSuppleRisk = superSuppleRisk == null ? null : superSuppleRisk.trim();
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner == null ? null : partner.trim();
    }

    public String getRiskControlAuditState() {
        return riskControlAuditState;
    }

    public void setRiskControlAuditState(String riskControlAuditState) {
        this.riskControlAuditState = riskControlAuditState == null ? null : riskControlAuditState.trim();
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

    public String getCustNote() {
        return custNote;
    }

    public void setCustNote(String custNote) {
        this.custNote = custNote == null ? null : custNote.trim();
    }
}