package com.autoyol.field.progress.manage.mq;

public interface MQConstraint {

    /**
     * 普通短信队列
     */
    String SEND_SMS_NORMAL_CHANNEL_QUEUE = "auto_send_sms_normal_channel_queue";
    /**
     * 供应商操作队列
     */
    String AUTO_SUPPLIER_OPR_QUEUE = "auto_supplier_opr_queue";
    /**
     * 供应商公司操作队列
     */
    String AUTO_TENANT_OPR_QUEUE = "auto_tenant_opr_queue";

    /**
     * 短信模板名
     */
    String TACK_ADD_WAIT_DISPATCH = "tack_add_wait_dispatch";
    String BACK_ADD_WAIT_DISPATCH = "back_add_wait_dispatch";
    String WAIT_ORDER_PROCEDURE_MSG = "订单生成，代派单发送短信";
    String PICK_WAIT_RECEIPT = "pick_wait_receipt";
    String BACK_WAIT_RECEIPT = "back_wait_receipt";
    String BACK_WAIT_RECEIPT_MSG = "待接单发送车管家短信";
    String ADMIN_PICK_WAIT_RECEIPT = "admin_pick_wait_receipt";
    String ADMIN_BACK_WAIT_RECEIPT = "admin_back_wait_receipt";
    String ADMIN_BACK_WAIT_RECEIPT_MSG = "待接单发送车辆管理员短信";
    String RENTER_PICK_WAIT_RECEIPT_1 = "renter_pick_wait_receipt_1";
    String RENTER_PICK_WAIT_RECEIPT_2 = "renter_pick_wait_receipt_2";
    String RENTER_BACK_WAIT_RECEIPT_1 = "renter_back_wait_receipt_1";
    String RENTER_BACK_WAIT_RECEIPT_2 = "renter_back_wait_receipt_2";
    String RENTER_BACK_WAIT_RECEIPT_3 = "renter_back_wait_receipt_3";
    String RENTER_BACK_WAIT_RECEIPT_MSG = "待接单发送租客短信";
    String OWNER_PICK_WAIT_RECEIPT = "owner_pick_wait_receipt";
    String OWNER_BACK_WAIT_RECEIPT = "owner_back_wait_receipt";
    String OWNER_BACK_WAIT_RECEIPT_MSG = "待接单发送车主短信";



    /**
     * 邮件模板名
     */
    String TACK_CANCEL = "tack_cancel";
    String BACK_CANCEL = "back_cancel";

    String TACK_CHANGE = "tack_change";
    String BACK_CHANGE = "back_change";
}
