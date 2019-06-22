package com.aliware.cluster;

import java.io.Serializable;

/**
 * 服务器实时属性
 */
public class Server implements Serializable {

    /**
     * 主机编码
     */
    private byte hostCode;

    /**
     * 吞吐量收集时间 / s
     */
    private long collectTime;

    /**
     * 吞吐量
     */
    private long throughput;

    /**
     * 快照生成时间（provider给出）
     */
    private long time;

    /**
     * 负载（consumer算出）
     */
    private double load;

    public long getCollectTime() {
        return collectTime;
    }

    public Server setCollectTime(long collectTime) {
        this.collectTime = collectTime;
        return this;
    }

    public long getThroughput() {
        return throughput;
    }

    public Server setThroughput(long throughput) {
        this.throughput = throughput;
        return this;
    }

    public double getLoad() {
        return load;
    }

    public Server setLoad(double load) {
        this.load = load;
        return this;
    }

    public byte getHostCode() {
        return hostCode;
    }

    public Server setHostCode(byte hostCode) {
        this.hostCode = hostCode;
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
                ", load=" + load +
                ", time=" + time +
                '}';
    }

}
