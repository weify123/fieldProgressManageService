package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class PickDeliverLogVo implements Serializable {
    private static final long serialVersionUID = -302753658372319285L;

    private Integer id;

    @AutoDocProperty(value = "取送车订单号")
    private String serviceOrderNo;

    @AutoDocProperty(value = "操作内容")
    private String oprContent;

    @AutoDocProperty(value = "操作人")
    private String createOp;

    @AutoDocProperty(value = "操作时间")
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceOrderNo() {
        return serviceOrderNo;
    }

    public void setServiceOrderNo(String serviceOrderNo) {
        this.serviceOrderNo = serviceOrderNo;
    }

    public String getOprContent() {
        return oprContent;
    }

    public void setOprContent(String oprContent) {
        this.oprContent = oprContent;
    }

    public String getCreateOp() {
        return createOp;
    }

    public void setCreateOp(String createOp) {
        this.createOp = createOp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
