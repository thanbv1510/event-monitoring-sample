package dev.thanbv1510.repository.impl;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import dev.thanbv1510.constant.DatabaseConstants;
import dev.thanbv1510.utils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class EntityManagerFactory {
    private static final Logger logger = LoggerFactory.getLogger(EntityManagerFactory.class);
    private static final ComboPooledDataSource cpds = new ComboPooledDataSource();

    static {
        try {
            cpds.setDriverClass(PropertyUtils.getProperty(DatabaseConstants.DRIVER_NAME, String.class).orElse("oracle.jdbc.driver.OracleDriver"));
            cpds.setJdbcUrl(PropertyUtils.getProperty(DatabaseConstants.URL, String.class).orElse("jdbc:oracle:thin:@192.168.10.151:1521/esb"));
            cpds.setUser(PropertyUtils.getProperty(DatabaseConstants.USERNAME, String.class).orElse("esb"));
            cpds.setPassword(PropertyUtils.getProperty(DatabaseConstants.PASSWORD, String.class).orElse("esb"));
            cpds.setMinPoolSize(PropertyUtils.getProperty(DatabaseConstants.MIN_CONNECTION, Integer.class).orElse(5));
            cpds.setInitialPoolSize(PropertyUtils.getProperty(DatabaseConstants.MIN_CONNECTION, Integer.class).orElse(5));
            cpds.setMaxPoolSize(PropertyUtils.getProperty(DatabaseConstants.MAX_CONNECTION, Integer.class).orElse(10));
        } catch (PropertyVetoException e) {
            logger.error("==> Ex: {}", e.getMessage());
        }
    }

    private EntityManagerFactory() {
        throw new IllegalStateException("EntityManagerFactory class");
    }

    public static Connection getConnection() {
        try {
            return cpds.getConnection();
        } catch (SQLException e) {
            logger.error("==> Ex: {}", e.getMessage());
            return null;
        }
    }
}