package com.autoyol.field.progress.manage.response.schedulelog;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class SchedulePageRespVo implements Serializable {
    private static final long serialVersionUID = -7045108473683095202L;

    private List<ScheduleLogVo> list;

    public List<ScheduleLogVo> getList() {
        return list;
    }

    public void setList(List<ScheduleLogVo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
