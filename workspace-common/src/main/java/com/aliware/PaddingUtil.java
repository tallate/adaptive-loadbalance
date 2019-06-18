package com.aliware;

import org.apache.dubbo.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class PaddingUtil {

    private static final List<String> BLANK_STR_LIST = new ArrayList<>();

    static {
        BLANK_STR_LIST.add("");
        BLANK_STR_LIST.add(" ");
        BLANK_STR_LIST.add("  ");
        BLANK_STR_LIST.add("   ");
        BLANK_STR_LIST.add("    ");
        BLANK_STR_LIST.add("     ");
        BLANK_STR_LIST.add("      ");
        BLANK_STR_LIST.add("       ");
        BLANK_STR_LIST.add("        ");
        BLANK_STR_LIST.add("         ");
        BLANK_STR_LIST.add("          ");
        BLANK_STR_LIST.add("           ");
        BLANK_STR_LIST.add("            ");
        BLANK_STR_LIST.add("             ");
        BLANK_STR_LIST.add("              ");
        BLANK_STR_LIST.add("               ");
        BLANK_STR_LIST.add("                ");
        BLANK_STR_LIST.add("                 ");
        BLANK_STR_LIST.add("                  ");
        BLANK_STR_LIST.add("                   ");
        // 暂时没有用到超过长度8的类型了
    }

    public static String padLeft2FixedLength(String origin, int targetLength) {
        Preconditions.checkArgument(origin == null
                || origin.length() <= targetLength, "字符串不能超过目标长度");
        return BLANK_STR_LIST.get(targetLength - StringUtils.length(origin))
                + origin;
    }

    public static String padRight2FixedLength(String origin, int targetLength) {
        Preconditions.checkArgument(origin == null
                || origin.length() <= targetLength, "字符串不能超过目标长度");
        return origin +
                BLANK_STR_LIST.get(targetLength - StringUtils.length(origin));
    }

    /**
     * 解析字符串下标区间[start, end]内的内容
     */
    public static byte getByteFromPadded(String str, int start, int count) {
        Preconditions.checkArgument(!StringUtils.isBlank(str)
                && str.length() >= start + count, "字符串不能为空或短于目标长度");
        byte rst = 0;
        int end = start + count;
        for (int i = start; i < end; i++) {
            if (str.charAt(i) == ' ') {
                continue;
            }
            rst = (byte) (rst * 10 + (str.charAt(i) - '0'));
        }
        return rst;
    }

    public static long getLongFromPadded(String str, int start, int count) {
        Preconditions.checkArgument(!StringUtils.isBlank(str)
                && str.length() >= start + count, "字符串不能为空或短于目标长度");
        long rst = 0;
        int end = start + count;
        for (int i = start; i < end; i++) {
            if (str.charAt(i) == ' ') {
                continue;
            }
            rst = rst * 10 + (str.charAt(i) - '0');
        }
        return rst;
    }

}
