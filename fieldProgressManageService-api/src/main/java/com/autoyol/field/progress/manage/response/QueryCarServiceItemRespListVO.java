package com.autoyol.field.progress.manage.response;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

@Data
public class QueryCarServiceItemRespListVO extends PageResult {

    @AutoDocProperty(value = "结果集")
    private List<QueryCarServiceItemRespVO> list;


    public List<QueryCarServiceItemRespVO> getList() {
        return list;
    }

    public void setList(List<QueryCarServiceItemRespVO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
