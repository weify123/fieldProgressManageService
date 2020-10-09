package com.autoyol.field.progress.manage.exception;

public class MemCoreInfoException extends RuntimeException{
    private static final long serialVersionUID = -4877434851984658765L;

    public MemCoreInfoException(String msg) {
        super(msg);
    }

    public MemCoreInfoException(String code, String msg) {
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
