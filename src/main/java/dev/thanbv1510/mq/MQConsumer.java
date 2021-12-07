package dev.thanbv1510.mq;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;

import java.util.Optional;
import java.util.ResourceBundle;

public class MQConsumer {
    private final MQQueueManager manager;
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("config");

    public MQConsumer() {
        MQConnectionBuilder connectionBuilder = MQConnectionBuilder.getInstance();
        this.manager = connectionBuilder.getQueueManager();
    }

    public Optional<MQMessage> getMessage() throws MQException {
        if (!manager.isConnected()) {
            return Optional.empty();
        }

        MQQueue readableQueue = manager.accessQueue(resourceBundle.getString("in.queue.name"), CMQC.MQGMO_WAIT + CMQC.MQGMO_FAIL_IF_QUIESCING);
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
