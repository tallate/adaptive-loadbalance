package com.aliware.tianchi;

import com.aliware.tianchi.server.ServerContext;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.*;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端过滤器
 * 可选接口
 * 用户可以在服务端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.PROVIDER)
public class TestServerFilter implements Filter {

    {
        // LOG: 加一个线程用于记录每秒接收到的请求数
        // DisposableScheduledTaskUtil.scheduleAtFixedRate(() -> {
        //     long lastSecond = TimeUtil.getLastSecond();
        //     LogUtil.info("时间=" + lastSecond + " 请求数=" + ServerContext.getReqCount(lastSecond));
        // }, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            // 计算吞吐量
            ServerContext.countReq();
            Result result = invoker.invoke(invocation);
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        return result;
    }

}
