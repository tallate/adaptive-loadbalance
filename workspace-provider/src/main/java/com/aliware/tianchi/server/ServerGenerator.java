package com.aliware.tianchi.server;

import com.aliware.TimeUtil;
import com.aliware.cluster.Server;
import com.aliware.config.HostUtil;

/**
 */
public class ServerGenerator {

    public static Server gen() throws InterruptedException {
        long lastSecond = TimeUtil.getLastSecond();
        return new Server()
                .setHostCode(HostUtil.getCurrentHostCode())
                .setCollectTime(lastSecond)
                .setThroughput(ServerContext.getReqCount(lastSecond))
                .setTime(TimeUtil.getCurrentMillis());
    }

}
