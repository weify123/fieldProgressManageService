package com.autoyol.field.progress.manage.cache;

public interface CacheConstraint {
    String CITY_KEY = "CITY_HASH_KEY";
    String DICT_KEY = "DICT_HASH_KEY";
    String APP_USER_INFO_ID_NO_KEY = "APP_USER_INFO_ID_NO_KEY";
    String PICK_DELIVER_CURRENT_INCR_KEY = "PICK_DELIVER_CURRENT_INCR_KEY";
    String ATTR_LABEL_KEY = "ATTR_LABEL_KEY";

    String DATE_YYYY_MM_DD_LINE = "yyyy-MM-dd";
    String DATE_YYYY_MM_DD_SLASH = "yyyy/MM/dd";
    String DATE_YYYY_MM_DD_SPOT = "yyyy.MM.dd";
    String DATE_YYYY_MM_DD_CONTACT = "yyyyMMdd";
    String DATE_FULL = "yyyy-MM-dd HH:mm:ss";
    String DATE_FULL_NO_CONTACT = "yyyyMMddHHmmss";
    String DATE_FULL_NO_CONTACT_1 = "yyyyMMDDHHmmss";
    String DATE_FULL_NO_CONTACT_2 = "YYYYMMDDHHmmss";
    String DATE_NO_SECOND = "yyyy-MM-dd HH:mm";
    String DATE_NO_SECOND_1 = "YYYY-MM-DD HH:mm";
    String DATE_MONTH = "MMddHHmmss";
    String DATE_TIME = "HH:mm:ss";
    String DATE_TIME_NO_SECOND = "HH:mm";
    String DATE_CST_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
    // Jul 17 2017  3:57PM
    String DATE_EN_FORMAT = "MMM d yyyy h:mma";

    String M = "M";
    String CST = "CST";
    String SYS = "sys";
    String CLEAR = "clear";
    String CHAR_L = "L";
    String CHAR_T = "T";
    String STRING_EMPTY = "";
    String CH_SUCCESS = "成功";
    String CH_FAIL = "失败";
    String CH_COUNT = "条";
    String CH_DI = "第";
    String CH_HANG = "行";
    String DAY = "日";
    String CQ = "出勤";
    String NO_LOCATE = "未定位";
    String TACK_BACK_ORDER_CHANGE = "取送订单变更";
    String TACK = "tack";
    String BACK = "back";
    String WB = "外包";

    int INT_ONE = 1;
    int INT_TOW = 2;
    int INT_THREE = 3;
    int INT_FOUR = 4;
    int INT_FIVE = 5;
    int INT_SIX = 6;
    int INT_SEVEN = 7;
    int INT_EIGHT = 8;
    int INT_NINE = 9;
    int INT_FOURTEEN = 14;
    int INT_ELEVEN = 11;
    int INT_SIXTEEN = 16;
    int INT_SEVENTEEN = 17;
    int INT_EIGHTEEN = 18;
    int INT_TWENTY_TWO = 22;
    int INT_TWENTY_THREE = 23;
    int INT_THIRTY = 30;
    int INT_THIRTY_ONE = 31;
    int INT_FIFTY_NINE = 59;
    int INT_THOUSAND = 1000;
    int NEG_ZERO = 0;
    int NEG_ONE = -1;
    int NEG_TOW = -2;
    int NEG_THREE = -3;
    int NEG_FOUR = -4;
    int NEG_FIVE = -5;
    int NEG_SIX = -6;

    String SPLIT_COMMA = ",";
    String SPLIT_DAWN = "、";
    String SPLIT_LINE = "|";
    String SPLIT_HORIZONTAL = "-";
    String SPLIT_POINT = "\\.";
    String SPLIT_AND = "&";
    String SPLIT_DOLLAR = "$";
    String SPLIT_SEMICOLON = ";";
    String EMPTY = "\"\"";
    String CONTAIN_NUMBER = ".*[0-9]+.*";

    String ISO_8859 = "ISO-8859-1";

    String CITY_LEVEL_TYPE = "city_level_type";

    String USER_STATUS = "user_status";
    String EMPLOY_TYPE = "employ_type";
    String STAR_LEVEL = "star_level";
    String STATION_TYPE = "station_type";
    String DISTRIBUTE_TYPE = "distribute_type";
    String EFFECTIVE_TYPE = "effective_type";

    String CERT_TYPE = "cert_type";
    String SIDE_TYPE = "side_type";
    String ZONE_TYPE = "zone_type";
    String ATTENDANCE_TYPE = "attendance_type";
    String MARK_TYPE = "mark_type";
    String SERVE_TYPE = "serve_type";
    String SERVICE_TYPE = "service_type";
    String TOPIC_TYPE = "topic_type";

    String FLOW_NAME_TYPE = "flow_name_type";
    String FLOW_SERVER_TYPE = "flow_server_type";
    String FLOW_NODE_NAME_TYPE = "flow_node_name_type";

    String PUBLISH_TYPE = "publish_type";
    String PLATFORM_TYPE = "platform_type";
    String COMPANY_FIRST_TYPE = "company_first_type";

    String LOCATE_STATUS = "locate_status";
    String LOCATE_SORT = "locate_sort";

