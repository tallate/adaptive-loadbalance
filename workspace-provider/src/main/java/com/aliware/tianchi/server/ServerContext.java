package com.aliware.tianchi.server;

import java.util.concurrent.atomic.AtomicLong;

public class ServerContext {

    /**
     * 并发连接数Concurrent-Connection-Count
     */
    private static final AtomicLong CCC = new AtomicLong();

    public static void incr() {
        CCC.incrementAndGet();
    }

    public static void decr() {
        CCC.decrementAndGet();
    }

    public static long getCurrentCCC() {
        return CCC.get();
    }

}
