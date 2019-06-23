package com.aliware.tianchi.cluster;

import com.aliware.RandomUtil;
import com.aliware.cluster.Cluster;
import com.aliware.cluster.Server;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.aliware.config.LoadConfig.LOAD_THRESHOLD;
import static com.aliware.config.LoadConfig.getHostFactor;

/**
 * 集群平均负载较低的状态
 * 1. 随机算法
 */
public class RelaxState implements ClusterState {

    private static final Logger logger = LoggerFactory.getLogger(RelaxState.class);

    @Override
    public boolean match(Cluster cluster) {
        return cluster.getAvgLoad() - 1 < LOAD_THRESHOLD;
    }

    private static final List<Double> WEIGHTS = new ArrayList<>();

    static {
        WEIGHTS.add(getHostFactor((byte) 1));
        WEIGHTS.add(getHostFactor((byte) 2));
        WEIGHTS.add(getHostFactor((byte) 3));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Server select(Cluster cluster) {
        Set<Map.Entry<Byte, Server>> entrySet = cluster.getServerMap().entrySet();
        Map.Entry<Byte, Server>[] entries = entrySet
                .toArray(new Map.Entry[0]);
        int pos = RandomUtil.randOne(WEIGHTS);
        return entries[pos].getValue();
    }

}
