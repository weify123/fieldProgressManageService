package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class PickBackSelectRespVo implements Serializable {
    private static final long serialVersionUID = -6999118292158791577L;

    @AutoDocProperty(value = "取送订单信息")
    private PickOrderInfoVo pickOrderInfo;

    @AutoDocProperty(value = "调度信息")
    private PickBackScheduleInfoVo scheduleInfo;

    @AutoDocProperty(value = "取车信息")
    private TackPickInfoVo pickInfo;

    @AutoDocProperty(value = "送车信息")
    private TackDeliverInfoVo deliverInfo;

    public PickOrderInfoVo getPickOrderInfo() {
        return pickOrderInfo;
    }

    public void setPickOrderInfo(PickOrderInfoVo pickOrderInfo) {
        this.pickOrderInfo = pickOrderInfo;
    }

    public PickBackScheduleInfoVo getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(PickBackScheduleInfoVo scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }

    public TackPickInfoVo getPickInfo() {
        return pickInfo;
    }

    public void setPickInfo(TackPickInfoVo pickInfo) {
        this.pickInfo = pickInfo;
    }

    public TackDeliverInfoVo getDeliverInfo() {
        return deliverInfo;
    }

    public void setDeliverInfo(TackDeliverInfoVo deliverInfo) {
        this.deliverInfo = deliverInfo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
