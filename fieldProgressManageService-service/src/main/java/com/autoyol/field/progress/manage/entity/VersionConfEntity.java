package com.autoyol.field.progress.manage.entity;

import java.util.Date;

/**
 * 
 *
 * @author feiyu.wei
 * @date 2019/12/31
 */
public class VersionConfEntity {
    /**
     * 
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 平台:0,Android;1,iOS
     */
    private Integer platformType;

    /**
     * 内部版本号
     */
    private String innerVersion;

    /**
     * 外部版本号
     */
    private String outerVersion;

    /**
     * 是否强制更新:0,否;1,是
     */
    private Integer forceUpdate;

    /**
     * 是否开启首页提示:0,否;1,是
     */
    private Integer homeNoticed;

    /**
     * 应用下载地址
     */
    private String downloadUrl;

    /**
     * 首页提示图片
     */
    private String homePicPath;

    /**
     * 描述
     */
    private String memo;

    /**
     * 是否发布:0,否;1,是
     */
    private Integer published;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getPlatformType() {
        return platformType;
    }

    public void setPlatformType(Integer platformType) {
        this.platformType = platformType;
    }

    public String getInnerVersion() {
        return innerVersion;
    }

    public void setInnerVersion(String innerVersion) {
        this.innerVersion = innerVersion == null ? null : innerVersion.trim();
    }

    public String getOuterVersion() {
        return outerVersion;
    }

    public void setOuterVersion(String outerVersion) {
        this.outerVersion = outerVersion;
    }

    public Integer getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Integer forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public Integer getHomeNoticed() {
        return homeNoticed;
    }

    public void setHomeNoticed(Integer homeNoticed) {
        this.homeNoticed = homeNoticed;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl == null ? null : downloadUrl.trim();
    }

    public String getHomePicPath() {
        return homePicPath;
    }

    public void setHomePicPath(String homePicPath) {
        this.homePicPath = homePicPath == null ? null : homePicPath.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Integer getPublished() {
        return published;
    }

    public void setPublished(Integer published) {
        this.published = published;
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