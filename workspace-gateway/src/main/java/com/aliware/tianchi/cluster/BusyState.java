package com.aliware.tianchi.cluster;

import com.aliware.RandomUtil;
import com.aliware.cluster.Cluster;
import com.aliware.cluster.Server;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aliware.config.LoadConfig.LOAD_THRESHOLD;

/**
 * 集群为繁忙状态
 * 1. 按每个服务器的负载比率分配请求
 */
public class BusyState implements ClusterState {

    private static final Logger logger = LoggerFactory.getLogger(BusyState.class);

    @Override
    public boolean match(Cluster cluster) {
        return cluster.getAvgLoad() >= LOAD_THRESHOLD;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Server select(Cluster cluster) {
        logger.info("BusyState selected");
        Set<Map.Entry<Byte, Server>> entrySet = cluster.getServerMap().entrySet();
        Map.Entry<Byte, Server>[] entries = entrySet
                .toArray(new Map.Entry[0]);
        List<Double> weights = Arrays.stream(entries)
                .map(entry -> entry.getValue().getLoad())
                .collect(Collectors.toList());
        int pos = RandomUtil.randOne(weights);
        return entries[pos].getValue();
    }
}
