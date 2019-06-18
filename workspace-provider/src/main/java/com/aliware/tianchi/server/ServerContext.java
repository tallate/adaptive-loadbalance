package com.aliware.tianchi.server;

import com.aliware.CircularQueue;

import java.util.Comparator;

import static com.aliware.config.SamplingConfig.MAX_THROUGHPUT;
import static com.aliware.config.SamplingConfig.SAMPLING_TIME_INTERVAL;

public class ServerContext {

    /**
     * 使用一个循环队列存储请求时间
     */
    private static final CircularQueue<Long> CIRCULAR_QUEUE = new CircularQueue<>(MAX_THROUGHPUT);

    /**
     * 记录一个请求到队列
     */
    public static void pushReq() {
        CIRCULAR_QUEUE.offer(System.currentTimeMillis());
    }

    /**
     * 获取当前吞吐量
     */
    public static long getCurrentThroughput() {
        // 当前请求
        int frontPos = CIRCULAR_QUEUE.getFrontPos();
        // SAMPLING_TIME_INTERVAL ms 前的请求
        int targetPos = CIRCULAR_QUEUE.binsearch(System.currentTimeMillis() - SAMPLING_TIME_INTERVAL, Comparator.naturalOrder());
        // 两者做差求中间发了多少个请求
        return CIRCULAR_QUEUE.getInterval(targetPos, frontPos);
    }

}
