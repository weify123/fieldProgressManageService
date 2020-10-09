package com.autoyol.field.progress.manage.entity;

import java.util.Date;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2019/12/31
 */
public class NodeRemindConfEntity {
    /**
     * 
     */
    private Integer id;

    /**
     * 流程名称:0,取送车派单流程;1,GPS安装流程
     */
    private Integer flowNameKey;

    /**
     * 服务类型:0,信息采集;1,车辆检测,2,GPS服务;3,取车服务;4,还车服务;5,保险事故维修取车服务;6,保险事故维修还车服务;7,GPS升级或维修取送车服务;8,车辆维修取送车服务;9,车后市场取车服务;10,车后市场还车服务
     */
    private Integer flowServerType;

    /**
     * 节点名称:0,待接单;1,已接单;2,取车抵达;3,取车交接;4,取车完成;5,送车抵达;6,送车交接;7,送车完成;8,已拒单;9,已出发
     */
    private Integer flowNodeNameKey;

    /**
     * 提醒内容
     */
    private String noticeContent;

    /**
     * 提醒次数
     */
    private Integer noticeCount;

    /**
     * 提醒开始时间
     */
    private Date noticeStartTime;

    /**
     * 提醒结束时间
     */
    private Date noticeEndTime;

    /**
     * 是否有效:0,无效;1,有效
     */
    private Integer effectived;

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

    public Integer getFlowServerType() {
        return flowServerType;
    }

    public void setFlowServerType(Integer flowServerType) {
        this.flowServerType = flowServerType;
    }

    public Integer getFlowNodeNameKey() {
        return flowNodeNameKey;
    }

    public void setFlowNodeNameKey(Integer flowNodeNameKey) {
        this.flowNodeNameKey = flowNodeNameKey;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent == null ? null : noticeContent.trim();
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

    public Date getNoticeEndTime() {
        return noticeEndTime;
    }

    public void setNoticeEndTime(Date noticeEndTime) {
        this.noticeEndTime = noticeEndTime;
    }

    public Integer getEffectived() {
        return effectived;
    }

    public void setEffectived(Integer effectived) {
        this.effectived = effectived;
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
}