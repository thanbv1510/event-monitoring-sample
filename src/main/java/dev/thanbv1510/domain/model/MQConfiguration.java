package dev.thanbv1510.domain.model;

import dev.thanbv1510.constant.MQConstants;
import dev.thanbv1510.utils.PropertyUtils;
import lombok.Data;

@Data
public class MQConfiguration {
    // MQ
    private String host;
    private int port;
    private String qManger;
    private String queue;
    private String topic;
    private String channel;
    private String userName;
    private String password;
    private int transportType;
    private int receiveTimeout;
    private String ciphersuit;
    private Boolean flipRequired;
    private boolean sslEnable;
    private String trustStore;
    private String trustPassword;
    private String keyStore;
    private String keyPassword;

    public MQConfiguration() {
        this.port = PropertyUtils.getProperty(MQConstants.PORT, Integer.class).orElse(1414);
        this.sslEnable = PropertyUtils.getProperty(MQConstants.SSL_ENABLE, Boolean.class).orElse(false);
        this.ciphersuit = PropertyUtils.getProperty(MQConstants.CIPHERSUIT, String.class).orElse("SSL_RSA_WITH_3DES_EDE_CBC_SHA");
        this.flipRequired = PropertyUtils.getProperty(MQConstants.FLIP_REQUIRED, Boolean.class).orElse(false);
        this.trustStore = PropertyUtils.getProperty(MQConstants.TRUST_STORE, String.class).orElse(null);
        this.trustPassword = PropertyUtils.getProperty(MQConstants.TRUST_PASSWORD, String.class).orElse(null);
        this.keyStore = PropertyUtils.getProperty(MQConstants.KEYSTORE_STORE, String.class).orElse(null);
        this.keyPassword = PropertyUtils.getProperty(MQConstants.KEY_PASSWORD, String.class).orElse(null);
        this.host = PropertyUtils.getProperty(MQConstants.HOST, String.class).orElse("localhost");
        this.transportType = PropertyUtils.getProperty(MQConstants.TRANSPORT_TYPE, Integer.class).orElse(1);
        this.qManger = PropertyUtils.getProperty(MQConstants.QMANAGER, String.class).orElse("QManager");
        this.queue = PropertyUtils.getProperty(MQConstants.QUEUE, String.class).orElse(null);
        this.receiveTimeout = PropertyUtils.getProperty(MQConstants.RECEIVE_TIMEOUT, Integer.class).orElse(1000);
        this.channel = PropertyUtils.getProperty(MQConstants.CHANNEL, String.class).orElse("LocalChanel1");
        this.userName = PropertyUtils.getProperty(MQConstants.USERNAME, String.class).orElse("username");
        this.password = PropertyUtils.getProperty(MQConstants.PASSWORD, String.class).orElse("password");
    }
}
