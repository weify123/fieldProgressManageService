package com.autoyol.field.progress.manage.request;

import com.autoyol.doc.annotation.AutoDocIgnoreProperty;

import java.io.Serializable;

public class BaseRequest implements Serializable {
	private static final long serialVersionUID = 2521228280343540730L;

	@AutoDocIgnoreProperty
	public String realName;
	@AutoDocIgnoreProperty
	public String handleUser;
	@AutoDocIgnoreProperty
	public Integer handleUserNo;

	public BaseRequest() {
	}

	public String getHandleUser() {
		return handleUser;
	}

	public void setHandleUser(String handleUser) {
		this.handleUser = handleUser;
	}

	public Integer getHandleUserNo() {
		return handleUserNo;
	}

	public void setHandleUserNo(Integer handleUserNo) {
		this.handleUserNo = handleUserNo;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Override
	public String toString() {
		return "BaseRequest{" +
				"realName='" + realName + '\'' +
				", handleUser='" + handleUser + '\'' +
				", handleUserNo=" + handleUserNo +
				'}';
	}
}
