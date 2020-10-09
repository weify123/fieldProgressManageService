package com.autoyol.field.progress.manage.request.user;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import com.autoyol.field.progress.manage.request.cert.AppUserCertReqVo;
import com.autoyol.field.progress.manage.util.Email;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author feiyu.wei
 * @date 2019/12/13
 */
public class UserInfoUpdateReqVo extends BaseRequest {

    private static final long serialVersionUID = 9100990987191006170L;

    /**
     * 人员id
     */
    @AutoDocProperty(value = "userId", required = true)
    @NotNull(message = "userId不能为空")
    private Integer userId;

    /**
     * 身份证号
     */
    @AutoDocProperty(value = "身份证号", required = true)
    @NotBlank(message = "idNo不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", message = "身份证格式错误")
    private String idNo;

    /**
     * 任职情况
     */
    @AutoDocProperty(value = "任职情况", required = true)
    @NotBlank(message = "employment不能为空")
    private String employment;

    /**
     * 邮箱
     */
    @AutoDocProperty(value = "邮箱", required = true)
    @Email(message = "邮箱格式不正确")
    @Length(max = 30, message = "邮箱最长30位")
    private String email;

    @AutoDocProperty(value = "联系人用户姓名", required = true)
    @NotBlank(message = "contactName不能为空")
    @Size(min = 1, max = 10, message = "联系人用户姓名长度最多10位")
    private String contactName;

    @AutoDocProperty(value = "联系人手机号", required = true)
    @NotBlank(message = "contactMobile不能为空")
    @Size(min = 11, max = 11, message = "联系人手机号长度为11位")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "联系人手机号格式错误")
    private String contactMobile;

    /**
     * 如果根据经纬度可查地址，该字段可删除
     */
    @AutoDocProperty(value = "如果根据经纬度可查地址，该字段可删除", required = true)
    @NotBlank(message = "address不能为空")
    @Length(max = 100, message = "address 最长100位")
    private String address;

    /**
     * 经度
     */
    @AutoDocProperty(value = "经度", required = true)
    @NotNull(message = "longitude不能为空")
    @DecimalMin(message = "longitude 不能小于0", value = "0")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @AutoDocProperty(value = "纬度", required = true)
    @NotNull(message = "latitude不能为空")
    @DecimalMin(message = "latitude 不能小于0", value = "0")
    private BigDecimal latitude;

    /**
     * 星级:1,一星;2,二星;3,三星;4,四星;5,五星;
     */
    @AutoDocProperty(value = "星级:1,一星;2,二星;3,三星;4,四星;5,五星;", required = true)
    @NotNull(message = "star不能为空")
    private String star;

    /**
     * 岗位:0,取送车人员;1,GPS工程师;
     */
    @AutoDocProperty(value = "岗位:0,取送车人员;1,GPS工程师;", required = true)
    @NotNull(message = "station不能为空")
    private String station;

    /**
     * 能否派单:1,能;0,否
     */
    @AutoDocProperty(value = "能否派单:1,能;0,否", required = true)
    @NotNull(message = "distribute不能为空")
    private String distribute;

    /*@AutoDocProperty(value = "账户状态：0启用、1停用、2注销;字典类型名[user_status]", required = true)
    @NotNull(message = "curStatus不能为空")
    private Integer curStatus;*/

    @AutoDocProperty("证件列表")
    @Valid
    private List<AppUserCertReqVo> certList;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDistribute() {
        return distribute;
    }

    public void setDistribute(String distribute) {
        this.distribute = distribute;
    }

    /*public Integer getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(Integer curStatus) {
        this.curStatus = curStatus;
    }*/

    public List<AppUserCertReqVo> getCertList() {
        return certList;
    }

    public void setCertList(List<AppUserCertReqVo> certList) {
        this.certList = certList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}