package com.musicforall.common.dao;

/**
 * Created by kgavrylchenko on 12.08.16.
 */
public class QueryParams {
    private final int maxCount;
    private final int offset;

    public QueryParams(int maxCount, int offset) {
        this.maxCount = maxCount;
        this.offset = offset;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "QueryParams{" +
                "maxCount=" + maxCount +
                ", offset=" + offset +
                '}';
    }
}
