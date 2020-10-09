package com.autoyol.field.progress.manage.config.dynamic;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface DynamicSource {

    String value() default "";
}
