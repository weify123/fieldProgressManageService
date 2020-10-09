package com.autoyol.field.progress.manage.response;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.constraint.ExcelElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class SysDictRespVo implements Serializable {

    private static final long serialVersionUID = -8723289511125975054L;
    /**
     * 主键id
     */
    @AutoDocProperty("主键id")
    private Integer id;

    /**
     * 类型名称：如user_status
     */
    @AutoDocProperty("类型名称:如user_status")
    private String typeName;

    /**
     * code值
     */
    @AutoDocProperty("code值:如0、1、2")
    private Integer code;

    /**
     * 标签名称
     */
    @AutoDocProperty("标签名称")
    private String label;

    /**
     * 标签名称
     */
    @AutoDocProperty("标签名称1")
    private String label1;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
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
