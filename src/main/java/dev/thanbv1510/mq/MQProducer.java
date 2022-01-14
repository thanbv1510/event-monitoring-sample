package dev.thanbv1510.mq;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import dev.thanbv1510.domain.model.MQConfiguration;

import java.io.IOException;

public class MQProducer {
    private final MQConfiguration config;
    private final MQQueueManager manager;

    public MQProducer() {
        config = new MQConfiguration();
        MQConnectionBuilder connectionBuilder = MQConnectionBuilder.getInstance();
        this.manager = connectionBuilder.getQueueManager();
    }

    public void send(String msg) throws MQException, IOException {
        MQQueue queue = this.manager.accessQueue(config.getQueue(), CMQC.MQOO_OUTPUT);
        if (queue == null) {
            return;
        }

        MQMessage mqMessage = new MQMessage();
        mqMessage.writeString(msg);
        MQPutMessageOptions pmo = new MQPutMessageOptions();
        queue.put(mqMessage, pmo);
        queue.close();
    }
}
