package com.autoyol.field.progress.manage.util.http;

/**
 * 系统错误码
 * 错误码共6位，前三位代表试驾服务系统“326”，后三位自定义
 * @author tao.sun
 *
 */
public enum ErrorCode {
	SUCCESS("000000","success"),
	SYS_ERROR("999999","系统异常"),
	FAILED("900000","操作失败！"),
	REQUEST_PARAM_ERROR("900001","请求参数错误"),
	REQUEST_FALLBACK("900002","触发降级"),

	PROMOTE_LINK_IS_NO("110007","推广链接不存在"),

	RENT_OWNER_IS_NULL("40003","长租车主信息不能为空"),
	TOKEN_IS_NULL("40002","Token不能为空"),
	MEMNO_IS_NULL("40001","会员号不能为空");

	private String resCode;
	private String resMsg;

	ErrorCode(String resCode, String resMsg) {
		this.resCode = resCode;
		this.resMsg = resMsg;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	public static ErrorCode dynamicErrorText(ErrorCode errorCode, String resMsg) {
		errorCode.setResMsg(resMsg);
		return errorCode;
	}
}
