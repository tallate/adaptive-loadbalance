package com.aliware.counter;

import java.util.concurrent.atomic.LongAdder;

/**
 * 计数器
 * 1. 考虑到评测时间不会太长，直接用一个数组保存从启动时间至今的所有计数
 */
public class Counter {

    private final int maxLen;

    private final long basePos;

    private final LongAdder[] counters;

    public Counter(int maxLen, long basePos) {
        this.maxLen = maxLen;
        this.basePos = basePos;
        counters = new LongAdder[maxLen];
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
        counters[exactPos].increment();
    }

    public long get(long pos) {
        checkPos(pos);
        int exactPos = getExactPos(pos);
        return counters[exactPos].longValue();
    }

}
