package com.aliware.config;

/**
 */
public class LoadConfig {

    /**
     * 上界，数太大一般是在预热或别的什么抖动
     */
    public static final double LOAD_THRESHOLD_UP_BOUND = 0.1;

    /**
     * 收敛速度
     */
    public static final double CONVERGENCE_SPEED = 3;

    /**
     * 负载指标最多收集多长时间
     */
    public static final int MAX_COLLECT_TIME_LENGTH = 1000;

    /**
     * 算法的初始权重
     */
    public static final double INIT_WEIGHT_SMALL = 1;
    public static final double INIT_WEIGHT_MEDIUM = 4;
    public static final double INIT_WEIGHT_LARGE = 5;

    /**
     * TODO: 骚操作应该去掉，现实环境中绝对不能这么用
     */
    public static double getHostFactor(byte hostCode) {
        return hostCode;
    }

}
