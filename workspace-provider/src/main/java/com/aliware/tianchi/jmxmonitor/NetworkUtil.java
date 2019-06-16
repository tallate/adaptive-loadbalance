package com.aliware.tianchi.jmxmonitor;

import com.aliware.Pair;
import com.aliware.Uninterruptibles;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * 网络信息
 * 网络流量也是五花八门，不过基本上都可以在/proc/net/dev里面获得。同样也是需要两次取值取其差作为流量值。
 */
public class NetworkUtil {

  private static final Logger logger = LoggerFactory.getLogger(NetworkUtil.class);

  private static final String FETCH_NETWORK_COMMAND = "cat /proc/net/dev";

  /**
   * 实际连接网络的网卡名的前缀
   */
  private static final String NETWORK_INTERFACE_NAME_PREFIX = "wlp";

  public static double getNetworkRatio() {
    if (OSType.isLinux()) {
      return getNetworkRatio4Linux();
    }
    throw new RuntimeException("不识别的操作系统类型");
  }

  private static Pair<Long, Long> getFlowRate() throws IOException {
    long startTime = System.currentTimeMillis();
    Process p = Runtime.getRuntime().exec(FETCH_NETWORK_COMMAND);
    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String line;
    long inSize = 0L, outSize = 0L;
    while ((line = in.readLine()) != null) {
      line = line.trim();
      if (line.startsWith(NETWORK_INTERFACE_NAME_PREFIX)) {
        String[] temp = line.split("\\s+");
        // Receive bytes,单位为Byte
        inSize = Long.parseLong(temp[1].substring(5));
        // Transmit bytes,单位为Byte
        outSize = Long.parseLong(temp[8]);
        break;
      }
    }
    in.close();
    p.destroy();
    return Pair.of(inSize, outSize);
  }

  /**
   * 收集网络带宽使用率
   */
  public static double getNetworkRatio4Linux() {
    double curRate = 0.0f;
    try {

      //第一次采集流量数据
      long startTime = System.currentTimeMillis();
      Pair<Long, Long> pair1 = getFlowRate();

      // 中间睡眠一段时间
      Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);

      // 第二次采集流量数据
      long endTime = System.currentTimeMillis();
      Pair<Long, Long> pair2 = getFlowRate();

      if (pair1.getK() != 0 && pair1.getV() != 0
          && pair2.getK() != 0 && pair2.getV() != 0) {
        double interval = (double) (endTime - startTime) / 1000;
        // 网口传输速度,单位为bps
        curRate = (double) (pair2.getK() - pair1.getK() + pair2.getV() - pair1.getV()) * 8 / (1000000 * interval);
      }
    } catch (IOException e) {
      logger.error(e);
    }
    return curRate;
  }

  public static String getMACAddress() {
    if (OSType.isWindows()) {
      return getWindowsMACAddress();
    } else if (OSType.isLinux()) {
      return getUnixMACAddress();
    }
    throw new RuntimeException("不识别的操作系统: " + OSType.getOSType());
  }

  /**
   * 获取unix网卡的mac地址.
   * 非windows的系统默认调用本方法获取.如果有特殊系统请继续扩充新的取mac地址方法.
   *
   * @return mac地址
   */
  public static String getUnixMACAddress() {
    String mac = null;
    BufferedReader bufferedReader = null;
    Process process = null;
    try {
      // linux下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
      // TODO：怎么找到当前用的网卡？
      process = Runtime.getRuntime().exec("ifconfig eth0");
      bufferedReader = new BufferedReader(new InputStreamReader(process
          .getInputStream()));
      String line = null;
      int index = -1;
      while ((line = bufferedReader.readLine()) != null) {
        // 寻找标示字符串[hwaddr]
        index = line.toLowerCase().indexOf("hwaddr");
        // 找到了
        if (index >= 0) {
          //  取出mac地址并去除2边空格
          mac = line.substring(index + "hwaddr".length() + 1).trim();
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      bufferedReader = null;
      process = null;
    }

    return mac;
  }

  /**
   * 获取widnows网卡的mac地址.
   *
   * @return mac地址
   */
  public static String getWindowsMACAddress() {
    String mac = null;
    BufferedReader bufferedReader = null;
    Process process = null;
    try {
      // windows下的命令，显示信息中包含有mac地址信息
      process = Runtime.getRuntime().exec("ipconfig /all");
      bufferedReader = new BufferedReader(new InputStreamReader(process
          .getInputStream()));
      String line = null;
      int index = -1;
      while ((line = bufferedReader.readLine()) != null) {
        // 寻找标示字符串[physical address]
        index = line.toLowerCase().indexOf("physical address");
        // 获取成功
        if (index >= 0) {
          // 寻找":"的位置
          index = line.indexOf(":");
          if (index >= 0) {
            // 取出mac地址并去除2边空格
            mac = line.substring(index + 1).trim();
          }
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      bufferedReader = null;
      process = null;
    }

    return mac;
  }

  /**
   * @return 本机主机名
   */
  public static String getHostName() {
    InetAddress ia = null;
    try {
      ia = InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    if (ia == null) {
      return "some error..";
    } else {
      return ia.getHostName();
    }
  }

  /**
   * @return 本机IP 地址
   */
  public static String getIPAddress() {
    InetAddress ia = null;
    try {
      ia = InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    if (ia == null) {
      return "some error..";
    } else {
      return ia.getHostAddress();
    }
  }

}
