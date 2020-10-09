package com.autoyol.field.progress.manage.request.attendance;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AttendanceImportReqVo extends BaseRequest {
    private static final long serialVersionUID = 2794900688173842991L;

    @AutoDocProperty(value = "导入日期:如2019-12-23", required = true)
    @NotBlank(message = "导入日期不能为空")
    private String importDate;

    @AutoDocProperty(value = "excel文件", required = true)
    @NotNull(message = "excel文件不能为空")
    private MultipartFile attachmentFile;

    public String getImportDate() {
        return importDate;
    }

    public void setImportDate(String importDate) {
        this.importDate = importDate;
    }

    public MultipartFile getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(MultipartFile attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
