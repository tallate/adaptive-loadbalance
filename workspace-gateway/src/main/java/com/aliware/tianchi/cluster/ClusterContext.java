package com.aliware.tianchi.cluster;

import com.aliware.Pair;
import com.aliware.TimeUtil;
import com.aliware.cluster.Cluster;
import com.aliware.cluster.Server;
import com.aliware.counter.Counter;
import com.aliware.log.LogUtil;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static com.aliware.config.LoadConfig.LOAD_THRESHOLD_UP_BOUND;

/**
 */
public class ClusterContext {

    private static Cluster cluster = new Cluster();

    /**
     * 通过上下文计算...
     * TODO: 改成无锁的
     */
    public static <T> T compute(Function<Cluster, T> function) throws ExecutionException {
        // LOCK.readLock().lock();
        T result = function.apply(cluster);
        // LOCK.readLock().unlock();
        return result;
    }

    /**
     * 将一个服务器信息添加到上下文
     * TODO: 改成无锁的
     */
    public static void putServer(Server server) throws ExecutionException {
        // LOCK.writeLock().lock();
        // 将server添加到上下文
        Server origin = cluster.getServer(server.getHostCode());
        // 取最后一次发出时的属性
        if (server.getTime() > origin.getTime()) {
            origin.setCollectTime(server.getCollectTime())
                    .setThroughput(server.getThroughput())
                    .setTime(server.getTime());
            // 没必要整个覆盖
            // cluster.putServer(server.getHostCode(), server);
        }
        // 计算平均负载，因为provider反馈的会有延迟、肯定比consumer端统计的要晚，所以以provider的采集时间为准
        double sumLoad = 0;
        double[] weights = new double[3];
        double[] loads = new double[3];
        long[] expects = new long[3];
        long[] exacts = new long[3];
        for (Map.Entry<Byte, Server> entry : cluster.getServerMap().entrySet()) {
            byte hostCode = entry.getKey();
            Server s = entry.getValue();
            long collectTime = s.getCollectTime();
            long exactThroughput = s.getThroughput();
            long expectThroughput = getExpectThrouhput(entry.getKey(), collectTime);
            // 计算单机负载
            double load = expectThroughput == 0 || exactThroughput == 0 ?
                    1 :
                    ((double) expectThroughput) / exactThroughput;
            s.setLoad(load);
            sumLoad += load;
            // 更新权重（影响负载均衡）
            double loadOffset = Math.abs(load - 1);
            if (loadOffset < LOAD_THRESHOLD_UP_BOUND) {
                s.setWeight(s.getWeight() / load);
            }
            weights[(int) hostCode - 1] = s.getWeight();
            loads[(int) hostCode - 1] = load;
            expects[(int) hostCode - 1] = expectThroughput;
            exacts[(int) hostCode - 1] = exactThroughput;
        }
        double avgLoad = sumLoad / cluster.getServerMap().size();
        // LOG: 统计平均负载计算过程
        LogUtil.info("平均负载avgLoad=" + avgLoad
                + " loads=[" + loads[0] + ", "
                + loads[1] + ", " + loads[2] + "]"
                + " expects=[" + expects[0] + ", "
                + expects[1] + ", " + expects[2] + "]"
                + " exacts=[" + exacts[0] + ", "
                + exacts[1] + ", " + exacts[2] + "]");
        cluster.setAvgLoad(avgLoad);
        // LOG: 记录权重
        LogUtil.error("权重weights=[" + weights[0] + ", " + weights[1] + ", " + weights[2] + "]");
        // LOCK.writeLock().unlock();
    }

    /**
     * 获取期望吞吐量(gateway发出的请求数)
     *
     * @param collectTime 收集时间 / s
     */
    public static long getExpectThrouhput(byte hostCode, long collectTime) {
        Counter counter = cluster.getServerCounterByHostCode(hostCode);
        return counter.get(collectTime);
    }

    /**
     * 记录请求
     */
    public static void countReq(byte hostCode) {
        long currentSecond = TimeUtil.getCurrentSecond();
        Counter counter = cluster.getServerCounterByHostCode(hostCode);
        counter.incr(currentSecond);
    }

    /**
     * 返回最后实际的吞吐量
     *
     * @return <收集时间 / s, 吞吐量>
     */
    public static Pair<Long, Long> getExactThrouhput(byte hostCode) {
        Server server = cluster.getServer(hostCode);
        if (null == server) {
            return Pair.of(0L, 0L);
        }
        return Pair.of(server.getCollectTime(), server.getThroughput());
    }
}
