package com.autoyol.field.progress.manage.request.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class OprTackBackOrderReqVo extends BaseTackBackReqVo {
    private static final long serialVersionUID = -1044501480503943607L;

    @AutoDocProperty(value = "调度状态,字典[pick_deliver_schedule_status]", required = true)
    @NotNull(message = "调度状态")
    private Integer scheduleStatusKey;

    @AutoDocProperty(value = "原因,字典[cancel_order_reason]", required = true)
    @NotBlank(message = "原因不能为空")
    private String reason;

    public Integer getScheduleStatusKey() {
        return scheduleStatusKey;
    }

    public void setScheduleStatusKey(Integer scheduleStatusKey) {
        this.scheduleStatusKey = scheduleStatusKey;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
