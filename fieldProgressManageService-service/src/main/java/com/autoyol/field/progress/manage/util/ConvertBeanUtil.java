package com.autoyol.field.progress.manage.util;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

public class ConvertBeanUtil extends BeanUtils {
    static {
        ConvertUtils.register(new DateConverter(null), java.util.Date.class);
        ConvertUtils.register(new BigDecimalConverter(BigDecimal.ZERO), java.math.BigDecimal.class);
    }
    public static void copyProperties(Object target, Object source) {
        BeanUtils.copyProperties(source, target);
    }
}
