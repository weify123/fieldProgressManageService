package com.autoyol.field.progress.manage.constraint;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelElement {
    /**
     * 对应excel的第一行（标题行）
     *
     * @return
     */
    String field();

    int index();
}
