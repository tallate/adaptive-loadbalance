package com.aliware.tianchi.cluster;

import com.aliware.cluster.Cluster;
import com.aliware.cluster.Server;

public interface ClusterState {

    /**
     * 是否匹配目标集群的状态
     */
    boolean match(Cluster cluster);

    /**
     * 从目标集群选择服务器
     */
    Server select(Cluster cluster);

}
