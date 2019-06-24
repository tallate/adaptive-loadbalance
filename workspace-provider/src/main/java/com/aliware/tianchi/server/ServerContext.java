package com.aliware.tianchi.server;

import com.aliware.TimeUtil;
import com.aliware.counter.Counter;

import static com.aliware.config.LoadConfig.MAX_COLLECT_TIME_LENGTH;

public class ServerContext {

    /**
     * 存储每一秒的请求量
     */
    private static final Counter REQ_COUNTER = new Counter(MAX_COLLECT_TIME_LENGTH, TimeUtil.getCurrentSecond());

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
