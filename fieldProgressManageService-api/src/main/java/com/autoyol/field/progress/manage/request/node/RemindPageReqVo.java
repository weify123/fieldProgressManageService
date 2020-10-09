package com.autoyol.field.progress.manage.request.node;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.BasePage;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class RemindPageReqVo extends BasePage {
    private static final long serialVersionUID = 4335195087389217310L;

    /**
     * 流程名称:0,取送车派单流程;1,GPS安装流程
     */
    @AutoDocProperty("流程名称;0,1;字典类型名[flow_name_type]")
    private Integer flowNameKey;
    /**
     * 服务类型:0,信息采集;1,车辆检测,2,GPS服务;3,取车服务;4,还车服务;5,保险事故维修取车服务;6,保险事故维修还车服务;7,GPS升级或维修取送车服务;8,车辆维修取送车服务;9,车后市场取车服务;10,车后市场还车服务
     */
    @AutoDocProperty("服务类型;0,1,2,3,4,5,6,7,8,9,10;字典类型名[flow_server_type]")
    private Integer serverTypeKey;

    /**
     * 节点名称:0,待接单;1,已接单;2,取车抵达;3,取车交接;4,取车完成;5,送车抵达;6,送车交接;7,送车完成;8,已拒单;9,已出发
     */
    @AutoDocProperty("节点名称;0,1,2,3,4,5,6,7,8,9;字典类型名[flow_node_name_type]")
    private Integer flowNodeNameKey;

    /**
     * 提醒开始时间
     */
    @AutoDocProperty("提醒开始时间;格式yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate noticeStartTime;

    /**
     * 提醒结束时间
     */
    @AutoDocProperty("提醒结束时间;格式yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate noticeEndTime;

    /**
     * 是否有效:0,无效;1,有效
     */
    @AutoDocProperty("提醒状态:0,无效;1,有效;字典类型名[effective_type]")
    private Integer effectivedKey;

    public Integer getFlowNameKey() {
        return flowNameKey;
    }

    public void setFlowNameKey(Integer flowNameKey) {
        this.flowNameKey = flowNameKey;
    }

    public Integer getServerTypeKey() {
        return serverTypeKey;
    }

    public void setServerTypeKey(Integer serverTypeKey) {
        this.serverTypeKey = serverTypeKey;
    }

    public Integer getFlowNodeNameKey() {
        return flowNodeNameKey;
    }

    public void setFlowNodeNameKey(Integer flowNodeNameKey) {
        this.flowNodeNameKey = flowNodeNameKey;
    }

    public LocalDate getNoticeStartTime() {
        return noticeStartTime;
    }

    public void setNoticeStartTime(LocalDate noticeStartTime) {
        this.noticeStartTime = noticeStartTime;
    }

    public LocalDate getNoticeEndTime() {
        return noticeEndTime;
    }

    public void setNoticeEndTime(LocalDate noticeEndTime) {
        this.noticeEndTime = noticeEndTime;
    }

    public Integer getEffectivedKey() {
        return effectivedKey;
    }

    public void setEffectivedKey(Integer effectivedKey) {
        this.effectivedKey = effectivedKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
