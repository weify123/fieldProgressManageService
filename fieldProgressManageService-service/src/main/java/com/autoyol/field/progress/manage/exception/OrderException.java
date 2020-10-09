package com.autoyol.field.progress.manage.exception;

public class OrderException extends RuntimeException {
    private static final long serialVersionUID = 2363617490984885270L;

    public OrderException(String msg) {
        super(msg);
    }

    public OrderException(String code, String msg) {
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
