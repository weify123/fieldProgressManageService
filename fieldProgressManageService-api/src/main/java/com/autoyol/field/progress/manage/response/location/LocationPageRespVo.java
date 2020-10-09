package com.autoyol.field.progress.manage.response.location;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class LocationPageRespVo extends PageResult {
    private static final long serialVersionUID = -7614085376984474541L;

    @AutoDocProperty("用户定位状态列表")
    private List<LocateVo> locateVos;

    public List<LocateVo> getLocateVos() {
        return locateVos;
    }

    public void setLocateVos(List<LocateVo> locateVos) {
        this.locateVos = locateVos;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
