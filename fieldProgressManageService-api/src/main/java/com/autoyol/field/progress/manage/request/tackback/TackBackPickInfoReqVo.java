package com.autoyol.field.progress.manage.request.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.response.tackback.TackBackFileVo;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class TackBackPickInfoReqVo extends BaseTackReqVo {
    private static final long serialVersionUID = -8348438115120975499L;

    @AutoDocProperty(value = "取车油量")
    private BigDecimal pickOil;

    @AutoDocProperty(value = "取车公里数")
    private Integer pickKilo;

    @AutoDocProperty(value = "车辆状况,字典[vehicle_status]")
    private Integer vehicleStatusKey;

    @AutoDocProperty(value = "变更取车地址")
    private String changePickAddress;

    @AutoDocProperty(value = "变更取车经度")
    private BigDecimal changePickLong;

    @AutoDocProperty(value = "变更取车纬度")
    private BigDecimal changePickLat;

    @AutoDocProperty(value = "文件列表")
    private List<TackBackFileVo> fileList;
    /**
     * 编辑状态:0:未提交,1:已提交
     */
    @AutoDocProperty(value = "编辑状态:0:未提交,1:已提交", required = true)
    @NotNull(message = "编辑状态不能为空")
    private Integer editStatus;

    public BigDecimal getPickOil() {
        return pickOil;
    }

    public void setPickOil(BigDecimal pickOil) {
        this.pickOil = pickOil;
    }

    public Integer getPickKilo() {
        return pickKilo;
    }

    public void setPickKilo(Integer pickKilo) {
        this.pickKilo = pickKilo;
    }

    public Integer getVehicleStatusKey() {
        return vehicleStatusKey;
    }

    public void setVehicleStatusKey(Integer vehicleStatusKey) {
        this.vehicleStatusKey = vehicleStatusKey;
    }

    public String getChangePickAddress() {
        return changePickAddress;
    }

    public void setChangePickAddress(String changePickAddress) {
        this.changePickAddress = changePickAddress;
    }

    public BigDecimal getChangePickLong() {
        return changePickLong;
    }

    public void setChangePickLong(BigDecimal changePickLong) {
        this.changePickLong = changePickLong;
    }

    public BigDecimal getChangePickLat() {
        return changePickLat;
    }

    public void setChangePickLat(BigDecimal changePickLat) {
        this.changePickLat = changePickLat;
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
