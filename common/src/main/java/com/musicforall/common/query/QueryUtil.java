package com.musicforall.common.query;

import com.musicforall.common.Constants;

/**
 * Created by kgavrylchenko on 21.07.16.
 */
public final class QueryUtil {

    public static String prefixLike(String str) {
        return Constants.PERCENT + str;
    }

    public static String postfixLike(String str) {
        return str + Constants.PERCENT;
    }

    public static String like(String str) {
        return Constants.PERCENT + str + Constants.PERCENT;
    }

    private QueryUtil() {
    }
}