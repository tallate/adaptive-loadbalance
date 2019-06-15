package com.aliware.tianchi.lb;

import com.aliware.Cluster;
import com.aliware.Server;

public class BusyState implements ClusterState {

    private static final double LOAD_THRESHOLD = 0.8;



    @Override
    public boolean match(Cluster cluster) {
        return cluster.getLoadSum() > LOAD_THRESHOLD;
    }

    @Override
    public Server select(Cluster cluster) {
        return
    }
}
