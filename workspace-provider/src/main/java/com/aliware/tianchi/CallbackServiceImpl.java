package com.aliware.tianchi;

import com.aliware.MessageUtil;
import com.aliware.cluster.Server;
import com.aliware.tianchi.server.ServerGenerator;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端回调服务
 * 可选接口
 * 用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CallbackServiceImpl() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!listeners.isEmpty()) {
                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                        try {
                            String content = genContent();
                            entry.getValue().receiveServerMsg(content);
                        } catch (Throwable t1) {
                            listeners.remove(entry.getKey());
                        }
                    }
                }
            }
        }, 0, 5000);
    }

    private Timer timer = new Timer();

    /**
     * key: listener type
     * value: callback listener
     */
    private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();

    @Override
    public void addListener(String key, CallbackListener listener) {
        listeners.put(key, listener);
        String content = genContent();
        // 发送给 consumer
        listener.receiveServerMsg(content); // send notification for change
    }

    private String genContent() {
        // 生成 provider 端属性
        Server server = null;
        try {
            server = ServerGenerator.gen();
        } catch (IOException | InterruptedException e) {
            logger.error("生成失败了，有没有搞错", e);
        }
        // 编码
        return MessageUtil.encode(server);
    }
}
