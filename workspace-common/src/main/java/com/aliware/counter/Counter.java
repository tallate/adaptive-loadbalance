package com.aliware.counter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 */
public class Counter<T> {

    private ConcurrentMap<T, AtomicLong> counterMap = new ConcurrentHashMap<>();

    public void incr(T t) {
        AtomicLong newCounter = new AtomicLong();
        AtomicLong counter = counterMap.putIfAbsent(t, newCounter);
        if (counter != null) {
            counter.incrementAndGet();
        } else {
            newCounter.incrementAndGet();
        }
    }

    public long getOrDefault(T t, Long d) {
        AtomicLong counter = counterMap.get(t);
        return counter == null ?
                (d == null ? 0 : d) :
                counter.get();
    }

}
