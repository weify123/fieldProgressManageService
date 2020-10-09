package com.autoyol.field.progress.manage.response.attendance;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class AttendanceQueryPageRespVo extends PageResult {
    private static final long serialVersionUID = 4730822727762424325L;

    @AutoDocProperty("考勤列表")
    private List<AttendanceQueryVo> list;

    public List<AttendanceQueryVo> getList() {
        return list;
    }

    public void setList(List<AttendanceQueryVo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
