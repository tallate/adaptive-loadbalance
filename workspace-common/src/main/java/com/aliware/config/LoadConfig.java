package com.aliware.config;

/**
 */
public class LoadConfig {

    /**
     * 预热时间 / ms
     */
    public static final int WARMUP_TIME = 20 * 1000;

    /**
     * 负载均衡算法切换阈值
     * 超出容许处理能力到一定程度后，切换负载均衡算法
     */
    public static final double LOAD_THRESHOLD = 0.05;

    public static final double EXTEND_FACTOR = 5;

}
