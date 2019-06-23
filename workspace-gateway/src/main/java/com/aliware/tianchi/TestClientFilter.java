package com.aliware.tianchi;

import com.aliware.DisposableScheduledTaskUtil;
import com.aliware.Pair;
import com.aliware.config.HostUtil;
import com.aliware.tianchi.cluster.ClusterContext;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.*;

import java.util.concurrent.TimeUnit;

/**
 * @author daofeng.xjf
 * <p>
 * 客户端过滤器
 * 可选接口
 * 用户可以在客户端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.CONSUMER)
public class TestClientFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(TestClientFilter.class);

    static {
        // 加一个线程用于记录发给每个服务器的请求量
        // DisposableScheduledTaskUtil.scheduleAtFixedRate(() -> {
        //     byte[] hostCodes = new byte[3];
        //     hostCodes[0] = 1;
        //     hostCodes[1] = 2;
        //     hostCodes[2] = 3;
        //     for (byte hostCode : hostCodes) {
        //         Pair<Long, Long> pair = ClusterContext.getExactThrouhput(hostCode);
        //         long collectTime = pair.getK();
        //         long exact = pair.getV();
        //         long expected = ClusterContext.getExpectThrouhput(hostCode, collectTime);
        //         logger.info("hostCode=" + hostCode + " collectTime=" + collectTime + " expected=" + expected + " exact=" + exact);
        //     }
        // }, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            byte hostCode = HostUtil.getHostCodeByName(invoker.getUrl().getHost());
            ClusterContext.countReq(hostCode);
            Result result = invoker.invoke(invocation);
            return result;
        } catch (Exception e) {
            // TODO: 处理捕获的异常
            throw e;
        }

    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        return result;
    }
}
