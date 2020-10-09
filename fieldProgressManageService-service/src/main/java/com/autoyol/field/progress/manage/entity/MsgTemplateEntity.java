package com.autoyol.field.progress.manage.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2020/03/17
 */
public class MsgTemplateEntity {
    /**
     * 
     */
    private Integer id;

    /**
     * 模板名称
     */
    private String msgName;

    /**
     * 所属系统：0、流程
     */
    private Integer belongSys;

    /**
     * 消息类型：0、短信；1、邮件；
     */
    private String msgType;

    private String msgSubject;

    /**
     * 消息状态：0、使用中；
     */
    private Integer msgStatus;

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
     * 模板内容
     */
    private String msgContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName == null ? null : msgName.trim();
    }

    public Integer getBelongSys() {
        return belongSys;
    }

    public void setBelongSys(Integer belongSys) {
        this.belongSys = belongSys;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType == null ? null : msgType.trim();
    }

    public String getMsgSubject() {
        return msgSubject;
    }

    public void setMsgSubject(String msgSubject) {
        this.msgSubject = msgSubject;
    }

    public Integer getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(Integer msgStatus) {
        this.msgStatus = msgStatus;
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

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent == null ? null : msgContent.trim();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}