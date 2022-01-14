package dev.thanbv1510;

import com.ibm.mq.MQQueueManager;
import dev.thanbv1510.constant.ThreadConstants;
import dev.thanbv1510.domain.entity.LogInfoEntity;
import dev.thanbv1510.mq.MQConnectionBuilder;
import dev.thanbv1510.task.Processor;
import dev.thanbv1510.task.Receiver;
import dev.thanbv1510.utils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    private static final Logger logger;
    private static final BlockingQueue<LogInfoEntity> blockingQueueLogs;

    static {
        logger = LoggerFactory.getLogger(Main.class);
        blockingQueueLogs = new LinkedBlockingQueue<>();
    }

    private Main() {
        throw new IllegalStateException("Main class");
    }

    public static void main(String[] args) {
        logger.info("==> STARTING APPLICATION");
        long maxBytes = Runtime.getRuntime().maxMemory();
        logger.info("==> Max memory: {}M", maxBytes / 1024 / 1024);

        initReceivers();
        initProcessors();
    }

    private static void initReceivers() {
        MQQueueManager queueManager = MQConnectionBuilder.getInstance().getQueueManager();
        // Init thread to reading from queue
        int receiverCorePoolSize = PropertyUtils.getProperty(ThreadConstants.RECEIVER_CORE_POOL_SIZE, Integer.class).orElse(10);
        ExecutorService executorReceiverService = Executors.newScheduledThreadPool(receiverCorePoolSize);
        for (int i = 0; i < receiverCorePoolSize; i++) {
            Runnable thread = new Receiver(queueManager, blockingQueueLogs);
            executorReceiverService.execute(thread);
        }

        monitor(executorReceiverService, "Executor Receiver");
    }

    private static void initProcessors() {
        // Init thread to save data to database
        int processorCorePoolSize = PropertyUtils.getProperty(ThreadConstants.PROCESSOR_CORE_POOL_SIZE, Integer.class).orElse(10);
        ExecutorService executorProcessorService = Executors.newScheduledThreadPool(processorCorePoolSize);
        for (int i = 0; i < processorCorePoolSize; i++) {
            Runnable thread = new Processor(blockingQueueLogs);
            executorProcessorService.execute(thread);
        }

        monitor(executorProcessorService, "Executor Processor");
    }

    private static void monitor(ExecutorService executorService, String executorServiceName) {
        int period = PropertyUtils.getProperty(ThreadConstants.MONITOR_TIME, Integer.class).orElse(60);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Monitor(executorService, executorServiceName), 0, period * 1000L);
    }
}
