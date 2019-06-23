package com.aliware.config;

/**
 */
public class LoadConfig {

    /**
     * 负载均衡算法切换阈值
     * 超出容许处理能力到一定程度后，切换负载均衡算法
     */
    public static final double LOAD_THRESHOLD = 0.001;

    public static final double BOOST_FACTOR = 2;

    /**
     * TODO: 骚操作应该去掉，现实环境中绝对不能这么用
     */
    public static double getHostFactor(byte hostCode) {
        return hostCode;
    }

}
