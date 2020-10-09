package com.autoyol.field.progress.manage.response.node;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.constraint.ExcelElement;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class RemindVo implements Serializable {
    private static final long serialVersionUID = 7749592888356217350L;

    @AutoDocProperty(value = "id")
    @ExcelElement(field = "id", index = 1)
    private Integer id;

    /**
     * 流程名称:0,取送车派单流程;1,GPS安装流程
     */
    @AutoDocProperty(value = "流程名称key;0,1;字典类型名[flow_name_type]")
    private Integer flowNameKey;
    @AutoDocProperty(value = "流程名称;取送车派单流程,GPS安装流程;字典类型名[flow_name_type]")
    @ExcelElement(field = "流程名称", index = 2)
    private String flowNameVal;

    /**
     * 服务类型:0,信息采集;1,车辆检测,2,GPS服务;3,取车服务;4,还车服务;5,保险事故维修取车服务;6,保险事故维修还车服务;7,GPS升级或维修取送车服务;8,车辆维修取送车服务;9,车后市场取车服务;10,车后市场还车服务
     */
    @AutoDocProperty("服务类型;0,1,2,3,4,5,6,7,8,9,10;字典类型名[flow_server_type]")
    private Integer serverTypeKey;
    @AutoDocProperty("服务类型;信息采集,车辆检测,GPS服务,取车服务,还车服务,保险事故维修取车服务,保险事故维修还车服务...;字典类型名[flow_server_type]")
    @ExcelElement(field = "服务类型", index = 3)
    private String serverTypeVal;

    /**
     * 节点名称:0,待接单;1,已接单;2,取车抵达;3,取车交接;4,取车完成;5,送车抵达;6,送车交接;7,送车完成;8,已拒单;9,已出发
     */
    @AutoDocProperty("节点名称;0,1,2,3,4,5,6,7,8,9;字典类型名[flow_node_name_type]")
    private Integer flowNodeNameKey;
    @AutoDocProperty("节点名称;待接单,已接单,取车抵达...;字典类型名[flow_node_name_type]")
    @ExcelElement(field = "节点名称", index = 4)
    private String flowNodeNameVal;

    /**
     * 提醒内容
     */
    @AutoDocProperty("提醒内容")
    @ExcelElement(field = "提醒内容", index = 5)
    private String noticeContent;

    /**
     * 提醒次数
     */
    @AutoDocProperty("每个车管家")
    @ExcelElement(field = "提醒次数", index = 6)
    private Integer noticeCount;

    /**
     * 提醒开始时间
     */
    @AutoDocProperty("提醒开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date noticeStartTime;

    @AutoDocProperty("提醒开始时间[导出使用]")
    @ExcelElement(field = "提醒开始时间", index = 7)
    private String noticeStartTimeStr;

    /**
     * 提醒结束时间
     */
    @AutoDocProperty("提醒结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date noticeEndTime;

    @AutoDocProperty("提醒开始时间[导出使用]")
    @ExcelElement(field = "提醒结束时间", index = 8)
    private String noticeEndTimeStr;

    /**
     * 是否有效:0,无效;1,有效
     */
    @AutoDocProperty("提醒状态:0,1;字典类型名[effective_type]")
    private Integer effectivedKey;
    @AutoDocProperty("提醒状态:无效;有效;字典类型名[effective_type]")
    @ExcelElement(field = "提醒状态", index = 9)
    private String effectivedVal;

    @AutoDocProperty("最后修改用户")
    private String createOp;
    /**
     *
     */
    @AutoDocProperty("创建时间")
    private Date createTime;

    /**
     *
     */
    @AutoDocProperty("最后修改用户")
    private String updateOp;

    /**
     *
     */
    @AutoDocProperty("修改时间")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFlowNameKey() {
        return flowNameKey;
    }

    public void setFlowNameKey(Integer flowNameKey) {
        this.flowNameKey = flowNameKey;
    }

    public String getFlowNameVal() {
        return flowNameVal;
    }

    public void setFlowNameVal(String flowNameVal) {
        this.flowNameVal = flowNameVal;
    }

    public Integer getServerTypeKey() {
        return serverTypeKey;
    }

    public void setServerTypeKey(Integer serverTypeKey) {
        this.serverTypeKey = serverTypeKey;
    }

    public String getServerTypeVal() {
        return serverTypeVal;
    }

    public void setServerTypeVal(String serverTypeVal) {
        this.serverTypeVal = serverTypeVal;
    }

    public Integer getFlowNodeNameKey() {
        return flowNodeNameKey;
    }

    public void setFlowNodeNameKey(Integer flowNodeNameKey) {
        this.flowNodeNameKey = flowNodeNameKey;
    }

    public String getFlowNodeNameVal() {
        return flowNodeNameVal;
    }

    public void setFlowNodeNameVal(String flowNodeNameVal) {
        this.flowNodeNameVal = flowNodeNameVal;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Integer getNoticeCount() {
        return noticeCount;
    }

    public void setNoticeCount(Integer noticeCount) {
        this.noticeCount = noticeCount;
    }

    public Date getNoticeStartTime() {
        return noticeStartTime;
    }

    public void setNoticeStartTime(Date noticeStartTime) {
        this.noticeStartTime = noticeStartTime;
    }

    public String getNoticeStartTimeStr() {
        return noticeStartTimeStr;
    }

    public void setNoticeStartTimeStr(String noticeStartTimeStr) {
        this.noticeStartTimeStr = noticeStartTimeStr;
    }

    public Date getNoticeEndTime() {
        return noticeEndTime;
    }

    public void setNoticeEndTime(Date noticeEndTime) {
        this.noticeEndTime = noticeEndTime;
    }

    public String getNoticeEndTimeStr() {
        return noticeEndTimeStr;
    }

    public void setNoticeEndTimeStr(String noticeEndTimeStr) {
        this.noticeEndTimeStr = noticeEndTimeStr;
    }

    public Integer getEffectivedKey() {
        return effectivedKey;
    }

    public void setEffectivedKey(Integer effectivedKey) {
        this.effectivedKey = effectivedKey;
    }

    public String getEffectivedVal() {
        return effectivedVal;
    }

    public void setEffectivedVal(String effectivedVal) {
        this.effectivedVal = effectivedVal;
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

    public String getUpdateOp() {
        return updateOp;
    }

    public void setUpdateOp(String updateOp) {
        this.updateOp = updateOp;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
