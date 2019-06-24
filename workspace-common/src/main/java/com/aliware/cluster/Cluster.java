package com.aliware.cluster;

import com.aliware.TimeUtil;
import com.aliware.counter.Counter;
import org.apache.dubbo.common.utils.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aliware.config.LoadConfig.*;

public class Cluster implements Serializable {

    /**
     * 服务端属性
     */
    private final Map<Byte, Counter> serverCounterMap = new HashMap<>();

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
        Server small = new Server((byte) 1).setWeight(INIT_WEIGHT_SMALL);
        Server medium = new Server((byte) 2).setWeight(INIT_WEIGHT_MEDIUM);
        Server large = new Server((byte) 3).setWeight(INIT_WEIGHT_LARGE);
        serverMap.put((byte) 1, small);
        serverMap.put((byte) 2, medium);
        serverMap.put((byte) 3, large);
        servers.add(small);
        servers.add(medium);
        servers.add(large);
        serverCounterMap.put((byte) 1, new Counter(MAX_COLLECT_TIME_LENGTH, TimeUtil.getCurrentSecond()));
        serverCounterMap.put((byte) 2, new Counter(MAX_COLLECT_TIME_LENGTH, TimeUtil.getCurrentSecond()));
        serverCounterMap.put((byte) 3, new Counter(MAX_COLLECT_TIME_LENGTH, TimeUtil.getCurrentSecond()));
    }

    public Counter getServerCounterByHostCode(byte hostCode) {
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
