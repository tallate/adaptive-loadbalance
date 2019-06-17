package com.aliware.tianchi.jmxmonitor;

import com.sun.management.OperatingSystemMXBean;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.StringTokenizer;

/**
 * 内存信息
 * 文件”/proc/meminfo”里面包含的就是内存的信息，还包括了swap的信息。例如：
 * $ cat /proc/meminfo
 * total: used: free: shared: buffers: cached:
 * Mem: 1057009664 851668992 205340672 0 67616768 367820800
 * Swap: 2146787328 164429824 1982357504
 * MemTotal: 1032236 kB
 * MemFree: 200528 kB
 * MemShared: 0 kB
 * ……
 * 不过从gkrellm的源代码看，有些版本没有前面那两行统计的信息，只能够根据下面的Key: Value这种各式的数据收集。
 */
public class MemoryUtil {

  /**
   * 使用率
   */
  public static double getMemRatio() throws IOException, InterruptedException {
    if (OSType.isLinux()) {
      int[] memInfo = getMemInfo4Linux();
      return (double) (memInfo[0] - memInfo[1]) / memInfo[0];
    }
    throw new RuntimeException("不识别的操作系统类型");
  }

  public static int getMemTotal() throws IOException, InterruptedException {
    return getMemInfo4Linux()[0];
  }

  public static int getMemFree() throws IOException, InterruptedException {
    return getMemInfo4Linux()[1];
  }

  public static int getSwapTotal() throws IOException, InterruptedException {
    return getMemInfo4Linux()[2];
  }

  public static int getSwapFree() throws IOException, InterruptedException {
    return getMemInfo4Linux()[3];
  }

  /**
   * 通过Linux指令获取内存信息
   * MemTotal：memInfo[0]
   * MemFree：memInfo[1]
   * SwapTotal：memInfo[2]
   * SwapFree：memInfo[3]
   */
  public static int[] getMemInfo4Linux() throws IOException, InterruptedException {
    File file = new File("/proc/meminfo");
    BufferedReader br = new BufferedReader(new InputStreamReader(
        new FileInputStream(file)));
    int[] result = new int[4];
    String str = null;
    StringTokenizer token = null;
    while ((str = br.readLine()) != null) {
      token = new StringTokenizer(str);
      if (!token.hasMoreTokens()) {
        continue;
      }

      str = token.nextToken();
      if (!token.hasMoreTokens()) {
        continue;
      }

      if (str.equalsIgnoreCase("MemTotal:")) {
        result[0] = Integer.parseInt(token.nextToken());
      } else if (str.equalsIgnoreCase("MemFree:")) {
        result[1] = Integer.parseInt(token.nextToken());
      } else if (str.equalsIgnoreCase("SwapTotal:")) {
        result[2] = Integer.parseInt(token.nextToken());
      } else if (str.equalsIgnoreCase("SwapFree:")) {
        result[3] = Integer.parseInt(token.nextToken());
      }
    }
    return result;
  }

  /**
   * 通过JMX获取内存使用率
   */
  public static String getMemery() {
    OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    // 总的物理内存+虚拟内存
    long totalvirtualMemory = osmxb.getTotalSwapSpaceSize();
    // 剩余的物理内存
    long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
    Double compare = (Double) (1 - freePhysicalMemorySize * 1.0 / totalvirtualMemory) * 100;
    String str = "内存已使用:" + compare.intValue() + "%";
    return str;
  }

}

