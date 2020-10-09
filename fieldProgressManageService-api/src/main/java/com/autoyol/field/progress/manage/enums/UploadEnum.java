package com.autoyol.field.progress.manage.enums;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.stream.Stream;

public enum UploadEnum {
    CERT(0, "config"),
    APP(1, "app"),
    TACK_PICK(2, "tack/pick"),
    TACK_DELIVER(3, "tack/deliver"),
    BACK_PICK(4, "back/pick"),
    BACK_DELIVER(5, "back/deliver"),
    ;
    private Integer code;
    private String path;

    UploadEnum(Integer code, String path) {
        this.code = code;
        this.path = path;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static UploadEnum getEnumByCode(Integer code) {
        return Stream.of(UploadEnum.values()).filter(uploadEnum -> uploadEnum.getCode().equals(code)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
