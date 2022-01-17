package dev.thanbv1510.mq;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import dev.thanbv1510.domain.model.MQConfiguration;

import java.util.Optional;

public class MQConsumer {
    private final MQQueueManager manager;
    private final MQConfiguration config;

    public MQConsumer() {
        this.config = MQConfiguration.getInstance();
        MQConnectionBuilder connectionBuilder = MQConnectionBuilder.getInstance();
        this.manager = connectionBuilder.getQueueManager();
    }

    public Optional<MQMessage> getMessage() throws MQException {
        if (!manager.isConnected()) {
            return Optional.empty();
        }

        MQQueue readableQueue = manager.accessQueue(config.getQueue(), CMQC.MQGMO_WAIT + CMQC.MQGMO_FAIL_IF_QUIESCING);
        if (readableQueue == null) {
            return Optional.empty();
        }

        MQMessage message = new MQMessage();
        MQGetMessageOptions gmo = new MQGetMessageOptions();
        gmo.options = CMQC.MQGMO_WAIT + CMQC.MQGMO_FAIL_IF_QUIESCING;
        gmo.waitInterval = 5000;  // wait up to 5 seconds
        readableQueue.get(message, gmo);

        return Optional.of(message);
    }
}
