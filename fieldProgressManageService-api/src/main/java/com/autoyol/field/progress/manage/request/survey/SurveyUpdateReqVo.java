package com.autoyol.field.progress.manage.request.survey;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class SurveyUpdateReqVo extends BaseRequest {
    private static final long serialVersionUID = -880682443818558546L;

    /**
     * 人员id
     */
    @AutoDocProperty(value = "主键id", required = true)
    @NotNull(message = "id不能为空")
    private Integer id;

    /**
     * 题库类型;0:A,1:B,2:C,3:D,4:E
     */
    @AutoDocProperty(value = "题库类型;0,1,2,3,4;字典类型名[topic_type]", required = true)
    @NotNull(message = "题库类型不能为空")
    private Integer topicTypeKey;

    /**
     * 短信模板
     */
    @AutoDocProperty(value = "短信模板", required = true)
    @NotBlank(message = "短信模板不能为空")
    @Length(max = 140, message = "短信模板长度不能超过140")
    private String msgContent;

    /**
     * 短信链接
     */
    @AutoDocProperty(value = "短信链接")
    @Length(max = 50, message = "短信链接长度不能超过50")
    private String msgLink;
    /**
     * 是否有效:0,无效;1,有效
     */
    @AutoDocProperty(value = "是否有效;0,1;字典类型名[effective_type]")
    @NotNull(message = "是否有效不能为空")
    private Integer effectivedKey;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
