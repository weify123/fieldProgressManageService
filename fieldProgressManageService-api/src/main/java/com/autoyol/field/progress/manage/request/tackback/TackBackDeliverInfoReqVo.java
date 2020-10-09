package com.autoyol.field.progress.manage.request.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import com.autoyol.field.progress.manage.response.tackback.TackBackFileVo;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class TackBackDeliverInfoReqVo extends BaseRequest {
    private static final long serialVersionUID = -1077374935329678648L;

    @AutoDocProperty(value = "取送车订单号", required = true)
    @NotBlank(message = "取送车订单号不能为空")
    private String pickDeliverOrderNo;

    @AutoDocProperty(value = "服务分类;字典[flow_server_type]", required = true)
    @NotNull(message = "服务分类不能为空")
    private Integer serverTypeKey;

    @AutoDocProperty(value = "取车时间年,格式yyyyMMdd", required = true)
    @NotBlank(message = "取车时间不能为空")
    private String pickTime;

    @AutoDocProperty(value = "送达油量")
    private BigDecimal deliverOil;

    @AutoDocProperty(value = "送达公里数")
    private BigDecimal deliverKilo;

    @AutoDocProperty(value = "变更送达地址")
    private String changeDeliverAddress;

    @AutoDocProperty(value = "变更送达经度")
    private BigDecimal changeDeliverLong;

    @AutoDocProperty(value = "变更送达纬度")
    private BigDecimal changeDeliverLat;

    @AutoDocProperty(value = "延期送达时间, 格式yyyy-MM-dd HH:mm")
    private String delayDeliverTime;

    @AutoDocProperty(value = "送达迟到原因;字典[delay_reason]")
    private Integer delayReasonKey;

    @AutoDocProperty(value = "文件列表")
    private List<TackBackFileVo> fileList;
    /**
     * 编辑状态:0:未提交,1:已提交
     */
    @AutoDocProperty(value = "编辑状态:0:未提交,1:已提交", required = true)
    @NotNull(message = "编辑状态不能为空")
    private Integer editStatus;

    public String getPickDeliverOrderNo() {
        return pickDeliverOrderNo;
    }

    public void setPickDeliverOrderNo(String pickDeliverOrderNo) {
        this.pickDeliverOrderNo = pickDeliverOrderNo;
    }

    public Integer getServerTypeKey() {
        return serverTypeKey;
    }

    public void setServerTypeKey(Integer serverTypeKey) {
        this.serverTypeKey = serverTypeKey;
    }

    public String getPickTime() {
        return pickTime;
    }

    public void setPickTime(String pickTime) {
        this.pickTime = pickTime;
    }

    public BigDecimal getDeliverOil() {
        return deliverOil;
    }

    public void setDeliverOil(BigDecimal deliverOil) {
        this.deliverOil = deliverOil;
    }

    public BigDecimal getDeliverKilo() {
        return deliverKilo;
    }

    public void setDeliverKilo(BigDecimal deliverKilo) {
        this.deliverKilo = deliverKilo;
    }

    public String getChangeDeliverAddress() {
        return changeDeliverAddress;
    }

    public void setChangeDeliverAddress(String changeDeliverAddress) {
        this.changeDeliverAddress = changeDeliverAddress;
    }

    public BigDecimal getChangeDeliverLong() {
        return changeDeliverLong;
    }

    public void setChangeDeliverLong(BigDecimal changeDeliverLong) {
        this.changeDeliverLong = changeDeliverLong;
    }

    public BigDecimal getChangeDeliverLat() {
        return changeDeliverLat;
    }

    public void setChangeDeliverLat(BigDecimal changeDeliverLat) {
        this.changeDeliverLat = changeDeliverLat;
    }

    public String getDelayDeliverTime() {
        return delayDeliverTime;
    }

    public void setDelayDeliverTime(String delayDeliverTime) {
        this.delayDeliverTime = delayDeliverTime;
    }

    public Integer getDelayReasonKey() {
        return delayReasonKey;
    }

    public void setDelayReasonKey(Integer delayReasonKey) {
        this.delayReasonKey = delayReasonKey;
    }

    public List<TackBackFileVo> getFileList() {
        return fileList;
    }

    public void setFileList(List<TackBackFileVo> fileList) {
        this.fileList = fileList;
    }

    public Integer getEditStatus() {
        return editStatus;
    }

    public void setEditStatus(Integer editStatus) {
        this.editStatus = editStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
