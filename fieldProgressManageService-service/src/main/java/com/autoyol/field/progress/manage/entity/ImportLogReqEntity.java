package com.autoyol.field.progress.manage.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ImportLogReqEntity extends BasePageForEntity{
    private static final long serialVersionUID = 7451185806385564968L;

    private final Integer businessType;

    public ImportLogReqEntity(Integer businessType, int start, int pageSize) {
        super(start, pageSize);
        this.businessType = businessType;
    }

    public final Integer getBusinessType() {
        return businessType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
