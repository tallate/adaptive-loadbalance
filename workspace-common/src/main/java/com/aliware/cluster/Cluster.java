package com.aliware.cluster;

import com.aliware.CircularQueue;
import com.aliware.config.SamplingConfig;
import org.apache.dubbo.common.utils.CollectionUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cluster implements Serializable {

    /**
     * 记录每个
     */
    private final Map<Byte, CircularQueue<Long>> circularQueueMap = new HashMap<>();

    /**
     * 服务器实时属性<hostCode, Server>
     */
    private final Map<Byte, Server> serverMap = new HashMap<>();

    /**
     * 平均负载
     */
    private double avgLoad;

    public Cluster() {
        // TODO: 初始化，这里硬编码了
        circularQueueMap.put((byte) 1, new CircularQueue<>(SamplingConfig.MAX_THROUGHPUT));
        circularQueueMap.put((byte) 2, new CircularQueue<>(SamplingConfig.MAX_THROUGHPUT));
        circularQueueMap.put((byte) 3, new CircularQueue<>(SamplingConfig.MAX_THROUGHPUT));
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

    public Map<Byte, CircularQueue<Long>> getCircularQueueMap() {
        return circularQueueMap;
    }

    public CircularQueue<Long> getCircularQueue(byte hostCode) {
        return circularQueueMap.get(hostCode);
    }

    public double getAvgLoad() {
        return avgLoad;
    }

    public Cluster setAvgLoad(double avgLoad) {
        this.avgLoad = avgLoad;
        return this;
    }
}
