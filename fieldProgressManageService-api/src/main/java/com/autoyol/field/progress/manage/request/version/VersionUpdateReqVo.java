package com.autoyol.field.progress.manage.request.version;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class VersionUpdateReqVo extends BaseRequest {
    private static final long serialVersionUID = -7595944903227543134L;
    @AutoDocProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Integer id;
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

    @AutoDocProperty(value = "是否开启首页提示key;0,1;字典类型名[publish_type]若无其他区别，暂时使用", required = true)
    @NotNull(message = "是否开启首页提示不能为空")
    private Integer homeNoticedKey;

    @AutoDocProperty(value = "是否发布key;0,1;字典类型名[publish_type]")
    @NotNull(message = "是否发布不能为空")
    private Integer publishedKey;

    @AutoDocProperty(value = "首页提示图片", required = true)
    @NotBlank(message = "首页提示图片不能为空")
    @Length(max = 100, message = "首页提示图片最长100位")
    private String homePicPath;

    @AutoDocProperty(value = "应用下载地址", required = true)
    @NotBlank(message = "应用下载地址不能为空")
    @Length(max = 100, message = "应用下载地址最长100位")
    private String downloadUrl;

    @AutoDocProperty(value = "标题", required = true)
    @NotBlank(message = "标题不能为空")
    @Length(max = 12, message = "标题最长12位")
    private String title;

    @AutoDocProperty(value = "描述", required = true)
    @NotBlank(message = "描述不能为空")
    @Length(max = 70, message = "标题最长70位")
    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getHomeNoticedKey() {
        return homeNoticedKey;
    }

    public void setHomeNoticedKey(Integer homeNoticedKey) {
        this.homeNoticedKey = homeNoticedKey;
    }

    public Integer getPublishedKey() {
        return publishedKey;
    }

    public void setPublishedKey(Integer publishedKey) {
        this.publishedKey = publishedKey;
    }

    public String getHomePicPath() {
        return homePicPath;
    }

    public void setHomePicPath(String homePicPath) {
        this.homePicPath = homePicPath;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
