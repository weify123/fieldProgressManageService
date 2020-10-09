package com.autoyol.field.progress.manage.entity;

import java.util.Date;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2019/12/19
 */
public class ImportLogEntity {
    /**
     * 
     */
    private Integer id;

    /**
     * 导入业务类型
     */
    private Integer businessType;

    /**
     * 匹配成功记录
     */
    private String matchRecordSucc;

    /**
     * 匹配失败记录
     */
    private String matchRecordFail;

    /**
     * 导入成功记录
     */
    private String importRecordSucc;

    /**
     * 导入失败记录
     */
    private String importRecordFail;

    /**
     * 是否删除:0:正常;1,已删除
     */
    private Integer isDelete;

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
        this.matchRecordSucc = matchRecordSucc == null ? null : matchRecordSucc.trim();
    }

    public String getMatchRecordFail() {
        return matchRecordFail;
    }

    public void setMatchRecordFail(String matchRecordFail) {
        this.matchRecordFail = matchRecordFail == null ? null : matchRecordFail.trim();
    }

    public String getImportRecordSucc() {
        return importRecordSucc;
    }

    public void setImportRecordSucc(String importRecordSucc) {
        this.importRecordSucc = importRecordSucc == null ? null : importRecordSucc.trim();
    }

    public String getImportRecordFail() {
        return importRecordFail;
    }

    public void setImportRecordFail(String importRecordFail) {
        this.importRecordFail = importRecordFail == null ? null : importRecordFail.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateOp() {
        return createOp;
    }

    public void setCreateOp(String createOp) {
        this.createOp = createOp == null ? null : createOp.trim();
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
        this.updateOp = updateOp == null ? null : updateOp.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}