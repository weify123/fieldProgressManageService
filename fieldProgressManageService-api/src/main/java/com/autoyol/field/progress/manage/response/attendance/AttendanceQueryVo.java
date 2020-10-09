package com.autoyol.field.progress.manage.response.attendance;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class AttendanceQueryVo implements Serializable {
    private static final long serialVersionUID = 7920008482518263863L;

    /**
     * 人员id
     */
    @AutoDocProperty("人员id")
    private Integer userId;

    /**
     * 用户姓名
     */
    @AutoDocProperty("用户姓名")
    private String userName;

    /**
     * 服务公司
     */
    @AutoDocProperty("服务公司")
    private String companyName;

    /**
     * 城市
     */
    @AutoDocProperty("城市")
    private String cityName;

    /**
     * 当前用户的账户状态：0启用、1停用、2注销
     */
    @AutoDocProperty("当前用户的账户状态：0、1、2;字典类型名[user_status]")
    private String statusKey;
    @AutoDocProperty("当前用户的账户状态：启用、停用、注销;字典类型名[user_status]")
    private String statusVal;

    @AutoDocProperty("考勤列表")
    private List<AttendanceVo> attendanceList;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(String statusKey) {
        this.statusKey = statusKey;
    }

    public String getStatusVal() {
        return statusVal;
    }

    public void setStatusVal(String statusVal) {
        this.statusVal = statusVal;
    }

    public List<AttendanceVo> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<AttendanceVo> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
