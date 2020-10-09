package com.autoyol.field.progress.manage.response.city;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.constraint.ExcelElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * @author feiyu.wei
 * @date 2019/12/16
 */
public class CityLevelVo implements Serializable {
    private static final long serialVersionUID = 7494009011876066553L;
    /**
     *
     */
    @AutoDocProperty("城市id")
    @ExcelElement(field = "城市id", index = 1)
    private Integer cityId;

    /**
     * 城市
     */
    @AutoDocProperty("城市")
    @ExcelElement(field = "城市", index = 2)
    private String city;

    /**
     * 城市等级
     */
    @AutoDocProperty("城市等级key:0,1,2,3,4;字典类型名[city_level_type]")
    private Integer cityLevelKey;

    @AutoDocProperty("城市等级val:1,2,3,4,5")
    @ExcelElement(field = "城市等级", index = 3)
    private String cityLevelVal;

    /**
     *
     */
    private String createOp;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private String updateOp;

    /**
     *
     */
    private Date updateTime;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCityLevelKey() {
        return cityLevelKey;
    }

    public void setCityLevelKey(Integer cityLevelKey) {
        this.cityLevelKey = cityLevelKey;
    }

    public String getCityLevelVal() {
        return cityLevelVal;
    }

    public void setCityLevelVal(String cityLevelVal) {
        this.cityLevelVal = cityLevelVal;
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

    public String getUpdateOp() {
        return updateOp;
    }

    public void setUpdateOp(String updateOp) {
        this.updateOp = updateOp;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}