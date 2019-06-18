package com.aliware.tianchi.server;

import com.aliware.cluster.Server;
import com.aliware.config.HostUtil;

import java.io.IOException;

/**
 */
public class ServerGenerator {

    private static byte getOmittedLoad(double load) {
        // load 都是小于 1 的，因此这里不会出现溢出的情况
        return (byte) (load * 100);
    }

    public static Server gen() throws InterruptedException {
        return new Server()
                .setHostCode(HostUtil.getCurrentHostCode())
                .setThroughput(ServerContext.getCurrentThroughput())
                .setTime(System.currentTimeMillis());
    }

}
