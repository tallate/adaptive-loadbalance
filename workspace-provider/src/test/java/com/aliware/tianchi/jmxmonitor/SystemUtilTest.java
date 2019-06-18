package com.aliware.tianchi.jmxmonitor;

import com.aliware.DisposableScheduledTaskUtil;
import com.aliware.tianchi.monitor.SystemUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * SystemUtil Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jun 15, 2019</pre>
 */
public class SystemUtilTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getCpuRatio()
     */
    @Test
    public void testGetCpuRatio() throws Exception {
        double cpuRatio = SystemUtil.getCpuRatio();
        System.out.println(cpuRatio);
    }

    /**
     * Method: getMemoryRatio()
     */
    @Test
    public void testGetMemoryRatio() throws Exception {
        double memoryRatio = SystemUtil.getMemoryRatio();
        System.out.println(memoryRatio);
    }

    /**
     * Method: getNetworkRatio()
     */
    @Test
    public void testGetNetworkRatio() throws Exception {
        double networkRatio = SystemUtil.getNetworkRatio();
        System.out.println(networkRatio);
    }

    /**
     * Method: getDiskRatio()
     */
    @Test
    public void testGetDiskRatio() throws Exception {
        double diskRatio = SystemUtil.getDiskRatio();
        System.out.println(diskRatio);
    }

    /**
     * Method: getSystemAverageLoad()
     */
    @Test
    public void testGetSystemAverageLoad() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        DisposableScheduledTaskUtil.scheduleAtFixedRate(() -> {
            double systemAverageLoad = SystemUtil.getSystemAverageLoad();
            System.out.println(systemAverageLoad);
            countDownLatch.countDown();
        }, 0, 5, TimeUnit.SECONDS);
        countDownLatch.await();

    }

}
