package dev.thanbv1510.domain.model;

import dev.thanbv1510.constant.MQConstants;
import dev.thanbv1510.utils.PropertyUtils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MQConfiguration {
    private static MQConfiguration instance = null;

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

    public static synchronized MQConfiguration getInstance() {
        if (instance == null) {
            instance = MQConfiguration.builder()
                    .port(PropertyUtils.getProperty(MQConstants.PORT, Integer.class).orElse(1414))
                    .sslEnable(PropertyUtils.getProperty(MQConstants.SSL_ENABLE, Boolean.class).orElse(false))
                    .ciphersuit(PropertyUtils.getProperty(MQConstants.CIPHERSUIT, String.class).orElse("SSL_RSA_WITH_3DES_EDE_CBC_SHA"))
                    .flipRequired(PropertyUtils.getProperty(MQConstants.FLIP_REQUIRED, Boolean.class).orElse(false))
                    .trustStore(PropertyUtils.getProperty(MQConstants.TRUST_STORE, String.class).orElse(null))
                    .trustPassword(PropertyUtils.getProperty(MQConstants.TRUST_PASSWORD, String.class).orElse(null))
                    .keyStore(PropertyUtils.getProperty(MQConstants.KEYSTORE_STORE, String.class).orElse(null))
                    .keyPassword(PropertyUtils.getProperty(MQConstants.KEY_PASSWORD, String.class).orElse(null))
                    .host(PropertyUtils.getProperty(MQConstants.HOST, String.class).orElse("localhost"))
                    .transportType(PropertyUtils.getProperty(MQConstants.TRANSPORT_TYPE, Integer.class).orElse(1))
                    .qManger(PropertyUtils.getProperty(MQConstants.QMANAGER, String.class).orElse("QManager"))
                    .queue(PropertyUtils.getProperty(MQConstants.QUEUE, String.class).orElse(null))
                    .receiveTimeout(PropertyUtils.getProperty(MQConstants.RECEIVE_TIMEOUT, Integer.class).orElse(1000))
                    .channel(PropertyUtils.getProperty(MQConstants.CHANNEL, String.class).orElse("LocalChanel1"))
                    .userName(PropertyUtils.getProperty(MQConstants.USERNAME, String.class).orElse("username"))
                    .password(PropertyUtils.getProperty(MQConstants.PASSWORD, String.class).orElse("password"))
                    .build();
        }

        return instance;
    }
}
