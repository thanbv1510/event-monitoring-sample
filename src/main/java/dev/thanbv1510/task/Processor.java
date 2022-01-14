package dev.thanbv1510.task;

import dev.thanbv1510.constant.DatabaseConstants;
import dev.thanbv1510.domain.entity.LogInfoEntity;
import dev.thanbv1510.repository.ILogRepository;
import dev.thanbv1510.repository.impl.LogRepository;
import dev.thanbv1510.utils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Processor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Processor.class);

    private final BlockingQueue<LogInfoEntity> blockingQueueLogs;
    private final ILogRepository logRepository;

    public Processor(BlockingQueue<LogInfoEntity> blockingQueueLogs) {
        this.blockingQueueLogs = blockingQueueLogs;

        this.logRepository = new LogRepository();
    }


    @Override
    public void run() {
        int batchSize = PropertyUtils.getProperty(DatabaseConstants.BATCH_SIZE, Integer.class).orElse(10);
        List<LogInfoEntity> logInfoEntities = new ArrayList<>();
        while (!Thread.interrupted()) {
            try {
                LogInfoEntity logInfoEntity = blockingQueueLogs.poll();
                if (logInfoEntity != null) {
                    logInfoEntities.add(logInfoEntity);
                }

                if (!logInfoEntities.isEmpty() && (blockingQueueLogs.isEmpty() || logInfoEntities.size() >= batchSize)) {
                    logRepository.saveAll(logInfoEntities);
                    logInfoEntities.clear();
                }
            } catch (Exception e) {
                logger.error("Ex: ", e);
            }
        }
    }
}
