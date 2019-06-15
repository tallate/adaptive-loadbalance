package com.aliware.tianchi.jmxmonitor;

import io.netty.util.NetUtil;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class SystemUtil {

    /**
     * CPU功率
     */
    public static double getCpuRatio() {
        return CpuUtil.getCpuRatio();
    }

    public static double getMemoryRatio() {
        return MemoryUtil.getMemRatio();
    }

    public static double getNetworkRatio() {
        return NetworkUtil,getMemoruRai
    }

    public static double getDiskRatio() {
        return DiskUtil.getDiskRatio4Linux();
    }

    /**
     * 系统平均负载的定义：在特定时间间隔内运行队列中的平均进程数。如果一个进程满足以下条件，则其就会位于运行队列中：
     * 1. 没有在等待I/O 操作的结果
     * 2. 没有主动进入等待状态（也就是没有调用'wait'）
     * 3. 没有被停止（例如：等待终止）
     *
     * @return 系统平均负载的百分比
     */
    public static double getSystemAverageLoad() {
        OperatingSystemMXBean osmxb = ManagementFactory.getOperatingSystemMXBean();
        return osmxb.getSystemLoadAverage();
    }

}
