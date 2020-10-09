package com.autoyol.field.progress.manage.constraint;

import java.util.stream.Stream;

public enum FieldErrorCode {
    EXPORT_DATA_EMPTY("F00001", "没有可以导出的数据"),
    ORDER_EXIST("F00002", "订单已存在"),
    ORDER_NOT_EXIST("F00003", "交易订单不存在"),
    TACK_BACK_ORDER_NOT_EXIST("F00004", "取还车订单不存在"),
    TACK_BACK_ORDER_EXIST("F00005", "取还车订单已存在"),
    TACK_BACK_FEE_NOT_EXIST("F00006", "费用配置为空"),
    USER_NOT_EXIST("F00007", "用戶不存在或已禁用"),
    USER_PW_ERROR("F00008", "密码不正确"),
    TACK_BACK_NOT_EXIST("F00009", "取还车订单不存在"),
    EDIT_FAIL("F00010", "参数不全, 不可编辑或提交"),
    OPERATE_NO_PERMIT("F00011", "操作不允许"),
    SCHEDULE_NO_PERMIT_EDIT("F00012", "该取还车订单调度状态不允许编辑"),
    SCHEDULE_NO_PERMIT_SUBMIT("F00013", "该取还车订单调度状态不允许提交"),
    COMPANY_ID_NOT_EXIST("F00014", "公司不存在"),
    APP_USER_IS_EMPTY("F00015", "请选择取送车人员"),
    APP_USER_NOT_EXIST("F00016", "用户不存在"),
    ORDER_CITY_NOT_USER_CITY("F00017", "只能分配订单所属城市用户"),
    REPEAT_NO_PERMIT("F00018", "不允许重复提交"),
    CONF_NOT_COMPLETE("F00019", "配置不完整,请联系开发完整"),
    NOT_ONE_VEHICLE("F00020", "车辆不同,不可续租"),
    NOT_ONE_RENTER("F00021", "租客不同,不可续租"),
    OIL_INPUT_ERROR("F00022", "油量輸入不能大于油刻分母表"),
    FILE_LIMITED("F00023", "最多只能上传50张"),
    NO_PERMIT_SUBMIT("F00024", "不允许直接提交"),
    NOT_WAIT_DISPATCH_NO("F00025", "非待派单状态订单无法批量分配"),
    SCHEDULE_NO_PERMIT_CANCEL("F00026", "该取还车订单调度状态不允许取消"),
    SCHEDULE_NO_PERMIT_BACK("F00027", "该取还车订单调度状态不允许回退"),
    USER_STATUS_DISABLE("F00028", "用户已注销"),
    ID_CARD_EXIST("F00029", "身份证号重复，请重新输入"),
    CONTACT_MOBILE_EXIST("F00030", "手机号重复，请换个手机号"),
    USER_NAME_EXIST("F00031", "已有该用户姓名，请重新输入"),
    USER_MOBILE_EXIST("F00032", "该手机号已有账号，请换个手机号"),
    USER_STATUS_DISABLE_NOT_ADD("F00033", "不允许新增注销状态"),
    NOT_ALLOWED_ADD_CITY("F00034", "您不允许新增该城市"),
    ;
    private String code;
    private String text;

    FieldErrorCode(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static FieldErrorCode getObj(String code){
        return Stream.of(FieldErrorCode.values()).filter(field -> field.code.equals(code)).findFirst().orElse(null);
    }
}
