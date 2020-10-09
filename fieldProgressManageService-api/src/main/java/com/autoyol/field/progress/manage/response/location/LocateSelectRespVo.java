package com.autoyol.field.progress.manage.response.location;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class LocateSelectRespVo implements Serializable {
    private static final long serialVersionUID = -4335719855919504218L;

    @AutoDocProperty("用户姓名")
    private String userName;

    @AutoDocProperty("最近定位时间")
    private Date locateTime;

    @AutoDocProperty("取车经度")
    private BigDecimal pickLongitude;

    @AutoDocProperty("取车伟度")
    private BigDecimal pickLatitude;

    @AutoDocProperty("送达经度")
    private BigDecimal deliverLongitude;

    @AutoDocProperty("送达伟度")
    private BigDecimal deliverLatitude;

    @AutoDocProperty("订单列表,目前最多两条")
    private List<TackBackOrderVo> list;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getLocateTime() {
        return locateTime;
    }

    public void setLocateTime(Date locateTime) {
        this.locateTime = locateTime;
    }

    public BigDecimal getPickLongitude() {
        return pickLongitude;
    }

    public void setPickLongitude(BigDecimal pickLongitude) {
        this.pickLongitude = pickLongitude;
    }

    public BigDecimal getPickLatitude() {
        return pickLatitude;
    }

    public void setPickLatitude(BigDecimal pickLatitude) {
        this.pickLatitude = pickLatitude;
    }

    public BigDecimal getDeliverLongitude() {
        return deliverLongitude;
    }

    public void setDeliverLongitude(BigDecimal deliverLongitude) {
        this.deliverLongitude = deliverLongitude;
    }

    public BigDecimal getDeliverLatitude() {
        return deliverLatitude;
    }

    public void setDeliverLatitude(BigDecimal deliverLatitude) {
        this.deliverLatitude = deliverLatitude;
    }

    public List<TackBackOrderVo> getList() {
        return list;
    }

    public void setList(List<TackBackOrderVo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
