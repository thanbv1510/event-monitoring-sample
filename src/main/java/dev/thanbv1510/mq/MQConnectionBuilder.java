package dev.thanbv1510.mq;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQPoolToken;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.CMQXC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Vector;

public class MQConnectionBuilder {
    private static final Logger logger = LoggerFactory.getLogger(MQConnectionBuilder.class);
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
    private static MQConnectionBuilder instance;
    private static MQQueueManager queueManager;
    MQPoolToken token;

    private MQConnectionBuilder() {
        //Pooling connection
        this.token = MQEnvironment.addConnectionPoolToken();

        MQEnvironment.hostname = resourceBundle.getString("in.host");
        MQEnvironment.channel = resourceBundle.getString("in.channel");
        MQEnvironment.port = Integer.parseInt(resourceBundle.getString("in.port"));
        MQEnvironment.properties.put(CMQC.TRANSPORT_PROPERTY, CMQC.TRANSPORT_MQSERIES_CLIENT);

        //Compress headers
        Collection headerComp = new Vector();
        headerComp.add(new Integer(CMQXC.MQCOMPRESS_SYSTEM));
        MQEnvironment.hdrCompList = headerComp;

        try {
            queueManager = new MQQueueManager(resourceBundle.getString("in.queue.manager"));  //create connection and return
        } catch (MQException e) {
            logger.error("==> Ex: ", e);
        }
    }

    public static MQConnectionBuilder getInstance() {
        return instance == null ? new MQConnectionBuilder() : instance;
    }

    public MQQueueManager getQueueManager() {
        if (queueManager == null || !queueManager.isConnected()) {
            try {
                queueManager = new MQQueueManager(resourceBundle.getString("in.queue.manager"));
            } catch (MQException e) {
                logger.error("==> Ex: ", e);
            }
        }
        return queueManager;
    }
}
