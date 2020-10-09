package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class TackBackCanOprStatusRespVo implements Serializable {
    private static final long serialVersionUID = -7107754461238197882L;

    @AutoDocProperty(value = "取送车数量限制")
    private Integer tackFileLimit;

    @AutoDocProperty(value = "列表查看")
    private String tackBackSee;

    @AutoDocProperty(value = "列表编辑")
    private String tackBackEdit;

    @AutoDocProperty(value = "取车信息可编辑状态")
    private String pickInfoCanEdit;

    @AutoDocProperty(value = "取车信息可提交状态")
    private String pickInfoCanSubmit;

    @AutoDocProperty(value = "送车信息可编辑状态")
    private String deliverInfoCanEdit;

    @AutoDocProperty(value = "送车信息可提交状态")
    private String deliverInfoCanSubmit;

    @AutoDocProperty(value = "交易订单可编辑状态")
    private String transOrderCanEdit;

    @AutoDocProperty(value = "交易订单可提交状态")
    private String transOrderCanSubmit;

    @AutoDocProperty(value = "费用信息可编辑状态")
    private String feeInfoCanEdit;

    @AutoDocProperty(value = "费用信息可提交状态")
    private String feeInfoCanSubmit;

    @AutoDocProperty(value = "调度信息可编辑状态")
    private String scheduleInfoCanEdit;

    @AutoDocProperty(value = "调度信息可提交状态")
    private String scheduleInfoCanSubmit;

    @AutoDocProperty(value = "可批量分配状态")
    private String waitDispatchCode;

    @AutoDocProperty(value = "可取消订单状态")
    private String pickOrderCanCancel;

    @AutoDocProperty(value = "可回退订单状态")
    private String pickOrderCanBack;

    @AutoDocProperty(value = "(供应商)可拒单状态")
    private String supplierCanReject;

    public Integer getTackFileLimit() {
        return tackFileLimit;
    }

    public void setTackFileLimit(Integer tackFileLimit) {
        this.tackFileLimit = tackFileLimit;
    }

    public String getTackBackSee() {
        return tackBackSee;
    }

    public void setTackBackSee(String tackBackSee) {
        this.tackBackSee = tackBackSee;
    }

    public String getTackBackEdit() {
        return tackBackEdit;
    }

    public void setTackBackEdit(String tackBackEdit) {
        this.tackBackEdit = tackBackEdit;
    }

    public String getPickInfoCanEdit() {
        return pickInfoCanEdit;
    }

    public void setPickInfoCanEdit(String pickInfoCanEdit) {
        this.pickInfoCanEdit = pickInfoCanEdit;
    }

    public String getPickInfoCanSubmit() {
        return pickInfoCanSubmit;
    }

    public void setPickInfoCanSubmit(String pickInfoCanSubmit) {
        this.pickInfoCanSubmit = pickInfoCanSubmit;
    }

    public String getDeliverInfoCanEdit() {
        return deliverInfoCanEdit;
    }

    public void setDeliverInfoCanEdit(String deliverInfoCanEdit) {
        this.deliverInfoCanEdit = deliverInfoCanEdit;
    }

    public String getDeliverInfoCanSubmit() {
        return deliverInfoCanSubmit;
    }

    public void setDeliverInfoCanSubmit(String deliverInfoCanSubmit) {
        this.deliverInfoCanSubmit = deliverInfoCanSubmit;
    }

    public String getTransOrderCanEdit() {
        return transOrderCanEdit;
    }

    public void setTransOrderCanEdit(String transOrderCanEdit) {
        this.transOrderCanEdit = transOrderCanEdit;
    }

    public String getTransOrderCanSubmit() {
        return transOrderCanSubmit;
    }

    public void setTransOrderCanSubmit(String transOrderCanSubmit) {
        this.transOrderCanSubmit = transOrderCanSubmit;
    }

    public String getFeeInfoCanEdit() {
        return feeInfoCanEdit;
    }

    public void setFeeInfoCanEdit(String feeInfoCanEdit) {
        this.feeInfoCanEdit = feeInfoCanEdit;
    }

    public String getFeeInfoCanSubmit() {
        return feeInfoCanSubmit;
    }

    public void setFeeInfoCanSubmit(String feeInfoCanSubmit) {
        this.feeInfoCanSubmit = feeInfoCanSubmit;
    }

    public String getScheduleInfoCanEdit() {
        return scheduleInfoCanEdit;
    }

    public void setScheduleInfoCanEdit(String scheduleInfoCanEdit) {
        this.scheduleInfoCanEdit = scheduleInfoCanEdit;
    }

    public String getScheduleInfoCanSubmit() {
        return scheduleInfoCanSubmit;
    }

    public void setScheduleInfoCanSubmit(String scheduleInfoCanSubmit) {
        this.scheduleInfoCanSubmit = scheduleInfoCanSubmit;
    }

    public String getWaitDispatchCode() {
        return waitDispatchCode;
    }

    public void setWaitDispatchCode(String waitDispatchCode) {
        this.waitDispatchCode = waitDispatchCode;
    }

    public String getPickOrderCanCancel() {
        return pickOrderCanCancel;
    }

    public void setPickOrderCanCancel(String pickOrderCanCancel) {
        this.pickOrderCanCancel = pickOrderCanCancel;
    }

    public String getPickOrderCanBack() {
        return pickOrderCanBack;
    }

    public void setPickOrderCanBack(String pickOrderCanBack) {
        this.pickOrderCanBack = pickOrderCanBack;
    }

    public String getSupplierCanReject() {
        return supplierCanReject;
    }

    public void setSupplierCanReject(String supplierCanReject) {
        this.supplierCanReject = supplierCanReject;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
