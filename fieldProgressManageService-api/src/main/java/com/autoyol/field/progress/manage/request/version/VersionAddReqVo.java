package com.autoyol.field.progress.manage.request.version;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class VersionAddReqVo extends BaseRequest {
    private static final long serialVersionUID = 2430266137830016848L;
    /**
     * 标题
     */
    @AutoDocProperty(value = "标题", required = true)
    @NotBlank(message = "标题不能为空")
    @Length(max = 12, message = "标题最长12位")
    private String title;

    /**
     * 平台:0,Android;1,iOS
     */
    @AutoDocProperty(value = "平台key;0;字典类型名[platform_type]", required = true)
    @NotNull(message = "平台不能为空")
    private Integer platformTypeKey;

    /**
     * 内部版本号
     */
    @AutoDocProperty(value = "内部版本号", required = true)
    @NotBlank(message = "内部版本号不能为空")
    @Length(max = 20, message = "内部版本号最长20位")
    @Pattern(regexp = "^\\d+(\\.\\d+)*$", message = "内部版本号格式错误")
    private String innerVersion;

    /**
     * 外部版本号
     */
    @AutoDocProperty(value = "外部版本号", required = true)
    @NotBlank(message = "外部版本号不能为空")
    @Length(max = 20, message = "外部版本号最长20位")
    @Pattern(regexp = "^\\d+(\\.\\d+)*$", message = "外部版本号格式错误")
    private String outerVersion;

    /**
     * 是否强制更新:0,否;1,是
     */
    @AutoDocProperty(value = "是否强制更新key;0,1;字典类型名[publish_type]若无其他区别，暂时使用", required = true)
    @NotNull(message = "是否强制更新不能为空")
    private Integer forceUpdateKey;

    /**
     * 是否开启首页提示:0,否;1,是
     */
    @AutoDocProperty(value = "是否开启首页提示key;0,1;字典类型名[publish_type]若无其他区别，暂时使用", required = true)
    @NotNull(message = "是否开启首页提示不能为空")
    private Integer homeNoticedKey;

    /**
     * 应用下载地址
     */
    @AutoDocProperty(value = "应用下载地址", required = true)
    @NotBlank(message = "应用下载地址不能为空")
    @Length(max = 100, message = "应用下载地址最长100位")
    private String downloadUrl;

    /**
     * 首页提示图片
     */
    @AutoDocProperty(value = "首页提示图片", required = true)
    @NotBlank(message = "首页提示图片不能为空")
    @Length(max = 100, message = "首页提示图片最长100位")
    private String homePicPath;

    /**
     * 描述
     */
    @AutoDocProperty(value = "描述", required = true)
    @NotBlank(message = "描述不能为空")
    @Length(max = 70, message = "描述最长70位")
    private String memo;

    /**
     * 是否发布:0,否;1,是
     */
    @AutoDocProperty(value = "是否发布key;0,1;字典类型名[publish_type]")
    @NotNull(message = "是否发布不能为空")
    private Integer publishedKey;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPlatformTypeKey() {
        return platformTypeKey;
    }

    public void setPlatformTypeKey(Integer platformTypeKey) {
        this.platformTypeKey = platformTypeKey;
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

    public Integer getHomeNoticedKey() {
        return homeNoticedKey;
    }

    public void setHomeNoticedKey(Integer homeNoticedKey) {
        this.homeNoticedKey = homeNoticedKey;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getHomePicPath() {
        return homePicPath;
    }

    public void setHomePicPath(String homePicPath) {
        this.homePicPath = homePicPath;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
