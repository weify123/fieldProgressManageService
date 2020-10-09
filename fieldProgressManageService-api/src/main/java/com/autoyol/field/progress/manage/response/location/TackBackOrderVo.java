package com.autoyol.field.progress.manage.response.location;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class TackBackOrderVo implements Serializable {
    private static final long serialVersionUID = -7469472066147258423L;

    @AutoDocProperty(value = "订单服务类型;字典[flow_server_type]")
    private String serverTypeVal;

    @AutoDocProperty(value = "取/还车时间")
    private Date tackBackTime;

    @AutoDocProperty(value = "取/还车地址")
    private String tackBackAddr;

    @AutoDocProperty(value = "预计送达时间")
    private Date expectDeliverTime;

    @AutoDocProperty(value = "送达地址")
    private String deliverAddr;

    public String getServerTypeVal() {
        return serverTypeVal;
    }

    public void setServerTypeVal(String serverTypeVal) {
        this.serverTypeVal = serverTypeVal;
    }

    public Date getTackBackTime() {
        return tackBackTime;
    }

    public void setTackBackTime(Date tackBackTime) {
        this.tackBackTime = tackBackTime;
    }

    public String getTackBackAddr() {
        return tackBackAddr;
    }

    public void setTackBackAddr(String tackBackAddr) {
        this.tackBackAddr = tackBackAddr;
    }

    public Date getExpectDeliverTime() {
        return expectDeliverTime;
    }

    public void setExpectDeliverTime(Date expectDeliverTime) {
        this.expectDeliverTime = expectDeliverTime;
    }

    public String getDeliverAddr() {
        return deliverAddr;
    }

    public void setDeliverAddr(String deliverAddr) {
        this.deliverAddr = deliverAddr;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
