package com.autoyol.field.progress.manage.response.city;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.page.PageResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class OrderPageRespVo extends PageResult {
    private static final long serialVersionUID = -7575922019629964489L;

    @AutoDocProperty("订单设定列表")
    private List<OrderSetVo> orderList;

    public List<OrderSetVo> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderSetVo> orderList) {
        this.orderList = orderList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
