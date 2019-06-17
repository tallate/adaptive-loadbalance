package com.aliware.tianchi.server;

import com.aliware.cluster.Server;
import com.aliware.config.HostUtil;
import com.aliware.tianchi.jmxmonitor.SystemUtil;

import java.io.IOException;

/**
 */
public class ServerGenerator {

    private static byte getOmittedLoad(double load) {
        // load 都是小于 1 的，因此这里不会出现溢出的情况
        return (byte) (load * 100);
    }

    public static Server gen() throws IOException, InterruptedException {
        return new Server()
                .setHostCode(HostUtil.getCurrentHostCode())
                .setCpuLoad(getOmittedLoad(SystemUtil.getCpuRatio()))
                .setMemoryLoad(getOmittedLoad(SystemUtil.getMemoryRatio()))
                .setNetworkLoad(getOmittedLoad(SystemUtil.getNetworkRatio()))
                .setTime(System.currentTimeMillis());
    }

}
