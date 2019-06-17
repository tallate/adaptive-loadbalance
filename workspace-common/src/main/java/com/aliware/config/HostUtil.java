package com.aliware.config;

import com.aliware.Preconditions;
import org.apache.dubbo.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 测试版本主机名是写死的，所以直接转成数字好了，用起来比较方便
 */
public class HostUtil {

    private static final Map<String, Byte> HOST_CODE_MAP = new HashMap<>();

    private static String currentHostName;

    private static Byte currentHostCode;

    private static final AtomicBoolean INIT = new AtomicBoolean();

    private static void initHost() {
        if (!INIT.compareAndSet(false, true)) {
            return;
        }
        HOST_CODE_MAP.put("small", (byte) 1);
        HOST_CODE_MAP.put("medium", (byte) 2);
        HOST_CODE_MAP.put("large", (byte) 3);

        currentHostName = System.getProperty("quota");
        currentHostCode = HOST_CODE_MAP.get(currentHostName);
    }

    /**
     * 获取当前服务器名
     * 1. 只能 provider 端调用
     */
    public static String getCurrentHostName() {
        initHost();
        Preconditions.checkArgument(!StringUtils.isBlank(currentHostName), "必须在 provider 端调用");
        return currentHostName;
    }

    /**
     * 获取当前服务器编码
     * 1. 必须在 provider 端调用
     */
    public static byte getCurrentHostCode() {
        initHost();
        Preconditions.checkArgument(currentHostCode != null, "必须在 provider 端调用");
        return currentHostCode;
    }

    public static byte getHostCodeByName(String hostName) {
        initHost();
        return HOST_CODE_MAP.get(hostName);
    }

    /**
     * 调整主机名格式
     */
    public static String adaptHost(String host) {
        Preconditions.checkArgument(!StringUtils.isBlank(host), "主机名不能为空");
        if (host.startsWith("provider-")) {
            return host.split("-")[1];
        }
        return host;
    }

}
