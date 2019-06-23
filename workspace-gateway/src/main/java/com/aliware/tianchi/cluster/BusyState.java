package com.aliware.tianchi.cluster;

import com.aliware.IntervalSelector;
import com.aliware.RandomUtil;
import com.aliware.cluster.Cluster;
import com.aliware.cluster.Server;
import com.aliware.config.LoadConfig;
import com.aliware.log.LogUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aliware.config.LoadConfig.EXTEND_FACTOR;
import static com.aliware.config.LoadConfig.LOAD_THRESHOLD;

/**
 * 集群为繁忙状态
 * 1. 按每个服务器的负载比率分配请求
 */
public class BusyState implements ClusterState {

    @Override
    public boolean match(Cluster cluster) {
        // 超出
        return cluster.getAvgLoad() - 1 >= LOAD_THRESHOLD;
    }

    /**
     * 为了让超出阈值的负载在负载均衡时更明显，将这些负载扩大
     */
    private double boostLoad(double load) {
        return load <= 1 ?
                load :
                1 + (load - 1) * EXTEND_FACTOR;
    }

    private IntervalSelector intervalSelector = new IntervalSelector(10, true);

    @SuppressWarnings("unchecked")
    @Override
    public Server select(Cluster cluster) {
        Set<Map.Entry<Byte, Server>> entrySet = cluster.getServerMap().entrySet();
        Map.Entry<Byte, Server>[] entries = entrySet
                .toArray(new Map.Entry[0]);
        // 通过每个服务器负载计算出负载时的权重
        List<Double> weights = Arrays.stream(entries)
                .map(entry -> {
                    // 大部分情况下过载在数值上不会特别明显，boost过程扩大了这部分的影响
                    double load = boostLoad(entry.getValue().getLoad());
                    double loadFactor = entry.getValue().getLoad() == 0 ?
                            1 :
                            1.0 / load;
                    double hostFactor = LoadConfig.getHostFactor(entry.getKey());
                    return loadFactor * hostFactor;
                })
                .collect(Collectors.toList());
        // LOG: 记录所有计算出的权重
        if (intervalSelector.get()) {
            LogUtil.info("所有权重=[" + weights.get(0) + ", " + weights.get(1) + ", " + weights.get(2) + "" + "]");
        }
        // 计算赋权随机数
        int pos = RandomUtil.randOne(weights);
        return entries[pos].getValue();
    }
}
