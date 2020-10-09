package com.autoyol.field.progress.manage.request.order;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

public class UpdateTransOrderReqVo implements Serializable {
    private static final long serialVersionUID = 7530383068246217312L;
    /**
     * 订单编号
     **/
    @NotBlank(message = "订单编号为空")
    private String ordernumber;
    /**
     * 服务类型（take:取车服务 back:还车服务）
     **/
    @NotBlank(message = "服务类型为空")
    private String servicetype;
    /**
     * 变更类型（addr：地址变更 time:订单时间变更 ownerAddr: 车主取还车地址变更
     **/
    @NotBlank(message = "变更类型为空")
    private String changetype;
    /**
     * 新实际取车地址
     **/
    private String newpickupcaraddr;
    /**
     * 新实际还车地址
     **/
    private String newalsocaraddr;

    /**
     * 新起租时间
     **/
    private String newtermtime;
    /**
     * 新归还时间
     **/
    private String newreturntime;
    /**
     * 默认地址
     **/
    private String defaultpickupcaraddr;
    /**
     * 预计取车时间（YYYYMMDDHHmmss)
     **/
    private String newbeforeTime;

    /**
     * 预计还车时间（YYYYMMDDHHmmss)
     **/
    private String newafterTime;

    /**
     * 预计取车公里数
     **/
    private String newgetKilometre;

    /**
     * 预计还车公里数
     **/
    private String newreturnKilometre;

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
     * 油费单价
     **/
    private String oilPrice;
    /**
     * 租车押金支付时间,格式yyyyMMddHHmmss
     **/
    private String depositPayTime;
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
     * 签名
     **/
    private String sign;

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

    public String getChangetype() {
        return changetype;
    }

    public void setChangetype(String changetype) {
        this.changetype = changetype;
    }

    public String getNewpickupcaraddr() {
        return newpickupcaraddr;
    }

    public void setNewpickupcaraddr(String newpickupcaraddr) {
        this.newpickupcaraddr = newpickupcaraddr;
    }

    public String getNewalsocaraddr() {
        return newalsocaraddr;
    }

    public void setNewalsocaraddr(String newalsocaraddr) {
        this.newalsocaraddr = newalsocaraddr;
    }

    public String getNewtermtime() {
        return newtermtime;
    }

    public void setNewtermtime(String newtermtime) {
        this.newtermtime = newtermtime;
    }

    public String getNewreturntime() {
        return newreturntime;
    }

    public void setNewreturntime(String newreturntime) {
        this.newreturntime = newreturntime;
    }

    public String getDefaultpickupcaraddr() {
        return defaultpickupcaraddr;
    }

    public void setDefaultpickupcaraddr(String defaultpickupcaraddr) {
        this.defaultpickupcaraddr = defaultpickupcaraddr;
    }

    public String getNewbeforeTime() {
        return newbeforeTime;
    }

    public void setNewbeforeTime(String newbeforeTime) {
        this.newbeforeTime = newbeforeTime;
    }

    public String getNewafterTime() {
        return newafterTime;
    }

    public void setNewafterTime(String newafterTime) {
        this.newafterTime = newafterTime;
    }

    public String getNewgetKilometre() {
        return newgetKilometre;
    }

    public void setNewgetKilometre(String newgetKilometre) {
        this.newgetKilometre = newgetKilometre;
    }

    public String getNewreturnKilometre() {
        return newreturnKilometre;
    }

    public void setNewreturnKilometre(String newreturnKilometre) {
        this.newreturnKilometre = newreturnKilometre;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
