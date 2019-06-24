package com.aliware.counter;

/**
 * 计数器
 * 1. 考虑到评测时间不会太长，直接用一个数组保存从启动时间至今的所有计数
 */
public class Counter {

    private final int maxLen;

    private final long basePos;

    private final long[] counters;

    public Counter(int maxLen, long basePos) {
        this.maxLen = maxLen;
        this.basePos = basePos;
        counters = new long[maxLen];
    }

    private int getExactPos(long pos) {
        return (int) (pos - basePos);
    }

    private void checkPos(long pos) {
        long exactPos = pos - basePos;
        if (exactPos > Integer.MAX_VALUE
                || exactPos >= maxLen) {
            throw new RuntimeException("超出可计数范围, pos=" + pos + ", maxLen=" + maxLen);
        }
    }

    public void incr(long pos) {
        checkPos(pos);
        int exactPos = getExactPos(pos);
        counters[exactPos]++;
    }

    public long get(long pos) {
        checkPos(pos);
        int exactPos = getExactPos(pos);
        return counters[exactPos];
    }

}
