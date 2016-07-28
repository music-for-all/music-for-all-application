package com.musicforall.common.query;

/**
 * Created by kgavrylchenko on 21.07.16.
 */
public final class QueryUtil {
    private static final String PERCENT = "%";

    public static String prefixLike(String str) {
        return PERCENT + str;
    }

    public static String postfixLike(String str) {
        return str + PERCENT;
    }

    public static String like(String str) {
        return PERCENT + str + PERCENT;
    }

    private QueryUtil() {
    }
}