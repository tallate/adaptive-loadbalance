package com.aliware.tianchi.cluster;

import com.aliware.cluster.Cluster;
import com.aliware.cluster.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.aliware.config.LoadConfig.LOAD_THRESHOLD;
import static com.aliware.config.LoadConfig.getHostFactor;

/**
 * 集群平均负载较低的状态
 * 1. 随机算法
 */
public class RelaxState implements ClusterState {

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

    private static final int[] STEP_POS = new int[]{0, 1, 1, 2, 2, 2};

    private static final AtomicInteger STEP = new AtomicInteger();

    @SuppressWarnings("unchecked")
    @Override
    public Server select(Cluster cluster) {
        List<Server> servers = cluster.getServersAsList();
        int step = STEP.getAndIncrement();
        int pos = STEP_POS[step % 6];
        Server server = servers.get(pos);
        // 回滚避免越界
        if (step >= Integer.MAX_VALUE / 2) {
            STEP.set(0);
        }
        return server;
    }

}
