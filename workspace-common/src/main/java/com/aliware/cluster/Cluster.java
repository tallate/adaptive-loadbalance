package com.aliware.cluster;

import com.aliware.counter.Counter;
import org.apache.dubbo.common.utils.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cluster implements Serializable {

    /**
     * 服务端属性
     */
    private final Map<Byte, Counter<Long>> serverCounterMap = new HashMap<>();

    /**
     * 服务器实时属性<hostCode, Server>
     */
    private final Map<Byte, Server> serverMap = new HashMap<>();

    /**
     * 按code进行排序
     */
    private final List<Server> servers = new ArrayList<>();

    /**
     * 平均负载
     */
    private double avgLoad;

    public Cluster() {
        // 初始化所有服务器，并给一个初始权重
        Server small = new Server((byte) 1).setWeight(1);
        Server medium = new Server((byte) 2).setWeight(2);
        Server large = new Server((byte) 3).setWeight(3);
        serverMap.put((byte) 1, small);
        serverMap.put((byte) 2, medium);
        serverMap.put((byte) 3, large);
        servers.add(small);
        servers.add(medium);
        servers.add(large);
        serverCounterMap.put((byte) 1, new Counter<>());
        serverCounterMap.put((byte) 2, new Counter<>());
        serverCounterMap.put((byte) 3, new Counter<>());
    }

    public Counter<Long> getServerCounterByHostCode(byte hostCode) {
        return serverCounterMap.get(hostCode);
    }

    public Server getServer(Byte hostCode) {
        return serverMap.get(hostCode);
    }

    public Server putServer(Byte hostCode, Server server) {
        return serverMap.put(hostCode, server);
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmptyMap(serverMap);
    }

    public Map<Byte, Server> getServerMap() {
        return serverMap;
    }

    public List<Server> getServersAsList() {
        return servers;
    }

    public double getAvgLoad() {
        return avgLoad;
    }

    public Cluster setAvgLoad(double avgLoad) {
        this.avgLoad = avgLoad;
        return this;
    }

}
