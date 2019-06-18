package com.aliware;

import com.aliware.cluster.Server;
import java.util.List;
import org.apache.dubbo.common.utils.StringUtils;

/**
 * consumer - provider 消息编解码
 */
public class MessageUtil {

    /**
     * Server 转换成字符串后的长度
     */
    private static final long SERVER_CONTENT_LENGTH = TypeUtil.BYTE_MAX_LENGTH * 4
            + TypeUtil.LONG_MAX_LENGTH * 2;

    public static String encode(Server server) {
        Preconditions.checkArgument(server != null, "server不能为空");
        return PaddingUtil.padLeft2FixedLength("" + server.getHostCode(), TypeUtil.BYTE_MAX_LENGTH)
                + PaddingUtil.padLeft2FixedLength("" + server.getCpuLoad(), TypeUtil.BYTE_MAX_LENGTH)
                + PaddingUtil.padLeft2FixedLength("" + server.getMemoryLoad(), TypeUtil.BYTE_MAX_LENGTH)
                + PaddingUtil.padLeft2FixedLength("" + server.getNetworkLoad(), TypeUtil.BYTE_MAX_LENGTH)
                + PaddingUtil.padLeft2FixedLength("" + server.getCcc(), TypeUtil.LONG_MAX_LENGTH)
                + PaddingUtil.padLeft2FixedLength("" + server.getTime(), TypeUtil.LONG_MAX_LENGTH);
    }

    public static Server decode(String content) {
        Preconditions.checkArgument(StringUtils.length(content) == SERVER_CONTENT_LENGTH, "内容长度必须为 " + SERVER_CONTENT_LENGTH);
        byte hostCode = PaddingUtil.getByteFromPadded(content, 0, TypeUtil.BYTE_MAX_LENGTH);
        byte cpuLoad = PaddingUtil.getByteFromPadded(content, TypeUtil.BYTE_MAX_LENGTH, TypeUtil.BYTE_MAX_LENGTH);
        byte memoryLoad = PaddingUtil.getByteFromPadded(content, TypeUtil.BYTE_MAX_LENGTH * 2, TypeUtil.BYTE_MAX_LENGTH);
        byte networkLoad = PaddingUtil.getByteFromPadded(content, TypeUtil.BYTE_MAX_LENGTH * 3, TypeUtil.BYTE_MAX_LENGTH);
        long ccc = PaddingUtil.getLongFromPadded(content, TypeUtil.BYTE_MAX_LENGTH * 4, TypeUtil.LONG_MAX_LENGTH);
        long time = PaddingUtil.getLongFromPadded(content, TypeUtil.BYTE_MAX_LENGTH * 4 + TypeUtil.LONG_MAX_LENGTH, TypeUtil.LONG_MAX_LENGTH);
        return new Server()
                .setHostCode(hostCode)
                .setCpuLoad(cpuLoad)
                .setMemoryLoad(memoryLoad)
                .setNetworkLoad(networkLoad)
                .setCcc(ccc)
                .setTime(time);
    }

    /**
     * TODO: 加个Builder更正式，但是有点麻烦先放着吧
     */
    private static class MessageEncoder {

        private static class MessageField {

            private int length;

        }

        private static class FloatField {

            private int integerPrecision;

            private int decimalPrecision;

        }

        /**
         * 消息中所有字段的定义
         */
        private List<MessageField> fields;

        /**
         * 所有字段的值
         */
        private List<Object> values;

        public String encode() {
            return "";
        }

        public long decodeLong(int pos) {
            return 0;
        }

    }

}
