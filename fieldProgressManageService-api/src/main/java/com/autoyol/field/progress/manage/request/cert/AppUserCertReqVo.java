package com.autoyol.field.progress.manage.request.cert;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2019/12/18
 */
public class AppUserCertReqVo implements Serializable {

    private static final long serialVersionUID = 5806097773141353054L;
    /**
     * 证件类型:1,身份证;2,驾驶证
     */
    @AutoDocProperty(value = "证件类型:1,身份证;2,驾驶证;3,头像;字典类型名[cert_type]")
    @NotNull(message = "certType 不能为空")
    private Integer certType;

    /**
     * 正反面:0,正面;1,反面;2,无正反面
     */
    @AutoDocProperty(value = "正反面:0,正面;1,反面;2,无正反面;字典类型名[side_type]")
    @NotNull(message = "side 不能为空")
    private Integer side;

    /**
     * 证件路径或地址
     */
    @AutoDocProperty(value = "证件路径或地址")
    /*@NotBlank(message = "certPath 不能为空")*/
    @Length(max = 100, message = "地址超长")
    private String certPath;


    public Integer getCertType() {
        return certType;
    }

    public void setCertType(Integer certType) {
        this.certType = certType;
    }

    public Integer getSide() {
        return side;
    }

    public void setSide(Integer side) {
        this.side = side;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }
}