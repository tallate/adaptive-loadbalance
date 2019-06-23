package com.aliware.tianchi;

import com.aliware.DisposableScheduledTaskUtil;
import com.aliware.IntervalSelector;
import com.aliware.cluster.Cluster;
import com.aliware.config.HostUtil;
import com.aliware.config.LoadConfig;
import com.aliware.tianchi.cluster.ClusterContext;
import com.aliware.tianchi.cluster.SelectFuntion;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * @author daofeng.xjf
 * <p>
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {

    private static final Logger logger = LoggerFactory.getLogger(UserLoadBalance.class);

    private static final AtomicBoolean WARMUP = new AtomicBoolean(true);

    static {
        DisposableScheduledTaskUtil.submitDelayTask(
                () -> WARMUP.set(false),
                LoadConfig.WARMUP_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 这个函数在上下文中的所有服务器中选出目标服务器（通过负载均衡算法）
     */
    private static final Function<Cluster, Byte> SELECT_FUNCTION = new SelectFuntion();

    private Byte selectTargetHost() {
        try {
            return ClusterContext.compute(SELECT_FUNCTION);
        } catch (ExecutionException e) {
            throw new RpcException("选择目标服务器失败", e);
        }
    }

    private static final IntervalSelector intervalSelector = new IntervalSelector(10, true);

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        if (WARMUP.get()) {
            // 利用官方提供的默认随机算法预热
            int pos = ThreadLocalRandom.current().nextInt(invokers.size());
            return invokers.get(pos);
        }
        Byte targetHostCode = selectTargetHost();
        // LOG: 记录负载均衡算法选中的是哪台
        // if (intervalSelector.get()) {
        //     logger.info("选中目标服务器=" + targetHostCode);
        // }
        Optional<Invoker<T>> target = invokers.stream()
                .filter(invoker -> {
                    // 调整主机名格式一致
                    String hostName = invoker.getUrl().getHost();
                    // 转换成编码并与负载均衡结果比对
                    return targetHostCode == HostUtil.getHostCodeByName(hostName);
                })
                .findFirst();
        if (!target.isPresent()) {
            // TODO: 容错
            throw new RpcException("目标服务器不存在");
        }
        return target.get();
    }
}
