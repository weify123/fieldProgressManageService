package com.autoyol.field.progress.manage.enums;

import java.util.stream.Stream;

public enum CommonEnum {
    DELETED("1", "已删除"),
    NOT_DELETE("0", "未删除"),

    CERT_UPLOADED_YES("1", "已上传"),
    CERT_UPLOADED_NO("0", "未上传"),

    LOG_TYPE_USER_INFO("0", "用户人员导入"),
    LOG_TYPE_CITY_LEVEL("1", "城市等级导入"),
    LOG_TYPE_ORDER_SET("2", "订单高级用户设定导入"),
    LOG_TYPE_USER_KQ("3", "用户考勤信息导入"),
    LOG_TYPE_STORE_BATCH_ADD("4", "门店信息批量新增导入"),
    LOG_TYPE_STORE_BATCH_UPDATE("5", "门店信息批量修改导入"),
    LOG_TYPE_SUPPLIER_BATCH_UPDATE("6", "供应商批量修改导入"),

    ;

    private String code;
    private String msg;

    CommonEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static CommonEnum getEnumByCode(String code) {
        return Stream.of(CommonEnum.values()).filter(commonEnum -> commonEnum.getCode().equals(code)).findFirst().orElse(null);
    }
}
