package com.aliware.cluster;

import java.io.Serializable;

public class Server implements Serializable {

    /**
     * 主机编码
     */
    private byte hostCode;

    /**
     * cpu负载 = cpuLoad / 100.0
     */
    private byte cpuLoad;

    /**
     * 内存负载
     */
    private byte memoryLoad;

    /**
     * 网络负载
     */
    private byte networkLoad;

    /**
     * 快照生成时间
     */
    private long time;

    public byte getHostCode() {
        return hostCode;
    }

    public Server setHostCode(byte hostCode) {
        this.hostCode = hostCode;
        return this;
    }

    public byte getCpuLoad() {
        return cpuLoad;
    }

    public Server setCpuLoad(byte cpuLoad) {
        this.cpuLoad = cpuLoad;
        return this;
    }

    public byte getMemoryLoad() {
        return memoryLoad;
    }

    public Server setMemoryLoad(byte memoryLoad) {
        this.memoryLoad = memoryLoad;
        return this;
    }

    public byte getNetworkLoad() {
        return networkLoad;
    }

    public Server setNetworkLoad(byte networkLoad) {
        this.networkLoad = networkLoad;
        return this;
    }

    public long getTime() {
        return time;
    }

    public Server setTime(long time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return "Server{" +
                "hostCode=" + hostCode +
                ", cpuLoad=" + cpuLoad +
                ", memoryLoad=" + memoryLoad +
                ", networkLoad=" + networkLoad +
                ", time=" + time +
                '}';
    }
}
