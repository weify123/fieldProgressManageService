package com.autoyol.field.progress.manage.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class BasePageForEntity implements Serializable {
    private static final long serialVersionUID = -3821249335410986585L;

    private final int start;

    private final int pageSize;

    public BasePageForEntity(int start, int pageSize) {
        this.start = start;
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
