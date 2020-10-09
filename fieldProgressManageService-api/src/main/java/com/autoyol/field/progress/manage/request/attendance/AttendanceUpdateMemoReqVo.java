package com.autoyol.field.progress.manage.request.attendance;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class AttendanceUpdateMemoReqVo extends BaseRequest {
    private static final long serialVersionUID = 5358564143865262992L;

    @AutoDocProperty(value = "主键id", required = true)
    @NotNull(message = "id 不能为空")
    private Integer id;

    @AutoDocProperty(value = "备注")
    @Length(max = 50, message = "备注最长50位")
    private String memo;

    /**
     * 考勤状态:0,出勤;1,休息;2,年假;3,事假;4,病假;5,调休6,加班
     */
    @AutoDocProperty(value = "考勤状态:0,出勤;1,休息;2,年假;3,事假;4,病假;5,调休6,加班;字典类型名[attendance_type]", required = true)
    @NotNull(message = "考勤状态不能为空")
    private Integer statusKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(Integer statusKey) {
        this.statusKey = statusKey;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
