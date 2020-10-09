package com.autoyol.field.progress.manage.response.schedulelog;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class ScheduleLogVo implements Serializable {

    private static final long serialVersionUID = -1964843701103207817L;

    @AutoDocProperty("主键id")
    private Integer id;

    @AutoDocProperty("取送车订单号")
    private String pickDeliverOrderNo;

    @AutoDocProperty("调度状态Key;字典类型名[pick_deliver_schedule_status]")
    private Integer scheduleStatusKey;

    @AutoDocProperty("调度状态val;")
    private String scheduleStatusVal;

    @AutoDocProperty("取消/回退/拒单原因")
    private String reason;

    @AutoDocProperty("操作人")
    private String createOp;

    @AutoDocProperty("变更时间")
    private Date createTime;

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
        this.pickDeliverOrderNo = pickDeliverOrderNo;
    }

    public Integer getScheduleStatusKey() {
        return scheduleStatusKey;
    }

    public void setScheduleStatusKey(Integer scheduleStatusKey) {
        this.scheduleStatusKey = scheduleStatusKey;
    }

    public String getScheduleStatusVal() {
        return scheduleStatusVal;
    }

    public void setScheduleStatusVal(String scheduleStatusVal) {
        this.scheduleStatusVal = scheduleStatusVal;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreateOp() {
        return createOp;
    }

    public void setCreateOp(String createOp) {
        this.createOp = createOp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
