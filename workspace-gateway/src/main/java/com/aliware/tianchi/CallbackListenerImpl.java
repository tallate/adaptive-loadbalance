package com.aliware.tianchi;

import com.aliware.MessageUtil;
import com.aliware.cluster.Server;
import com.aliware.tianchi.cluster.ClusterContext;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.listener.CallbackListener;

import java.util.concurrent.ExecutionException;

/**
 * @author daofeng.xjf
 * <p>
 * 客户端监听器
 * 可选接口
 * 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 */
public class CallbackListenerImpl implements CallbackListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Server decode(String msg) {
        try {
            return MessageUtil.decode(msg);
        } catch (Exception e) {
            throw new RuntimeException("格式错误，傻逼了吧", e);
        }
    }

    @Override
    public void receiveServerMsg(String msg) {
        logger.error(msg);
        // 解析
        Server server = decode(msg);
        // 添加到上下文
        try {
            ClusterContext.putServer(server);
        } catch (ExecutionException e) {
            logger.error("更新失败，放弃这次更新", e);
        }
    }

}
