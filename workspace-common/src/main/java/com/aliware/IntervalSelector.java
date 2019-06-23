package com.aliware;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 隔几个选一个
 */
public class IntervalSelector {

    private int interval;

    private AtomicLong counter;

    /**
     * @param interval 间隔
     * @param first    是否选中第一个
     */
    public IntervalSelector(int interval, boolean first) {
        this.interval = interval;
        if (first) {
            counter = new AtomicLong();
        } else {
            counter = new AtomicLong(1);
        }
    }

    public boolean get() {
        return counter.getAndIncrement() % interval == 0;
    }

}
