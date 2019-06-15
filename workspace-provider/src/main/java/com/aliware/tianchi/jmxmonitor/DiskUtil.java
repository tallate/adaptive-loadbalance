package com.aliware.tianchi.jmxmonitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 磁盘信息
 * 1. 磁盘空间
 * 从gkrellm的源代码看，这个是一个很复杂的数据。磁盘分区的数据有可能分布在：/proc/mounts、/proc/diskstats、 /proc/partitions等等。而且如果想要检查某几个特定的路径，还需要通过mount、df等命令的帮助。为了减少麻烦，这个数据我就直接用 statfs函数直接获得了。
 * int statfs(const char *path, struct statfs *buf);
 * 这个函数只需要输入需要检查的路径名称，就可以返回这个路径所在的分区的空间使用情况：
 * 总空间：buf.f_bsize * buf.f_blocks
 * 空余空间：buf.f_bsize * buf.f_bavail
 * 2. 磁盘IO
 * 磁盘I/O的数据也同样比较复杂，有些版本看/proc/diskstats，有些版本看/proc/partitions，还有些版本至今我也不知道在那里看……不过可以看到数据的版本也像CPU那样，需要隔一段时间取值，两次取值的差就是流量。
 */
public class DiskUtil {

    public static double getDiskRatio4Linux() {
        String rootName = "/";
        File rootFile = new File(rootName);
        if (rootFile.exists()) {
            long total = rootFile.getTotalSpace();
            long free = rootFile.getFreeSpace();
            return (double) (total - free) / total;
        }
        throw new RuntimeException("wtf???");
    }

    /**
     * 获取文件系统使用率
     * Windows下分盘符，所以返回值是一个List
     */
    public static List<String> getDisk4Windows() {
        // 操作系统
        List<String> list = new ArrayList<String>();
        for (char c = 'A'; c <= 'Z'; c++) {
            String dirName = c + ":/";
            File win = new File(dirName);
            if (win.exists()) {
                long total = (long) win.getTotalSpace();
                long free = (long) win.getFreeSpace();
                Double compare = (Double) (1 - free * 1.0 / total) * 100;
                String str = c + ":盘  已使用 " + compare.intValue() + "%";
                list.add(str);
            }
        }
        return list;
    }

}
