package com.aliware.tianchi.cluster;

import com.aliware.cluster.Cluster;
import com.aliware.cluster.Server;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 */
public class ClusterContext {

    private static final Logger logger = LoggerFactory.getLogger(ClusterContext.class);

    private static Cluster cluster = new Cluster();

    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

    public static <T> T compute(Function<Cluster, T> function) throws ExecutionException {
        LOCK.readLock().lock();
        T result = function.apply(cluster);
        LOCK.readLock().unlock();
        return result;
    }

    public static void putServer(Server server) throws ExecutionException {
        LOCK.writeLock().lock();
        // 将server添加到上下文
        Server origin = cluster.getServer(server.getHostCode());
        // 取最后一次发出时的属性
        if (origin == null || server.getTime() > origin.getTime()) {
            cluster.putServer(server.getHostCode(), server);
        }
        // 计算平均负载
        double sumLoad = 0;
        for (Map.Entry<Byte, Server> entry : cluster.getServerMap().entrySet()) {
            sumLoad += entry.getValue().getCpuLoad() / 100.0;
        }
        double avgLoad = sumLoad / cluster.getServerMap().size();
        logger.info("平均负载avgLoad：" + avgLoad);
        cluster.setAvgLoad(avgLoad);
        LOCK.writeLock().unlock();
    }

}
