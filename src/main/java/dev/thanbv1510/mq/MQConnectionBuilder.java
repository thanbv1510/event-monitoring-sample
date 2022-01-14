package dev.thanbv1510.mq;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQPoolToken;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.CMQXC;
import dev.thanbv1510.domain.model.MQConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Vector;

public class MQConnectionBuilder {
    private static final Logger logger = LoggerFactory.getLogger(MQConnectionBuilder.class);
    private static MQConnectionBuilder instance;
    private final MQConfiguration config;
    private final MQPoolToken token;
    private MQQueueManager queueManager;

    private MQConnectionBuilder() {
        //Pooling connection
        this.token = MQEnvironment.addConnectionPoolToken();
        this.config = new MQConfiguration();
        MQEnvironment.hostname = config.getHost();
        MQEnvironment.channel = config.getChannel();
        MQEnvironment.port = config.getPort();
//        MQEnvironment.properties.put(CMQC.USER_ID_PROPERTY, config.getUserName());
//        MQEnvironment.properties.put(CMQC.PASSWORD_PROPERTY, config.getPassword());
        MQEnvironment.properties.put(CMQC.TRANSPORT_PROPERTY, CMQC.TRANSPORT_MQSERIES_CLIENT);

        //Compress headers
        Collection<Object> headerComp = new Vector<>();
        headerComp.add(CMQXC.MQCOMPRESS_SYSTEM);
        MQEnvironment.hdrCompList = headerComp;

        try {
            queueManager = new MQQueueManager(config.getQManger());  //create connection and return
        } catch (MQException e) {
            logger.error("==> Ex: ", e);
        }
    }

    public static MQConnectionBuilder getInstance() {
        if (instance == null) {
            instance = new MQConnectionBuilder();
        }

        return instance;
    }

    public MQQueueManager getQueueManager() {
        if (queueManager == null || !queueManager.isConnected()) {
            try {
                queueManager = new MQQueueManager(config.getQManger());
            } catch (MQException e) {
                logger.error("==> Ex: ", e);
            }
        }
        return queueManager;
    }

    public void closeConnection() {
        if (queueManager.isConnected()) {
            try {
                queueManager.close();
            } catch (MQException e) {
                logger.error("Ex: ", e);
            }
            MQEnvironment.removeConnectionPoolToken(token);
        }
    }

    /*public SSLContext createSSLContext() {
        try {
            Class.forName("com.sun.net.ssl.internal.ssl.Provider");

            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(config.getKeyStore()), config.getKeyPassword().toCharArray());

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream(config.getTrustStore()), config.getTrustPassword().toCharArray());

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            KeyManagerFactory keyManagerFactory =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(trustStore);
            keyManagerFactory.init(ks, config.getKeyPassword().toCharArray());

            SSLContext sslContext = SSLContext.getInstance("SSLv3");

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
                    null);

            return sslContext;
        } catch (Exception e) {
            logger.error("Ex: ", e);
            return null;
        }
    }*/
}
