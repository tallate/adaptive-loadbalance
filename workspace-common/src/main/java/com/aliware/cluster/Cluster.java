package com.aliware.cluster;

import org.apache.dubbo.common.utils.CollectionUtils;

import java.io.Serializable;
import java.util.Map;

public class Cluster implements Serializable {

    /**
     * 服务器对象<hostCode, Server>
     */
    private Map<Byte, Server> serverMap;

    /**
     * 平均负载
     */
    private double avgLoad;

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

    public Cluster setServerMap(Map<Byte, Server> serverMap) {
        this.serverMap = serverMap;
        return this;
    }

    public double getAvgLoad() {
        return avgLoad;
    }

    public Cluster setAvgLoad(double avgLoad) {
        this.avgLoad = avgLoad;
        return this;
    }
}
