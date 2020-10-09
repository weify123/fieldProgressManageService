package com.autoyol.field.progress.manage.request.attendance;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.user.BaseUserQueryVo;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

public class AttendanceExportReqVo extends BaseUserQueryVo {
    private static final long serialVersionUID = -2034270922940962096L;

    @AutoDocProperty("人员id")
    private Integer userId;

    @AutoDocProperty("用户姓名")
    private String userName;

    @AutoDocProperty("账户状态：0启用、1停用、2注销;字典类型名[user_status]")
    private Integer statusKey;

    @AutoDocProperty(value = "导出开始日期:如2019-12-23", required = true)
    @NotBlank(message = "导出开始日期不能为空")
    private String exportStartDate;

    @AutoDocProperty(value = "导出截止日期:如2019-12-23", required = true)
    @NotBlank(message = "导出截止日期不能为空")
    private String exportEndDate;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(Integer statusKey) {
        this.statusKey = statusKey;
    }

    public String getExportStartDate() {
        return exportStartDate;
    }

    public void setExportStartDate(String exportStartDate) {
        this.exportStartDate = exportStartDate;
    }

    public String getExportEndDate() {
        return exportEndDate;
    }

    public void setExportEndDate(String exportEndDate) {
        this.exportEndDate = exportEndDate;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
