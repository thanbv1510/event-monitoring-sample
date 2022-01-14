package dev.thanbv1510;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Monitor extends TimerTask {
    private static final Logger logger = LoggerFactory.getLogger(Monitor.class);
    private final ScheduledThreadPoolExecutor executorService;
    private final String executorServiceName;

    public Monitor(ExecutorService executorService, String executorServiceName) {
        this.executorService = (ScheduledThreadPoolExecutor) executorService;
        this.executorServiceName = executorServiceName;
    }

    @Override
    public void run() {
        logger.info("==> Thread pool: {} with size (active/current/maximum): {}/{}/{}",
                executorServiceName,
                executorService.getActiveCount(),
                executorService.getPoolSize(),
                executorService.getLargestPoolSize()
        );
    }
}