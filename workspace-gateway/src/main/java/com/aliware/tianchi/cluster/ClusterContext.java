package com.aliware.tianchi.cluster;

import com.aliware.CircularQueue;
import com.aliware.cluster.Cluster;
import com.aliware.cluster.Server;
import com.aliware.config.SamplingConfig;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;

import java.util.Comparator;
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

    /**
     * 通过上下文计算...
     * TODO: 改成无锁的
     */
    public static <T> T compute(Function<Cluster, T> function) throws ExecutionException {
        LOCK.readLock().lock();
        T result = function.apply(cluster);
        LOCK.readLock().unlock();
        return result;
    }

    /**
     * 将一个服务器信息添加到上下文
     * TODO: 改成无锁的
     */
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
            long throughput = entry.getValue().getThroughput();
            long expectedThroughput = getExpectedThrouhput(entry.getKey());
            double load = ((double) expectedThroughput) / throughput;
            // 更新单机负载
            entry.getValue().setLoad(load);
            sumLoad += load;
        }
        double avgLoad = sumLoad / cluster.getServerMap().size();
        logger.info("平均负载avgLoad：" + avgLoad);
        cluster.setAvgLoad(avgLoad);
        LOCK.writeLock().unlock();
    }

    /**
     * 获取期望吞吐量(gateway发出的请求数)
     */
    public static long getExpectedThrouhput(byte hostCode) {
        CircularQueue<Long> circularQueue = cluster.getCircularQueue(hostCode);
        int frontPos = circularQueue.getFrontPos();
        int targetPos = circularQueue.binsearch(System.currentTimeMillis() - SamplingConfig.SAMPLING_TIME_INTERVAL,
                Comparator.naturalOrder());
        return circularQueue.getInterval(targetPos, frontPos);
    }

    /**
     * 将请求压入队列
     */
    public static void pushReq(byte hostCode) {
        cluster.getCircularQueue(hostCode)
                .offer(System.currentTimeMillis());
    }
}