    String PICK_BACK_OPR_TYPE = "pick_back_opr_type";
    String PICK_DELIVER_SCHEDULE_STATUS = "pick_deliver_schedule_status";
    String TRANS_SOURCE = "trans_source";
    String TRANS_SCENE_SOURCE = "scene_source";
    String OFFLINE_ORDER_TYPE = "offline_order_type";
    String DETECT_STATUS = "detect_status";
    String VEHICLE_TYPE = "vehicle_type";
    String ENGINE_TYPE = "engine_type";
    String DISPATCH_TYPE = "dispatch_type";
    String VEHICLE_STATUS = "vehicle_status";
    String DELAY_REASON = "delay_reason";
    String IS_OWN_USER = "is_own_user";
    String BEAR_TYPE = "bear_type";
    String SUBSIDY_OWNER_REASON_TYPE = "subsidy_owner_reason_type";
    String SUBSIDY_RENT_REASON_TYPE = "subsidy_rent_reason_type";
    String ADJ_PRICE_OWNER_REASON_TYPE = "adj_price_owner_reason_type";
    String ADJ_PRICE_RENT_REASON_TYPE = "adj_price_rent_reason_type";
    String EXPENSE_FUEL = "expenseFuel";
    String FUEL_ATTR = "fuelattr";
    String EXPENSE_PARK = "expensePark";
    String PARK_ATTR = "parkattr";
    String EXPENSE_TRAFFIC = "expenseTraffic";
    String TRAFFIC_ATTR = "trafficattr";

    String CHANNEL_TYPE = "channel";
    String SERVICE_CLASS = "service_class";
    String ORDER_CHANNEL_SOURCE = "order_channel_source";


    String ID_REG = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    String PHONE_REG = "^1[3456789]\\d{9}$";
    String MAIL_REG = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    String CAT_TRANSACTION_GET_SHORT_URL = "get_short_url";
    String CAT_TRANSACTION_GET_MEMBER_CORE_INFO = "getMemberCoreInfo";

    String NEW_USER_ID = "USER_ID";
    String NEW_USER_NAME = "USER_NAME";

    String GD_MAP_URL = "https://restapi.amap.com/v3/direction/driving";

    String sqlServer_datasource ="sqlServerDataSource";
    String sqlServer_switch ="Y";
    String sqlServer_service_type ="p_stype";
    String sqlServer_new ="p_flowst_2";
    String sqlServer_tack_deliver ="p_flowst_3";
    String sqlServer_cancel ="p_flowst_4";
    String sqlServer_serverType_code ="p_autotasendc1";
    String sqlServer_oprType_code ="p_autotasendc2";
    String sqlServer_city_code ="p_autoc011";
    String sqlServer_is_supply ="p_autotasendc1_17";
    String sqlServer_company ="p_company";
    String sqlServer_owen_type ="p_qsctype";
    String sqlServer_order_type ="p_ordertype";
    String sqlServer_field_app_type ="p_appstate";
    String sqlServer_vehicle_status_type ="p_vstate";
    String sqlServer_deliver_delay_reason_type ="p_latenote";
    String sqlServer_trans_source_type ="p_source";
    String sqlServer_risk_control_type ="p_checkstate";
    String sqlServer_supper_supple_risk_type ="p_ssarisks";
    String sqlServer_dispatch_type ="p_sendtype";
    String sqlServer_day_mileage_type ="p_daymileage";
    String sqlServer_bear_type ="p_info05bears";
    String sqlServer_owner_ajd_type ="p_opreason";
    String sqlServer_renter_ajd_type ="p_rpreason";
    String sqlServer_oil_expense_type ="p_info05c075";
    String sqlServer_park_expense_type ="p_info05c076";
    String sqlServer_traffic_expense_type ="p_info05c077";
    String sqlServer_station_type ="p_employment";

    String sqlServer_mq_routing_key ="auto.sync.ren.yun.#";
    String sqlServer_mq_exchange ="auto.sync.ren.yun";
    // 流程->仁云，公司同步
    String sqlServer_company_mq_routing_key ="auto.sync.company.ren.yun.#";
    String sqlServer_company_mq_exchange ="auto.sync.company.ren.yun";
    // 仁云->流程，公司同步
    String sqlServer_field_company_mq_routing_key = "ren.yun.sync.auto.company.#";
    String sqlServer_field_company_mq_exchange = "ren.yun.sync.auto.company";
    String sqlServer_company_mq_queue = "queue.ren.yun.sync.auto.company";
    // 流程->仁云，供应商同步
    String sqlServer_supplier_mq_routing_key ="auto.sync.supplier.ren.yun.#";
    String sqlServer_supplier_mq_exchange ="auto.sync.supplier.ren.yun";
    // 仁云->流程，供应商同步
    String sqlServer_field_supplier_mq_exchange ="ren.yun.sync.auto.supplier";
    String sqlServer_field_supplier_mq_routing_key = "ren.yun.sync.auto.supplier.#";
    String sqlServer_supplier_mq_queue = "queue.ren.yun.sync.auto.supplier";

    // 流程->仁云，车管家同步
    String sqlServer_app_user_mq_routing_key ="auto.sync.app_user.ren.yun.#";
    String sqlServer_app_user_mq_exchange ="auto.sync.app_user.ren.yun";
    String sqlServer_app_user_mq_queue ="auto.sync.app_user.ren.yun.queue";

    // 仁云->流程，车管家同步
    String sqlServer_field_user_mq_routing_key ="ren.yun.sync.auto.user.#";
    String sqlServer_field_user_mq_exchange ="ren.yun.sync.auto.user";
    String sqlServer_field_user_mq_queue ="queue.ren.yun.sync.auto.user";

    // 流程->仁云，考勤同步
    String sqlServer_attendance_mq_routing_key ="auto.sync.attendance.ren.yun.#";
    String sqlServer_attendance_mq_exchange ="auto.sync.attendance.ren.yun";
    String sqlServer_attendance_mq_queue ="auto.sync.attendance.ren.yun.queue";

    // 仁云->流程，考勤同步
    String sqlServer_field_attendance_mq_routing_key ="ren.yun.sync.auto.attendance.#";
    String sqlServer_field_attendance_mq_exchange ="ren.yun.sync.auto.attendance";
    String sqlServer_field_attendance_mq_queue ="queue.ren.yun.sync.auto.attendance";
}
