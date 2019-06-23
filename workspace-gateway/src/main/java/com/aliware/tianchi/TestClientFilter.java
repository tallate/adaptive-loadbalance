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
        DisposableScheduledTaskUtil.scheduleAtFixedRate(() -> {
            byte[] hostCodes = new byte[3];
            hostCodes[0] = 1;
            hostCodes[1] = 2;
            hostCodes[2] = 3;
            long[] collectTimes = new long[3];
            long[] exacts = new long[3];
            long[] expects = new long[3];
            for (int i = 0; i < 3; i++) {
                byte hostCode = hostCodes[i];
                Pair<Long, Long> pair = ClusterContext.getExactThrouhput(hostCode);
                collectTimes[i] = pair.getK();
                exacts[i] = pair.getV();
                expects[i] = ClusterContext.getExpectThrouhput(hostCode, collectTimes[i]);
            }
            logger.info("统计请求量 collectTime=[" + collectTimes[0] + ", " + collectTimes[1] + ", " + collectTimes[2] + "]"
                    + " expect=[" + expects[0] + ", " + expects[1] + ", " + expects[2] + "]"
                    + " exact=[" + exacts[0] + ", " + exacts[1] + ", " + exacts[2] + "]");
        }, 1, 1, TimeUnit.SECONDS);
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
