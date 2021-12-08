package dev.thanbv1510.repository.impl;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EntityManagerFactory {
    private static final Logger logger = LoggerFactory.getLogger(EntityManagerFactory.class);
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
    private static final ComboPooledDataSource cpds = new ComboPooledDataSource();

    static {
        try {
            cpds.setDriverClass(resourceBundle.getString("db.driverName"));
            cpds.setJdbcUrl(resourceBundle.getString("db.url"));
            cpds.setUser(resourceBundle.getString("db.user"));
            cpds.setPassword(resourceBundle.getString("db.password"));
            cpds.setMinPoolSize(Integer.parseInt(resourceBundle.getString("db.min_connections")));
            cpds.setInitialPoolSize(Integer.parseInt(resourceBundle.getString("db.min_connections")));
            cpds.setMaxPoolSize(Integer.parseInt(resourceBundle.getString("db.max_connections")));
        } catch (PropertyVetoException e) {
            logger.error("==> Ex: ", e);
        }
    }

    private EntityManagerFactory() {
        throw new IllegalStateException("EntityManagerFactory class");
    }

    public static Connection getConnection() {
        try {
            return cpds.getConnection();
        } catch (SQLException e) {
            logger.error("==> Ex: ", e);
            return null;
        }
    }
}