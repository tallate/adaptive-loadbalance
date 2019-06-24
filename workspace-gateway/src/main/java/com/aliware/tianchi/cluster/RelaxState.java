package com.aliware.tianchi.cluster;

import com.aliware.RandomUtil;
import com.aliware.cluster.Cluster;
import com.aliware.cluster.Server;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 集群平均负载较低的状态
 * 1. 加权随机
 * 2. 必须放到最后
 */
public class RelaxState implements ClusterState {

    @Override
    public boolean match(Cluster cluster) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Server select(Cluster cluster) {
        List<Server> servers = cluster.getServersAsList();
        List<Double> weights = servers.stream()
                .map(Server::getWeight)
                .collect(Collectors.toList());
        int pos = RandomUtil.randOne(weights);
        return servers.get(pos);
    }

}
