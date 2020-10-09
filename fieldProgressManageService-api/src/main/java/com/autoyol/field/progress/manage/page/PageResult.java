package com.autoyol.field.progress.manage.page;

import com.autoyol.doc.annotation.AutoDocProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 分页
 * @author: feiyu.wei
 * @create: 2019-12-13
 **/
@Data
public class PageResult implements Serializable {

    // 当前页
    @AutoDocProperty("要获取的页数")
    private int pageNo = 1;// 开始页数
    @AutoDocProperty("分页大小")
    private int pageSize = 20; // 分页大小
    // 总记录数
    @AutoDocProperty(value = "总记录数")
    private long total;
    //总页数
    @AutoDocProperty(value = "总页数")
    private int pages;

    public PageResult() {
    }

    public PageResult(PageInfo pageInfo) {
        this.pageNo = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.pages = pageInfo.getPages();
        this.total = pageInfo.getTotal();
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo < 1 ? 1 : pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize < 1 ? 20 : pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        this.setPages((int) this.total % this.pageSize == 0 ?
                (int) this.total / this.pageSize :
                (int) this.total / this.pageSize + 1);
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
