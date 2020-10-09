package com.autoyol.field.progress.manage.response.store;

import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class StoreInfoPageRespVo extends PageResult {
    private static final long serialVersionUID = -8811117179407097462L;

    private List<StoreInfoPageVo> pageList;

    public List<StoreInfoPageVo> getPageList() {
        return pageList;
    }

    public void setPageList(List<StoreInfoPageVo> pageList) {
        this.pageList = pageList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
