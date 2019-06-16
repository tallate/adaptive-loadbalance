package com.aliware;

import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一次性定时任务
 */
public class DisposableScheduledTaskUtil {

    private static Logger logger = LoggerFactory.getLogger(DisposableScheduledTaskUtil.class);

    private static final String THREAD_NAME_PREFIX = "DISPOSABLE_";

    private static AtomicLong THREAD_NO = new AtomicLong();

    private static final Lock LOCK = new ReentrantLock();

    private static final ScheduledExecutorService THREAD_POOL =
            new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                    runnable -> {
                        Thread thread = new Thread(runnable, THREAD_NAME_PREFIX + THREAD_NO.incrementAndGet());
                        // wrap 回到 0 ，避免长期运行（其实这个线程池也没打算长期运行）
                        if (THREAD_NO.get() >= Long.MAX_VALUE / 2) {
                            LOCK.lock();
                            if (THREAD_NO.get() >= Long.MAX_VALUE / 2) {
                                THREAD_NO.set(0);
                            }
                            LOCK.unlock();
                        }
                        return thread;
                    });

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            THREAD_POOL.shutdown();
            try {
                if (!THREAD_POOL.awaitTermination(10, TimeUnit.SECONDS)) {
                    //规定时间内耗时的task 没有完成
                    THREAD_POOL.shutdownNow(); // 强制关闭
                }
            } catch (InterruptedException e) {
                logger.error("awaitTermination exception", e);
                THREAD_POOL.shutdownNow();
            }
        }));
    }

    public static void submit(Runnable task) {
        THREAD_POOL.submit(task);
    }

    public static void scheduleAtFixedRate(Runnable task, long start, long interval, TimeUnit timeUnit) {
        THREAD_POOL.scheduleAtFixedRate(task, start, interval, timeUnit);
    }

}
