package com.aliware.cluster;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.dubbo.common.utils.CollectionUtils;

public class Cluster implements Serializable {

    /**
     * 服务器属性<hostCode, ServerParam>
     */
    private Map<Byte, ServerParam> serverParamMap = new HashMap<>();

    /**
     * 服务器实时属性<hostCode, Server>
     */
    private Map<Byte, Server> serverMap = new HashMap<>();

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

    public Map<Byte, ServerParam> getServerParamMap() {
        return serverParamMap;
    }

    public Cluster setServerParamMap(Map<Byte, ServerParam> serverParamMap) {
        this.serverParamMap = serverParamMap;
        return this;
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
