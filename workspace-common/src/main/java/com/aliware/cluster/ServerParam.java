package com.aliware.cluster;

/**
 * 服务器属性
 */
public class ServerParam {

    /**
     * 最大并发连接数Maximum-Concurrent-Connection-Count
     */
    private long mccc;

    public long getMccc() {
        return mccc;
    }

    public ServerParam setMccc(long mccc) {
        this.mccc = mccc;
        return this;
    }
}
