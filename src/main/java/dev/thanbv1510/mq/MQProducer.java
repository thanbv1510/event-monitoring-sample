package dev.thanbv1510.mq;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;

import java.io.IOException;
import java.util.ResourceBundle;

public class MQProducer {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
    private final MQQueueManager manager;

    public MQProducer() {
        MQConnectionBuilder connectionBuilder = MQConnectionBuilder.getInstance();
        this.manager = connectionBuilder.getQueueManager();
    }

    public void send(String msg) throws MQException, IOException {
        MQQueue queue = this.manager.accessQueue(resourceBundle.getString("in.queue.name"), CMQC.MQOO_OUTPUT);
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
