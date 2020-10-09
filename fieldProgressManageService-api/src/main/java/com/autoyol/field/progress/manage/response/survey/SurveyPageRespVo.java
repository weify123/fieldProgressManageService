package com.autoyol.field.progress.manage.response.survey;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class SurveyPageRespVo extends PageResult {
    private static final long serialVersionUID = 3013956057470734758L;

    @AutoDocProperty(value = "数据列表")
    private List<SurveyRespVo> list;

    public List<SurveyRespVo> getList() {
        return list;
    }

    public void setList(List<SurveyRespVo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
