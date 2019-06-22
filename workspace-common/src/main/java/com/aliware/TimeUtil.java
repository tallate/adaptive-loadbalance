package com.aliware;

/**
 */
public class TimeUtil {

    /**
     * 获取当前秒
     */
    public static long getCurrentSecond() {
        long t = System.currentTimeMillis();
        return t / 1000;
    }

    /**
     * 获取上一秒
     */
    public static long getLastSecond() {
        long t = System.currentTimeMillis();
        return t / 1000 - 1;
    }

    public static long getCurrentMillis() {
        return System.currentTimeMillis();
    }
}
