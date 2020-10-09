package com.autoyol.field.progress.manage.response.attendance;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class AttendanceVo implements Serializable {
    private static final long serialVersionUID = -5263669826511839179L;

    @AutoDocProperty("id")
    private Integer id;

    @AutoDocProperty("日期:如1日")
    private String day;

    @AutoDocProperty("区间描述:如[上午],[下午]")
    private String zoneDesc;

    @AutoDocProperty("考勤状态:0,1,2,3,4,5,6;字典类型名[attendance_type]")
    private String statusKey;

    @AutoDocProperty("考勤状态:出勤,休息,年假,事假,病假,调休,加班")
    private String statusVal;

    @AutoDocProperty("备注")
    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getZoneDesc() {
        return zoneDesc;
    }

    public void setZoneDesc(String zoneDesc) {
        this.zoneDesc = zoneDesc;
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
