package dev.thanbv1510.task;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import dev.thanbv1510.domain.entity.LogInfoEntity;
import dev.thanbv1510.domain.model.MQConfiguration;
import dev.thanbv1510.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;

import static dev.thanbv1510.utils.DateUtils.formatToLocalDateTime;
import static dev.thanbv1510.utils.XmlUtils.queryDataFromXMLDocument;

public class Receiver implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    private final MQConfiguration config;
    private final MQQueueManager manager;
    private final BlockingQueue<LogInfoEntity> blockingQueueLogs;

    public Receiver(MQQueueManager manager, BlockingQueue<LogInfoEntity> blockingQueueLogs) {
        this.manager = manager;
        this.blockingQueueLogs = blockingQueueLogs;
        this.config = new MQConfiguration();
    }

    @Override
    public void run() {
        MQGetMessageOptions gmo = new MQGetMessageOptions();
        gmo.options = CMQC.MQGMO_WAIT + CMQC.MQGMO_FAIL_IF_QUIESCING;
        gmo.waitInterval = 10000;  // wait up to 10 seconds

        while (true) {
            try {
                MQQueue readableQueue = manager.accessQueue(config.getQueue(), CMQC.MQGMO_WAIT + CMQC.MQGMO_FAIL_IF_QUIESCING);
                MQMessage message = new MQMessage();
                readableQueue.get(message, gmo);

                int strLen = message.getDataLength();
                byte[] strData = new byte[strLen];
                message.readFully(strData);
                String data = new String(strData);

                String content = StringUtils.decryptString(queryDataFromXMLDocument(data, "/event/bitstreamData/bitstream", null).orElse(""));

                LogInfoEntity logEntity = LogInfoEntity.builder()
                        .resultFlow(-1)
                        .command(queryDataFromXMLDocument(data, "/event/applicationData/complexContent/LogInfo/command", null).orElse(""))
                        .flowName(queryDataFromXMLDocument(data, "/event/eventPointData/messageFlowData/messageFlow", "wmb:name").orElse(""))
                        .contentType(queryDataFromXMLDocument(data, "/event/applicationData/complexContent/LogInfo/content_type", null).orElse(""))
                        .description("")
                        .stepName("")
                        .id(1L)
                        .infoLevel(queryDataFromXMLDocument(data, "/event/applicationData/complexContent/LogInfo/LogLevel", null).orElse(""))
                        .serverName(queryDataFromXMLDocument(data, "/event/applicationData/complexContent/LogInfo/system_name", null).orElse(""))
                        .systemId(1)
                        .msgContent(content)
                        .clientIp(queryDataFromXMLDocument(data, "/event/applicationData/complexContent/LogInfo/ip", null).orElse(""))
                        .username(queryDataFromXMLDocument(data, "/event/applicationData/complexContent/LogInfo/user_name", null).orElse(""))
                        .keyTime(1L)
                        .logTime(LocalDateTime.now())
                        .mflId(1)
                        .msgId("xxx")
                        .msgTime(formatToLocalDateTime(queryDataFromXMLDocument(data, "/event/eventPointData/eventData/eventSequence", "wmb:creationTime").orElse("")))
                        .msisdn("")
                        .build();

                blockingQueueLogs.add(logEntity);
            } catch (MQException | IOException e) {
                logger.info("==> No message - loop again");
            }
        }
    }
}
