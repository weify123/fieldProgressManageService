package com.autoyol.field.progress.manage.response.user;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.constraint.ExcelElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * @author feiyu.wei
 * @date 2019/12/13
 */
public class AppUserVo implements Serializable {
    private static final long serialVersionUID = 9043966153253519397L;
    /**
     * 人员id
     */
    @AutoDocProperty("人员id")
    @ExcelElement(field = "人员id", index = 1)
    private Integer userId;

    /**
     * 用户姓名
     */
    @AutoDocProperty("用户姓名")
    @ExcelElement(field = "用户姓名", index = 2)
    private String userName;

    /**
     * 用户手机号
     */
    @AutoDocProperty("用户手机号")
    @ExcelElement(field = "用户手机号", index = 5)
    private String mobile;


    @AutoDocProperty("服务公司id")
    private Integer companyId;

    @AutoDocProperty("城市id")
    private Integer cityId;

    @AutoDocProperty("服务公司一级级分类")
    private String companyNameFirst;
    /**
     * 服务公司
     */
    @AutoDocProperty("服务公司二级分类")
    @ExcelElement(field = "服务公司", index = 3)
    private String companyName;

    /**
     * 城市
     */
    @AutoDocProperty("城市")
    @ExcelElement(field = "城市", index = 4)
    private String cityName;

    /**
     * 当前用户的账户状态：0启用、1停用、2注销
     */
    @AutoDocProperty("当前用户的账户状态：0、1、2;字典类型名[user_status]")
    private Integer statusKey;

    @AutoDocProperty("账户状态:启用、停用、注销;字典类型名[user_status]")
    @ExcelElement(field = "账户启用状态", index = 9)
    private String statusVal;

    /**
     * 角色:0,App取送车
     */
    @AutoDocProperty("角色:0,App取送车")
    private Integer role;

    /**
     *
     */
    @ExcelElement(field = "操作人", index = 6)
    private String createOp;

    /**
     *
     */
    private Date createTime;

    @AutoDocProperty("创建时间, 格式化后的")
    @ExcelElement(field = "操作时间", index = 7)
    private String createTimeStr;

    /**
     *
     */
    private String updateOp;

    /**
     *
     */
    private Date updateTime;

    @AutoDocProperty("修改时间, 格式化后的")
    @ExcelElement(field = "最后修改时间", index = 8)
    private String updateTimeStr;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompanyNameFirst() {
        return companyNameFirst;
    }

    public void setCompanyNameFirst(String companyNameFirst) {
        this.companyNameFirst = companyNameFirst;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(Integer statusKey) {
        this.statusKey = statusKey;
    }

    public String getStatusVal() {
        return statusVal;
    }

    public void setStatusVal(String statusVal) {
        this.statusVal = statusVal;
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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}