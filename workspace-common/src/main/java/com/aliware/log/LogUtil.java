package com.aliware.log;

import com.aliware.config.LogConfig;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;

/**
 * 用Logger太麻烦了还得声明一下，用一个静态方法记一下吧
 */
public class LogUtil {

    private static Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void debug(String content) {
        if (LogConfig.DEBUG_ENABLED) {
            logger.debug(content);
        }
    }

    public static void info(String content) {
        if (LogConfig.INFO_ENABLED) {
            logger.info(content);
        }
    }

    public static void warn(String content) {
        if (LogConfig.WARN_ENABLED) {
            logger.warn(content);
        }
    }
    public static void error(String content) {
        if (LogConfig.ERROR_ENABLED) {
            logger.error(content);
        }
    }
}
