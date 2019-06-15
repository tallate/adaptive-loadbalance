package com.aliware;

import java.util.List;

public class Cluster {

    private List<Server> servers;

    private double loadSum;

    private double loadAverage;

    public List<Server> getServers() {
        return servers;
    }

    public Cluster setServers(List<Server> servers) {
        this.servers = servers;
        return this;
    }

    public double getLoadSum() {
        return loadSum;
    }

    public Cluster setLoadSum(double loadSum) {
        this.loadSum = loadSum;
        return this;
    }

    public double getLoadAverage() {
        return loadAverage;
    }

    public Cluster setLoadAverage(double loadAverage) {
        this.loadAverage = loadAverage;
        return this;
    }
}
