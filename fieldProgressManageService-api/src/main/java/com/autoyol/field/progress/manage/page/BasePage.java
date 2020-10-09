package com.autoyol.field.progress.manage.page;

import com.autoyol.doc.annotation.AutoDocIgnoreProperty;
import com.autoyol.doc.annotation.AutoDocProperty;
import com.autoyol.field.progress.manage.request.BaseRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;


public class BasePage extends BaseRequest {
	private static final long serialVersionUID = -17810774362979786L;

	@AutoDocProperty("要获取的页数")
	private int pageNo = 1;// 开始页数
	@AutoDocProperty("分页大小")
	private int pageSize = 20; // 分页大小
	@AutoDocIgnoreProperty
	private int start;// 开始的行数

	public BasePage() {
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo < 1 ? 1 : pageNo;
		this.limit();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize < 1 ? 20 : pageSize;
//		if(pageSize < 1 || pageSize > 10){
//			pageSize = 5;
//		}
		this.limit();
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	private void limit() {
		this.start = (pageNo - 1) * pageSize;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
