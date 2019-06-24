package com.aliware.cluster;

import com.aliware.PaddingUtil;
import com.aliware.Preconditions;
import com.aliware.TypeUtil;
import com.aliware.cluster.Server;
import org.apache.dubbo.common.utils.StringUtils;

import java.util.List;

/**
 * consumer - provider 消息内容编解码
 */
public class MessageUtil {

    /**
     * Server 转换成字符串后的长度
     */
    private static final long SERVER_CONTENT_LENGTH = TypeUtil.BYTE_MAX_LENGTH
            + TypeUtil.LONG_MAX_LENGTH * 3;

    public static String encode(Server server) {
        Preconditions.checkArgument(server != null, "server不能为空");
        return PaddingUtil.padLeft2FixedLength("" + server.getHostCode(), TypeUtil.BYTE_MAX_LENGTH)
                + PaddingUtil.padLeft2FixedLength("" + server.getCollectTime(), TypeUtil.LONG_MAX_LENGTH)
                + PaddingUtil.padLeft2FixedLength("" + server.getThroughput(), TypeUtil.LONG_MAX_LENGTH)
                + PaddingUtil.padLeft2FixedLength("" + server.getTime(), TypeUtil.LONG_MAX_LENGTH);
    }

    public static Server decode(String content) {
        Preconditions.checkArgument(StringUtils.length(content) == SERVER_CONTENT_LENGTH, "内容长度必须为 " + SERVER_CONTENT_LENGTH);
        byte hostCode = PaddingUtil.getByteFromPadded(content, 0, TypeUtil.BYTE_MAX_LENGTH);
        long collectTime = PaddingUtil.getLongFromPadded(content, TypeUtil.BYTE_MAX_LENGTH, TypeUtil.LONG_MAX_LENGTH);
        long throughput = PaddingUtil.getLongFromPadded(content, TypeUtil.BYTE_MAX_LENGTH + TypeUtil.LONG_MAX_LENGTH, TypeUtil.LONG_MAX_LENGTH);
        long time = PaddingUtil.getLongFromPadded(content, TypeUtil.BYTE_MAX_LENGTH + TypeUtil.LONG_MAX_LENGTH * 2, TypeUtil.LONG_MAX_LENGTH);
        return new Server(hostCode)
                .setCollectTime(collectTime)
                .setThroughput(throughput)
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
