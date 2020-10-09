package com.autoyol.field.progress.manage.response.survey;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class SurveyRespVo implements Serializable {
    private static final long serialVersionUID = -72906131079282868L;
    private Integer id;

    /**
     * 题库类型;0:A,1:B,2:C,3:D,4:E
     */
    @AutoDocProperty(value = "题库类型key;0,1,2,3,4;字典类型名[topic_type]")
    private Integer topicTypeKey;
    @AutoDocProperty(value = "题库类型val;A,B,C,D,E;字典类型名[topic_type]")
    private String topicTypeVal;

    /**
     * 短信模板
     */
    @AutoDocProperty(value = "短信模板")
    private String msgContent;

    @AutoDocProperty(value = "短信链接")
    private String msgLink;
    /**
     * 是否有效:0,无效;1,有效
     */
    @AutoDocProperty(value = "是否有效key;0,1;字典类型名[effective_type]")
    private Integer effectivedKey;

    @AutoDocProperty(value = "是否有效val;无效;有效;字典类型名[effective_type]")
    private String effectivedVal;
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

    public Integer getTopicTypeKey() {
        return topicTypeKey;
    }

    public void setTopicTypeKey(Integer topicTypeKey) {
        this.topicTypeKey = topicTypeKey;
    }

    public String getTopicTypeVal() {
        return topicTypeVal;
    }

    public void setTopicTypeVal(String topicTypeVal) {
        this.topicTypeVal = topicTypeVal;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgLink() {
        return msgLink;
    }

    public void setMsgLink(String msgLink) {
        this.msgLink = msgLink;
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
