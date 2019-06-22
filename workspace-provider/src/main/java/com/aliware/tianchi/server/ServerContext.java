package com.aliware.tianchi.server;

import com.aliware.TimeUtil;
import com.aliware.counter.Counter;

public class ServerContext {

    /**
     * 存储每一秒的请求量
     */
    private static final Counter<Long> REQ_COUNTER = new Counter<>();

    public static void countReq() {
        long currentSecond = TimeUtil.getCurrentSecond();
        REQ_COUNTER.incr(currentSecond);
    }

    /**
     * @param t 时间 / s
     */
    public static long getReqCount(long t) {
        return REQ_COUNTER.get(t);
    }

}
