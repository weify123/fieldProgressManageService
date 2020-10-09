package com.autoyol.field.progress.manage.exception;

import com.autoyol.field.progress.manage.constraint.FieldErrorCode;

public class TackBackException extends RuntimeException{

    public TackBackException(FieldErrorCode fieldErrorCode) {
        super(fieldErrorCode.getText());
        this.code = fieldErrorCode.getCode();
    }

    public TackBackException(String msg) {
        super(msg);
    }

    public TackBackException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
