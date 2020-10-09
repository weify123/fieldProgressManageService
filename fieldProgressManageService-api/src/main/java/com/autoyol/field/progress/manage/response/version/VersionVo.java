package com.autoyol.field.progress.manage.response.version;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class VersionVo implements Serializable {
    private static final long serialVersionUID = 1766369871903229637L;

    /**
     *
     */
    private Integer id;

    @AutoDocProperty(value = "标题")
    private String title;

    @AutoDocProperty(value = "首页提示图片")
    private String homePicPath;
    /**
     * 平台:0,Android;1,iOS
     */
    @AutoDocProperty(value = "平台key;0;字典类型名[platform_type]")
    private Integer platformTypeKey;
    @AutoDocProperty(value = "平台val;Android;字典类型名[platform_type]")
    private String platformTypeVal;

    /**
     * 内部版本号
     */
    @AutoDocProperty(value = "内部版本号")
    private String innerVersion;

    /**
     * 外部版本号
     */
    @AutoDocProperty(value = "外部版本号")
    private String outerVersion;

    /**
     * 是否强制更新:0,否;1,是
     */
    @AutoDocProperty(value = "是否强制更新key;0,1;字典类型名[publish_type]若无其他区别，暂时使用")
    private Integer forceUpdateKey;
    @AutoDocProperty(value = "是否强制更新val;否,是;字典类型名[publish_type]若无其他区别，暂时使用")
    private String forceUpdateVal;

    /**
     * 是否开启首页提示:0,否;1,是
     */
    @AutoDocProperty(value = "是否开启首页提示key;0,1;字典类型名[publish_type]若无其他区别，暂时使用")
    private Integer homeNoticedKey;
    @AutoDocProperty(value = "是否开启首页提示val;否,是;字典类型名[publish_type]若无其他区别，暂时使用")
    private String homeNoticedVal;

    /**
     * 应用下载地址
     */
    @AutoDocProperty(value = "应用下载地址")
    private String downloadUrl;

    /**
     * 描述
     */
    @AutoDocProperty(value = "描述")
    private String memo;

    /**
     * 是否发布:0,否;1,是
     */
    @AutoDocProperty(value = "是否发布key;0,1;字典类型名[publish_type]")
    private Integer publishedKey;
    @AutoDocProperty(value = "是否发布val;否,是;字典类型名[publish_type]")
    private String publishedVal;

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
        this.title = title;
    }

    public String getHomePicPath() {
        return homePicPath;
    }

    public void setHomePicPath(String homePicPath) {
        this.homePicPath = homePicPath;
    }

    public Integer getPlatformTypeKey() {
        return platformTypeKey;
    }

    public void setPlatformTypeKey(Integer platformTypeKey) {
        this.platformTypeKey = platformTypeKey;
    }

    public String getPlatformTypeVal() {
        return platformTypeVal;
    }

    public void setPlatformTypeVal(String platformTypeVal) {
        this.platformTypeVal = platformTypeVal;
    }

    public String getInnerVersion() {
        return innerVersion;
    }

    public void setInnerVersion(String innerVersion) {
        this.innerVersion = innerVersion;
    }

    public String getOuterVersion() {
        return outerVersion;
    }

    public void setOuterVersion(String outerVersion) {
        this.outerVersion = outerVersion;
    }

    public Integer getForceUpdateKey() {
        return forceUpdateKey;
    }

    public void setForceUpdateKey(Integer forceUpdateKey) {
        this.forceUpdateKey = forceUpdateKey;
    }

    public String getForceUpdateVal() {
        return forceUpdateVal;
    }

    public void setForceUpdateVal(String forceUpdateVal) {
        this.forceUpdateVal = forceUpdateVal;
    }

    public Integer getHomeNoticedKey() {
        return homeNoticedKey;
    }

    public void setHomeNoticedKey(Integer homeNoticedKey) {
        this.homeNoticedKey = homeNoticedKey;
    }

    public String getHomeNoticedVal() {
        return homeNoticedVal;
    }

    public void setHomeNoticedVal(String homeNoticedVal) {
        this.homeNoticedVal = homeNoticedVal;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getPublishedKey() {
        return publishedKey;
    }

    public void setPublishedKey(Integer publishedKey) {
        this.publishedKey = publishedKey;
    }

    public String getPublishedVal() {
        return publishedVal;
    }

    public void setPublishedVal(String publishedVal) {
        this.publishedVal = publishedVal;
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
