package com.autoyol.field.progress.manage.request.node;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RemindAddReqVo extends BaseRequest {
    private static final long serialVersionUID = 2758143458503769612L;

    @AutoDocProperty(value = "流程名称;0,1;字典类型名[flow_name_type]", required = true)
    @NotNull(message = "流程名称不能为空")
    private Integer flowNameKey;

    @AutoDocProperty(value = "服务类型;0,1,2,3,4,5,6,7,8,9,10;字典类型名[flow_server_type]", required = true)
    @NotNull(message = "服务类型不能为空")
    private Integer serverTypeKey;

    @AutoDocProperty(value = "节点名称;0,1,2,3,4,5,6,7,8,9;字典类型名[flow_node_name_type]", required = true)
    @NotNull(message = "节点名称不能为空")
    private Integer flowNodeNameKey;

    @AutoDocProperty(value = "提醒内容", required = true)
    @NotBlank(message = "提醒内容不能为空")
    @Length(max = 200, message = "提醒内容最大长度为200")
    private String noticeContent;

    @AutoDocProperty(value = "提醒次数", required = true)
    @NotNull(message = "提醒次数不能为空")
    @Max(value = 10000,message = "提醒次数最大10000")
    private Integer noticeCount;

    @AutoDocProperty(value = "提醒开始时间;格式yyyy-MM-dd", required = true)
    @NotBlank(message = "提醒开始时间不能为空")
    @Pattern(regexp = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$", message = "提醒开始时间格式错误")
    private String noticeStartTime;

    @AutoDocProperty(value = "提醒结束时间;格式yyyy-MM-dd", required = true)
    @NotBlank(message = "提醒结束时间不能为空")
    @Pattern(regexp = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$", message = "提醒结束时间格式错误")
    private String noticeEndTime;

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

    public String getNoticeStartTime() {
        return noticeStartTime;
    }

    public void setNoticeStartTime(String noticeStartTime) {
        this.noticeStartTime = noticeStartTime;
    }

    public String getNoticeEndTime() {
        return noticeEndTime;
    }

    public void setNoticeEndTime(String noticeEndTime) {
        this.noticeEndTime = noticeEndTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
