package com.autoyol.field.progress.manage.util.valid;

import com.autoyol.field.progress.manage.cache.CacheConstraint;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

public class ValidationUtils {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> ValidResult validateEntity(T obj) {
        ValidResult result = new ValidResult();
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if (CollectionUtils.isNotEmpty(set)) {
            result.setHasErrors(true);
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<T> cv : set) {
                stringBuilder.append(cv.getMessage());
                stringBuilder.append(CacheConstraint.SPLIT_COMMA);
            }
            result.setErrorMsg(stringBuilder.substring(0, stringBuilder.length() - 1));
        }
        return result;
    }
}
