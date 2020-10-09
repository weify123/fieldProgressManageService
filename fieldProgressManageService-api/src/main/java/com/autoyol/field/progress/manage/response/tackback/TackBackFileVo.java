package com.autoyol.field.progress.manage.response.tackback;

import com.autoyol.doc.annotation.AutoDocProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class TackBackFileVo implements Serializable {
    private static final long serialVersionUID = 1720839228142975238L;

    @AutoDocProperty(value = "文件id")
    private Integer id;

    @AutoDocProperty(value = "文件路径")
    private String filePath;

    @AutoDocProperty(value = "录音语言描述")
    private String recordDesc;

    @AutoDocProperty(value = "录音时间")
    private Date recordTime;

    @AutoDocProperty(value = "录音上传时间")
    private Date recordUploadTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRecordDesc() {
        return recordDesc;
    }

    public void setRecordDesc(String recordDesc) {
        this.recordDesc = recordDesc;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Date getRecordUploadTime() {
        return recordUploadTime;
    }

    public void setRecordUploadTime(Date recordUploadTime) {
        this.recordUploadTime = recordUploadTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
