package com.aliware.tianchi.cluster;

import com.aliware.cluster.Cluster;
import com.aliware.cluster.Server;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 */
public class ClusterContext {

    private static Cluster cluster = new Cluster();

    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

    public static <T> T compute(Function<Cluster, T> function) throws ExecutionException {
        LOCK.readLock().lock();
        T result = function.apply(cluster);
        LOCK.readLock().unlock();
        return result;
    }

    public static Cluster getCluster() throws ExecutionException {
        return cluster;
    }

    public static void putServer(Server server) throws ExecutionException {
        LOCK.writeLock().lock();
        // 将server添加到上下文
        Server origin = cluster.getServer(server.getHostCode());
        // 取最后一次发出时的属性
        if (server.getTime() > origin.getTime()) {
            cluster.putServer(server.getHostCode(), server);
        }
        // 计算平均负载
        double loadSum = cluster.getServerMap().entrySet().stream()
                .map(entry -> (double) entry.getValue().getCpuLoad())
                .reduce((load1, load2) -> load1 / 100 + load2 / 100)
                .orElse(0.0);
        cluster.setAvgLoad(loadSum / cluster.getServerMap().size());
        LOCK.writeLock().unlock();
    }

}
