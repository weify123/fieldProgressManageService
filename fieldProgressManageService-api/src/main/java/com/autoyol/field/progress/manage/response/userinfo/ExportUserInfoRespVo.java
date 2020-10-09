package com.autoyol.field.progress.manage.response.userinfo;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.constraint.ExcelElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author feiyu.wei
 * @date 2019/12/19
 */
public class ExportUserInfoRespVo implements Serializable {

    private static final long serialVersionUID = -936725383142562581L;

    @AutoDocProperty("人员id")
    @ExcelElement(field="人员id",index=1)
    private Integer userId;

    /**
     * 用户姓名
     */
    @AutoDocProperty("用户姓名")
    @ExcelElement(field="用户姓名",index=2)
    private String userName;

    /**
     * 服务公司
     */
    @AutoDocProperty("服务公司")
    @ExcelElement(field="服务公司",index=3)
    private String companyName;

    /**
     * 城市
     */
    @AutoDocProperty("城市")
    @ExcelElement(field="城市",index=4)
    private String cityName;

    /**
     * 用户手机号
     */
    @AutoDocProperty("用户手机号")
    @ExcelElement(field="用户手机号",index=5)
    private String mobile;

    @AutoDocProperty("身份证号")
    @ExcelElement(field="身份证号",index=6)
    private String idNo;
    /**
     * 联系人姓名
     */
    @AutoDocProperty("联系人姓名")
    @ExcelElement(field="联系人姓名",index=7)
    private String contactName;

    /**
     * 联系人手机
     */
    @AutoDocProperty("联系人手机")
    @ExcelElement(field="联系人手机",index=8)
    private String contactMobile;

    /**
     * 任职情况:0,专职;1,兼职
     */
    @AutoDocProperty("任职情况:0,专职;1,兼职")
    @ExcelElement(field="任职情况",index=9)
    private String employment;

    /**
     * 如果根据经纬度可查地址，该字段可删除
     */
    @AutoDocProperty("如果根据经纬度可查地址，该字段可删除")
    @ExcelElement(field="居住地址",index=10)
    private String address;

    /**
     * 经度
     */
    @AutoDocProperty("经度")
    @ExcelElement(field="居住地址经度",index=11)
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @AutoDocProperty("纬度")
    @ExcelElement(field="居住地址纬度",index=12)
    private BigDecimal latitude;

    /**
     * 星级:1,一星;2,二星;3,三星;4,四星;5,五星;
     */
    @AutoDocProperty("星级:1,一星;2,二星;3,三星;4,四星;5,五星;")
    @ExcelElement(field="星级",index=13)
    private String star;

    /**
     * 岗位:0,取送车人员;1,GPS工程师;
     */
    @AutoDocProperty("岗位:0,取送车人员;1,GPS工程师;")
    @ExcelElement(field="岗位",index=14)
    private String station;

    /**
     * 当前用户的账户状态：0启用、1停用、2注销
     */
    @AutoDocProperty("当前用户的账户状态：0启用、1停用、2注销")
    @ExcelElement(field="账户启用状态",index=15)
    private String statusVal;

    /**
     *
     */
    @ExcelElement(field="操作人",index=16)
    private String createOp;

    @AutoDocProperty("修改时间, 格式化后的")
    @ExcelElement(field="操作时间",index=17)
    private String updateTimeStr;

    /**
     * 能否派单:1,能;0,否
     */
    @ExcelElement(field="能否派单",index=18)
    private String distribute;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
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

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

    public String getDistribute() {
        return distribute;
    }

    public void setDistribute(String distribute) {
        this.distribute = distribute;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}