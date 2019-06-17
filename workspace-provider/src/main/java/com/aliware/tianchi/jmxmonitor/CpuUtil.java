package com.aliware.tianchi.jmxmonitor;

import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.StringTokenizer;

/**
 * Cpu信息
 * 在文件”/proc/stat”里面就包含了CPU的信息。每一个CPU的每一tick用在什么地方都在这个文件里面记着。后面的数字含义分别是： user、nice、sys、idle、iowait。有些版本的kernel没有iowait这一项。这些数值表示从开机到现在，CPU的每tick用在了哪里。例如：
 * cpu0 256279030 0 11832528 1637168262
 * 就是cpu0从开机到现在有 256279030 tick用在了user消耗，11832528用在了sys消耗。所以如果想计算单位时间（例如1s）里面CPU的负载，那只需要计算1秒前后数值的差除以每一秒的tick数量就可以了。gkrellm就是这样实现的：((200 * (v2 - v1) / CPU_TICKS_PER_SECOND) + 1) /2
 * 例如，第一次读取/proc/stat，user的值是256279030；一秒以后再读一次，值是256289030，那么CPU在这一秒的 user消耗就是：((200 * (256289030 - 256279030) / CPU_TICKS_PER_SECOND) + 1) /2 = ((10000 * 200 / 1000000) + 1) / 2 = 1%了。
 * ---------------------
 * 原文：https://blog.csdn.net/waferleo/article/details/3877806
 */
public class CpuUtil {

    private static Logger logger = LoggerFactory.getLogger(CpuUtil.class);

    private static final int CPUTIME = 500;
    private static final int PERCENT = 100;
    private static final int FAULTLENGTH = 10;

    public static double getCpuRatio() throws IOException, InterruptedException {
        if (OSType.isLinux()) {
            return getCpuRatio4Linux();
        }
        throw new RuntimeException("不识别的操作系统类型");
    }

    /**
     * get memory by used info
     */
    public static double getCpuRatio4Linux() throws IOException, InterruptedException {
        File file = new File("/proc/stat");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file)));
        StringTokenizer token = new StringTokenizer(br.readLine());
        token.nextToken();
        int user1 = Integer.parseInt(token.nextToken());
        int nice1 = Integer.parseInt(token.nextToken());
        int sys1 = Integer.parseInt(token.nextToken());
        int idle1 = Integer.parseInt(token.nextToken());

        // TODO: 这个采样有点生猛
        Thread.sleep(1000);

        br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)));
        token = new StringTokenizer(br.readLine());
        token.nextToken();
        int user2 = Integer.parseInt(token.nextToken());
        int nice2 = Integer.parseInt(token.nextToken());
        int sys2 = Integer.parseInt(token.nextToken());
        int idle2 = Integer.parseInt(token.nextToken());

        return (double) ((user2 + sys2 + nice2) - (user1 + sys1 + nice1))
                / (double) ((user2 + nice2 + sys2 + idle2) - (user1 + nice1
                + sys1 + idle1));
    }

    //获得cpu使用率
    public static double getCpuRatioForWindows() {
        try {
            String procCmd = System.getenv("windir") + "//system32//wbem//wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // 取进程信息
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                return Double.valueOf(PERCENT * (busytime) * 1.0 / (busytime + idletime)).intValue();
            } else {
                return 0;
            }
        } catch (Exception e) {
            logger.error("Windows CPU采样失败", e);
            return 0;
        }
    }

    //读取cpu相关信息
    private static long[] readCpu(final Process proc) {
        long[] retn = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH) {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocidx) {
                    continue;
                }
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
                // ThreadCount,UserModeTime,WriteOperation
                String caption = substring(line, capidx, cmdidx - 1).trim();
                String cmd = substring(line, cmdidx, kmtidx - 1).trim();
                if (cmd.indexOf("wmic.exe") >= 0) {
                    continue;
                }
                String s1 = substring(line, kmtidx, rocidx - 1).trim();
                String s2 = substring(line, umtidx, wocidx - 1).trim();
                if (caption.equals("System Idle Process") || caption.equals("System")) {
                    if (s1.length() > 0) {
                        idletime += Long.valueOf(s1).longValue();
                    }
                    if (s2.length() > 0) {
                        idletime += Long.valueOf(s2).longValue();
                    }
                    continue;
                }
                if (s1.length() > 0) {
                    kneltime += Long.valueOf(s1).longValue();
                }
                if (s2.length() > 0) {
                    usertime += Long.valueOf(s2).longValue();
                }
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在 包含汉字的字符串时存在隐患，现调整如下：
     *
     * @param src       要截取的字符串
     * @param start_idx 开始坐标（包括该坐标)
     * @param end_idx   截止坐标（包括该坐标）
     */
    private static String substring(String src, int start_idx, int end_idx) {
        byte[] b = src.getBytes();
        String tgt = "";
        for (int i = start_idx; i <= end_idx; i++) {
            tgt += (char) b[i];
        }
        return tgt;
    }

}
