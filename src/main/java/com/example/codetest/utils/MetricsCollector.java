package com.example.codetest.utils;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class MetricsCollector {

    private static final OperatingSystemMXBean osBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public static double getProcessCpuLoad() {
        double load = osBean.getProcessCpuLoad();
        return (load >= 0) ? load * 100 : 0.0;
    }

    public static double getMemoryUsageMb() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBytes = runtime.totalMemory() - runtime.freeMemory();
        return usedMemoryBytes / (1024.0 * 1024.0);
    }
}
