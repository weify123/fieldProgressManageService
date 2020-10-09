package com.autoyol.field.progress.manage.constraint;

public interface MsgConstraint {
    String EXCEL_IS_EMPTY = "表格内容为空";
    String EXCEL_CONTENT_CONTACT_MOBILE_MUL = "表格内联系人手机号重复";
    String EXCEL_CONTENT_STORE_NAME_MUL = "表格内门店简称重复";
    String EXCEL_CONTENT_STORE_FULL_NAME_MUL = "表格内门店全称重复";
    String EXCEL_CONTENT_ID_NO_MUL = "表格内身份证号重复";
    String EXIST_PART_FAIL = "存在导入失败的数据";
    String IMPORT_FAIL = "导入失败";

    String ORDER_IS_EXIST = "订单设定已存在,";

    String OPERATOR_IS_EMPTY = "操作人为空";
    String ADDR_IS_EMPTY = "地址为空";
    String ALREADY_LOGOUT = "用户未登录,请重新登陆";
    String BELONG_COMPANY_NOT_CONFIG = "您尚未配置所属服务公司";
    String DATE_FORMAT_ERROR = "日期格式错误";
    String START_IS_BEFORE_END = "开始时间不能大于结束时间";
    String START_IS_BEFORE_NOW = "开始时间不能小于当前时间";

    String STORE_NAME_EXIST = "已有该门店，请重新输入";
    String STORE_FULL_NAME_EXIST = "已有该门店，请重新输入";
    String SECOND_COMPANY_EXIST = "已有该服务公司二级分类，请重新输入";
    String CAR_PRO_NAME_EXIST = "服务产品名称已存在";
    String COMPANY_USER_EXIST = "当前服务公司下仍有外勤人员，无法失效";
    String COMPANY_SUPPLIER_EXIST = "当前服务公司下仍有供应商，无法失效";
    String INNER_VERSION_EXIST = "已有该内部版本号，请重新输入";
    String OUTER_VERSION_EXIST = "已有该外部版本号，请重新输入";
    String START_OR_END_TIME_FORMAT_ERROR = "开始/结束时间格式错误";
    String SERVER_NODE_EXIST = "当前服务节点的提醒时间有重叠,";
    String NOT_ALLOWED_SET_PASS = "您不允许设置该用户密码";

    String SURVEY_SHORT_URL_MARKS = "满意度调查模板短链接";

    String SURVEY_SHORT_BUSINESS_TYPE = "field-progress";

    String UPLOAD_TYPE_NOT_EXIST = "上传类型不存在";
}
