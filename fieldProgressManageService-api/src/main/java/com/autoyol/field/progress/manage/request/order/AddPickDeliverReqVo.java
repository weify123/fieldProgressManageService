package com.autoyol.field.progress.manage.request.order;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class AddPickDeliverReqVo implements Serializable {
    private static final long serialVersionUID = -5479826281066937104L;
    /**
     * 取车备注
     **/
    private String takeNote;
    /**
     * 车龄
     **/
    private Float year;
    /**
     * 风控审核状态
     **/
    private String riskControlAuditState;

    /**
     * 车主会员号
     **/
    private String ownerNo;
    /**
     * 租客会员号
     **/
    private String renterNo;
    /**
     * 订单类型（0,普通订单 1,代步车订单 2,携程套餐订单 3,携程到店订单 4,同程套餐订单 5,安联代步车订单 6,普通套餐订单 7,VIP订单
     * ）
     **/
    private String orderType;
    /**
     * 渠道（0,短租渠道 1,代步车渠道 2,BD渠道订单 3,VIP渠道）
     **/
    private String channelType;

    /**
     * 车主取车地址
     **/
    private String ownerGetAddr;
    /**
     * 车主取车经度
     **/
    private String ownerGetLon;
    /**
     * 车主取车维度
     **/
    private String ownerGetLat;
    /**
     * 车主还车地址
     **/
    private String ownerReturnAddr;
    /**
     * 车主还车经度
     **/
    private String ownerReturnLon;
    /**
     * 车主还车维度
     **/
    private String ownerReturnLat;
    /**
     * 油量刻度分母
     **/
    private String oilScaleDenominator;
    /**
     * 订单编号
     **/
    private String ordernumber;
    /**
     * 服务类型（take:取车服务 back:还车服务）
     **/
    private String servicetype;
    /**
     * 起租时间
     **/
    private String termtime;
    /**
     * 归还时间
     **/
    private String returntime;
    /**
     * 车牌号
     **/
    private String carno;
    /**
     * 车型
     **/
    private String vehiclemodel;
    /**
     * 车辆类型
     **/
    private String vehicletype;
    /**
     * 交车所在市
     **/
    private String deliverycarcity;
    /**
     * 实际取车地址（只有取车服务才有）
     **/
    private String pickupcaraddr;
    /**
     * 实际还车地址（只有还车服务才有）
     **/
    private String alsocaraddr;
    /**
     * 车主姓名
     **/
    private String ownername;
    /**
     * 车主电话
     **/
    private String ownerphone;
    /**
     * 成功订单次数（此车辆）
     **/
    private String successordenumber;
    /**
     * 租客姓名
     **/
    private String tenantname;
    /**
     * 租客电话
     **/
    private String tenantphone;
    /**
     * 租客已成交次数
     **/
    private String tenantturnoverno;
    /**
     * 默认地址
     **/
    private String defaultpickupcaraddr;
    /**
     * 车辆使用类型
     **/
    private String ownerType;
    /**
     * 订单来源场景
     **/
    private String sceneName;
    /**
     * 排量
     **/
    private String displacement;
    /**
     * 交易来源
     * 1：手机，2：网站，3：管理后台, 4:CP B2C, 5: CP UPOP，6：携程，7:返利网，8:机场租车,10:H5一键租车,12：凹凸微信,13：APP分享下单
     **/
    private String source;
    /**
     * 预计取车时间（YYYY-MM-DD HH:mm)
     **/
    private String beforeTime;
    /**
     * 预计还车时间（YYYY-MM-DD HH:mm)
     **/
    private String afterTime;
    /**
     * 预计取车公里数
     **/
    private String getKilometre;
    /**
     * 预计还车公里数
     **/
    private String returnKilometre;
    /**
     * 代管车管理员
     **/
    private String delegaAdmin;
    /**
     * 代管车管理员手机号
     **/
    private String delegaAdminPhone;
    /**
     * 车辆最新检测状态（检测状态: 检测状态:1,未检测;2,检测通过;3,检测通过-建议改善;4,高危车辆-已修复;5,高危车辆-未修复6 .未检测-已报名7 .检测失效）
     **/
    private String detectStatus;
    /**
     * 日限里程
     **/
    private String dayMileage;
    /**
     * 线下订单类型（1:三方合同订单,2:托管车长租订单,3:旅游产品下单,4太保出险代步车（未满足））
     **/
    private String offlineOrderType;
    /**
     * 超级补充全
     **/
    private String ssaRisks;
    /**
     * 紧急联系人
     **/
    private String emerContact;
    /**
     * 紧急联系人电话
     **/
    private String emerContactPhone;
    /**
     * 油箱容量
     **/
    private String tankCapacity;
    /**
     * 实际取车经度
     **/
    private String realGetCarLon;
    /**
     * 实际取车维度
     **/
    private String realGetCarLat;
    /**
     * 实际还车经度
     **/
    private String realReturnCarLon;
    /**
     * 实际还车维度
     **/
    private String realReturnCarLat;
    /**
     * 车辆经度（默认取车经度）
     **/
    private String carLon;
    /**
     * 车辆纬度（默认取车纬度）
     **/
    private String carLat;
    /**
     * 油费单价
     **/
    private String oilPrice;
    /**
     * 租车押金支付时间,格式yyyyMMddHHmmss
     **/
    private String depositPayTime;
    /**
     * 会员标识
     **/
    private String renterMemberFlag;
    /**
     * 功能类型
     **/
    private String useType;
    /**
     * 航班号
     **/
    private String flightNo;
    /**
     * 日租金指导价
     **/
    private String guideDayPrice;
    /**
     * 签名
     **/
    private String sign;
    /**
     * 车主的标签
     **/
    private String ownerLables;
    /**
     * 租客的标签
     **/
    private String tenantLables;
    /**
     * 租客的等级
     **/
    private String tenantLevel;
    /**
     * 车主的等级
     **/
    private String ownerLevel;
    /**
     * 燃料
     */
    private String engineType;
    /**
     * 天单价
     */
    private String dayUnitPrice;
    /**
     * 天节日单价
     **/
    private String holidayPrice;
    /**
     * 天均价
     **/
    private String holidayAverage;
    /**
     * 租金
     **/
    private String rentAmt;
    /**
     * 合作方
     **/
    private String partner;

    public String getTakeNote() {
        return takeNote;
    }

    public void setTakeNote(String takeNote) {
        this.takeNote = takeNote;
    }

    public Float getYear() {
        return year;
    }

    public void setYear(Float year) {
        this.year = year;
    }

    public String getRiskControlAuditState() {
        return riskControlAuditState;
    }

    public void setRiskControlAuditState(String riskControlAuditState) {
        this.riskControlAuditState = riskControlAuditState;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getOwnerGetAddr() {
        return ownerGetAddr;
    }

    public void setOwnerGetAddr(String ownerGetAddr) {
        this.ownerGetAddr = ownerGetAddr;
    }

    public String getOwnerGetLon() {
        return ownerGetLon;
    }

    public void setOwnerGetLon(String ownerGetLon) {
        this.ownerGetLon = ownerGetLon;
    }

    public String getOwnerGetLat() {
        return ownerGetLat;
    }

    public void setOwnerGetLat(String ownerGetLat) {
        this.ownerGetLat = ownerGetLat;
    }

    public String getOwnerReturnAddr() {
        return ownerReturnAddr;
    }

    public void setOwnerReturnAddr(String ownerReturnAddr) {
        this.ownerReturnAddr = ownerReturnAddr;
    }

    public String getOwnerReturnLon() {
        return ownerReturnLon;
    }

    public void setOwnerReturnLon(String ownerReturnLon) {
        this.ownerReturnLon = ownerReturnLon;
    }

    public String getOwnerReturnLat() {
        return ownerReturnLat;
    }

    public void setOwnerReturnLat(String ownerReturnLat) {
        this.ownerReturnLat = ownerReturnLat;
    }

    public String getOilScaleDenominator() {
        return oilScaleDenominator;
    }

    public void setOilScaleDenominator(String oilScaleDenominator) {
        this.oilScaleDenominator = oilScaleDenominator;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public String getTermtime() {
        return termtime;
    }

    public void setTermtime(String termtime) {
        this.termtime = termtime;
    }

    public String getReturntime() {
        return returntime;
    }

    public void setReturntime(String returntime) {
        this.returntime = returntime;
    }

    public String getCarno() {
        return carno;
    }

    public void setCarno(String carno) {
        this.carno = carno;
    }

    public String getVehiclemodel() {
        return vehiclemodel;
    }

    public void setVehiclemodel(String vehiclemodel) {
        this.vehiclemodel = vehiclemodel;
    }

    public String getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(String vehicletype) {
        this.vehicletype = vehicletype;
    }

    public String getDeliverycarcity() {
        return deliverycarcity;
    }

    public void setDeliverycarcity(String deliverycarcity) {
        this.deliverycarcity = deliverycarcity;
    }

    public String getPickupcaraddr() {
        return pickupcaraddr;
    }

    public void setPickupcaraddr(String pickupcaraddr) {
        this.pickupcaraddr = pickupcaraddr;
    }

    public String getAlsocaraddr() {
        return alsocaraddr;
    }

    public void setAlsocaraddr(String alsocaraddr) {
        this.alsocaraddr = alsocaraddr;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getOwnerphone() {
        return ownerphone;
    }

    public void setOwnerphone(String ownerphone) {
        this.ownerphone = ownerphone;
    }

    public String getSuccessordenumber() {
        return successordenumber;
    }

    public void setSuccessordenumber(String successordenumber) {
        this.successordenumber = successordenumber;
    }

    public String getTenantname() {
        return tenantname;
    }

    public void setTenantname(String tenantname) {
        this.tenantname = tenantname;
    }

    public String getTenantphone() {
        return tenantphone;
    }

    public void setTenantphone(String tenantphone) {
        this.tenantphone = tenantphone;
    }

    public String getTenantturnoverno() {
        return tenantturnoverno;
    }

    public void setTenantturnoverno(String tenantturnoverno) {
        this.tenantturnoverno = tenantturnoverno;
    }

    public String getDefaultpickupcaraddr() {
        return defaultpickupcaraddr;
    }

    public void setDefaultpickupcaraddr(String defaultpickupcaraddr) {
        this.defaultpickupcaraddr = defaultpickupcaraddr;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBeforeTime() {
        return beforeTime;
    }

    public void setBeforeTime(String beforeTime) {
        this.beforeTime = beforeTime;
    }

    public String getAfterTime() {
        return afterTime;
    }

    public void setAfterTime(String afterTime) {
        this.afterTime = afterTime;
    }

    public String getGetKilometre() {
        return getKilometre;
    }

    public void setGetKilometre(String getKilometre) {
        this.getKilometre = getKilometre;
    }

    public String getReturnKilometre() {
        return returnKilometre;
    }

    public void setReturnKilometre(String returnKilometre) {
        this.returnKilometre = returnKilometre;
    }

    public String getDelegaAdmin() {
        return delegaAdmin;
    }

    public void setDelegaAdmin(String delegaAdmin) {
        this.delegaAdmin = delegaAdmin;
    }

    public String getDelegaAdminPhone() {
        return delegaAdminPhone;
    }

    public void setDelegaAdminPhone(String delegaAdminPhone) {
        this.delegaAdminPhone = delegaAdminPhone;
    }

    public String getDetectStatus() {
        return detectStatus;
    }

    public void setDetectStatus(String detectStatus) {
        this.detectStatus = detectStatus;
    }

    public String getDayMileage() {
        return dayMileage;
    }

    public void setDayMileage(String dayMileage) {
        this.dayMileage = dayMileage;
    }

    public String getOfflineOrderType() {
        return offlineOrderType;
    }

    public void setOfflineOrderType(String offlineOrderType) {
        this.offlineOrderType = offlineOrderType;
    }

    public String getSsaRisks() {
        return ssaRisks;
    }

    public void setSsaRisks(String ssaRisks) {
        this.ssaRisks = ssaRisks;
    }

    public String getEmerContact() {
        return emerContact;
    }

    public void setEmerContact(String emerContact) {
        this.emerContact = emerContact;
    }

    public String getEmerContactPhone() {
        return emerContactPhone;
    }

    public void setEmerContactPhone(String emerContactPhone) {
        this.emerContactPhone = emerContactPhone;
    }

    public String getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(String tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    public String getRealGetCarLon() {
        return realGetCarLon;
    }

    public void setRealGetCarLon(String realGetCarLon) {
        this.realGetCarLon = realGetCarLon;
    }

    public String getRealGetCarLat() {
        return realGetCarLat;
    }

    public void setRealGetCarLat(String realGetCarLat) {
        this.realGetCarLat = realGetCarLat;
    }

    public String getRealReturnCarLon() {
        return realReturnCarLon;
    }

    public void setRealReturnCarLon(String realReturnCarLon) {
        this.realReturnCarLon = realReturnCarLon;
    }

    public String getRealReturnCarLat() {
        return realReturnCarLat;
    }

    public void setRealReturnCarLat(String realReturnCarLat) {
        this.realReturnCarLat = realReturnCarLat;
    }

    public String getCarLon() {
        return carLon;
    }

    public void setCarLon(String carLon) {
        this.carLon = carLon;
    }

    public String getCarLat() {
        return carLat;
    }

    public void setCarLat(String carLat) {
        this.carLat = carLat;
    }

    public String getOilPrice() {
        return oilPrice;
    }

    public void setOilPrice(String oilPrice) {
        this.oilPrice = oilPrice;
    }

    public String getDepositPayTime() {
        return depositPayTime;
    }

    public void setDepositPayTime(String depositPayTime) {
        this.depositPayTime = depositPayTime;
    }

    public String getRenterMemberFlag() {
        return renterMemberFlag;
    }

    public void setRenterMemberFlag(String renterMemberFlag) {
        this.renterMemberFlag = renterMemberFlag;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getGuideDayPrice() {
        return guideDayPrice;
    }

    public void setGuideDayPrice(String guideDayPrice) {
        this.guideDayPrice = guideDayPrice;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOwnerLables() {
        return ownerLables;
    }

    public void setOwnerLables(String ownerLables) {
        this.ownerLables = ownerLables;
    }

    public String getTenantLables() {
        return tenantLables;
    }

    public void setTenantLables(String tenantLables) {
        this.tenantLables = tenantLables;
    }

    public String getTenantLevel() {
        return tenantLevel;
    }

    public void setTenantLevel(String tenantLevel) {
        this.tenantLevel = tenantLevel;
    }

    public String getOwnerLevel() {
        return ownerLevel;
    }

    public void setOwnerLevel(String ownerLevel) {
        this.ownerLevel = ownerLevel;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getDayUnitPrice() {
        return dayUnitPrice;
    }

    public void setDayUnitPrice(String dayUnitPrice) {
        this.dayUnitPrice = dayUnitPrice;
    }

    public String getHolidayPrice() {
        return holidayPrice;
    }

    public void setHolidayPrice(String holidayPrice) {
        this.holidayPrice = holidayPrice;
    }

    public String getHolidayAverage() {
        return holidayAverage;
    }

    public void setHolidayAverage(String holidayAverage) {
        this.holidayAverage = holidayAverage;
    }

    public String getRentAmt() {
        return rentAmt;
    }

    public void setRentAmt(String rentAmt) {
        this.rentAmt = rentAmt;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
