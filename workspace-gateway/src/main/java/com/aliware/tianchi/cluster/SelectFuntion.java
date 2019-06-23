package com.aliware.tianchi.cluster;

import com.aliware.cluster.Cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 */
public class SelectFuntion implements Function<Cluster, Byte> {

    private static final List<ClusterState> STATES = new ArrayList<>();

    public SelectFuntion() {
        STATES.add(new BusyState());
        STATES.add(new RelaxState());
    }

    @Override
    public Byte apply(Cluster cluster) {
        Optional<ClusterState> matchedState = STATES.stream()
                .filter(state -> state.match(cluster))
                .findFirst();
        // LOG: 记录算法切换过程
        // logger.info(Clu"BusyState selected");
        if (!matchedState.isPresent()) {
            throw new RuntimeException("没有匹配的集群状态，救不了");
        }
        byte hostCode = matchedState.get()
                .select(cluster)
                .getHostCode();
        return hostCode;
    }
}
