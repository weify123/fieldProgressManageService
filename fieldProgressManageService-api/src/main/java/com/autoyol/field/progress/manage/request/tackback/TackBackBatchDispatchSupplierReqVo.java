package com.autoyol.field.progress.manage.request.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

public class TackBackBatchDispatchSupplierReqVo extends BaseRequest {
    private static final long serialVersionUID = 7791872909380078651L;

    @AutoDocProperty(value = "取送车订单号列表", required = true)
    @NotNull(message = "取送车订单号列表不能为空")
    private List<String> pickDeliverOrderNoList;

    @AutoDocProperty(value = "服务分类;字典[flow_server_type]", required = true)
    @NotNull(message = "服务分类不能为空")
    private Integer serverTypeKey;

    @AutoDocProperty(value = "供应商公司id", required = true)
    @NotNull(message = "供应商公司id不能为空")
    private Integer supplierCompanyId;

    @AutoDocProperty(value = "取车时间年,格式yyyyMMdd, 后台目前只关心年", required = true)
    @NotBlank(message = "取车时间不能为空")
    private String pickTime;

    public List<String> getPickDeliverOrderNoList() {
        return pickDeliverOrderNoList;
    }

    public void setPickDeliverOrderNoList(List<String> pickDeliverOrderNoList) {
        this.pickDeliverOrderNoList = pickDeliverOrderNoList;
    }

    public Integer getServerTypeKey() {
        return serverTypeKey;
    }

    public void setServerTypeKey(Integer serverTypeKey) {
        this.serverTypeKey = serverTypeKey;
    }

    public Integer getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(Integer supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public String getPickTime() {
        return pickTime;
    }

    public void setPickTime(String pickTime) {
        this.pickTime = pickTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
