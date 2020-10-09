package com.autoyol.field.progress.manage.exception;

public class ShortUrlException extends RuntimeException{
    private static final long serialVersionUID = 4517317778460469976L;

    public ShortUrlException(String msg) {
        super(msg);
    }

    public ShortUrlException(String code, String msg) {
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
