package com.autoyol.field.progress.manage.config.dynamic;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
@Component
public class DataSourceAspect implements Ordered {

    @Resource
    DynamicDataSource dynamicDataSource;

    @Pointcut(value = "@annotation(com.autoyol.field.progress.manage.config.dynamic.DynamicSource)")
    private void dataSource() {

    }

    @Around("dataSource()")
    public Object dataSourceAspect(ProceedingJoinPoint point) throws Throwable {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = null;
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        methodSignature = (MethodSignature) signature;
        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        DynamicSource dynamicSource = currentMethod.getAnnotation(DynamicSource.class);
        if (StringUtils.isNotEmpty(dynamicSource.value())) {
            DataSourceContextHolder.setDataSourceType(dynamicSource.value());
        } else {
            DataSourceContextHolder.setDataSourceType("mysqlDataSource");
        }

        try {
            return point.proceed();
        } finally {
            DataSourceContextHolder.clearDataSourceType();
        }

    }

    @Override
    public int getOrder() {
        return 1;
    }
}
