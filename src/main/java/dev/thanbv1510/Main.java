package dev.thanbv1510;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.headers.MQMD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.thanbv1510.entity.LogEntity;
import dev.thanbv1510.mq.MQConsumer;
import dev.thanbv1510.repository.ILogRepository;
import dev.thanbv1510.repository.impl.LogRepository;

import java.io.IOException;
import java.util.Optional;

public class Main {
    private Main() {
        throw new IllegalStateException("Main class");
    }

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        MQConsumer consumer = new MQConsumer();

        consumeMessage(consumer);
    }

    public static void consumeMessage(MQConsumer consumer) {
        ILogRepository logRepository = new LogRepository();
        MQGetMessageOptions gmo = new MQGetMessageOptions();
        gmo.options = CMQC.MQGMO_WAIT + CMQC.MQGMO_FAIL_IF_QUIESCING;
        gmo.waitInterval = 5000;  // wait up to 5 seconds
        boolean getMore = true;

        while (getMore) {
            try {
                Optional<MQMessage> mqMessageOptional = consumer.getMessage();
                if (mqMessageOptional.isPresent()) {
                    MQMessage message = mqMessageOptional.get();
                    MQMD md = new MQMD();
                    md.copyFrom(message);
                    logger.info(md.toString());
                    message.getDataLength();
                    int strLen = message.getDataLength();
                    byte[] strData = new byte[strLen];
                    message.readFully(strData);
                    String data = new String(strData);

                    LogEntity logEntity = LogEntity.builder().msg(data).build();
                    logRepository.save(logEntity);

                }


            } catch (MQException e) {
                if ((e.completionCode == CMQC.MQCC_FAILED) &&
                        (e.reasonCode == CMQC.MQRC_NO_MSG_AVAILABLE)) {
                    // No message - loop again
                } else {
                    getMore = false;
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
