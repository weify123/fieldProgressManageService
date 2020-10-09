package com.autoyol.field.progress.manage.response;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2019/12/19
 */
public class ImportLogVo implements Serializable {
    private static final long serialVersionUID = 4927102108560009363L;

    private Integer id;

    /**
     * 导入业务类型
     */
    @AutoDocProperty("导入业务类型")
    private Integer businessType;
    /**
     * 匹配成功记录
     */
    @AutoDocProperty("匹配成功记录")
    private String matchRecordSucc;

    /**
     * 匹配失败记录
     */
    @AutoDocProperty("匹配失败记录;如[失败X条|第Y行,第Z行]")
    private String matchRecordFail;

    /**
     * 导入成功记录
     */
    @AutoDocProperty("导入成功记录")
    private String importRecordSucc;

    /**
     * 导入失败记录
     */
    @AutoDocProperty("导入失败记录;如[失败X条|第Y行,第Z行]")
    private String importRecordFail;

    /**
     * 
     */
    @AutoDocProperty("创建人")
    private String createOp;

    /**
     * 
     */
    @AutoDocProperty("创建时间,已格式化")
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getMatchRecordSucc() {
        return matchRecordSucc;
    }

    public void setMatchRecordSucc(String matchRecordSucc) {
        this.matchRecordSucc = matchRecordSucc;
    }

    public String getMatchRecordFail() {
        return matchRecordFail;
    }

    public void setMatchRecordFail(String matchRecordFail) {
        this.matchRecordFail = matchRecordFail;
    }

    public String getImportRecordSucc() {
        return importRecordSucc;
    }

    public void setImportRecordSucc(String importRecordSucc) {
        this.importRecordSucc = importRecordSucc;
    }

    public String getImportRecordFail() {
        return importRecordFail;
    }

    public void setImportRecordFail(String importRecordFail) {
        this.importRecordFail = importRecordFail;
    }

    public String getCreateOp() {
        return createOp;
    }

    public void setCreateOp(String createOp) {
        this.createOp = createOp;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}