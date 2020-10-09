package com.autoyol.field.progress.manage.util;

import java.util.concurrent.atomic.AtomicInteger;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;

public class ImportLogUtil {

    public static String buildFailLogMsg(AtomicInteger atomicInteger, StringBuffer builder) {
        if (atomicInteger.get() == NEG_ZERO) {
            return STRING_EMPTY;
        }
        return CH_FAIL + atomicInteger.get() + CH_COUNT + SPLIT_LINE + builder.substring(0, builder.length() - 1);
    }

    public static void appendLog(StringBuffer builder, AtomicInteger atomicInteger, int j) {
        builder.append(CH_DI).append(j).append(CH_HANG).append(SPLIT_COMMA);
        atomicInteger.incrementAndGet();
    }
}
