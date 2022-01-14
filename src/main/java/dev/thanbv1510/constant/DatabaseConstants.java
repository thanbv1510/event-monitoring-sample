package dev.thanbv1510.constant;

public class DatabaseConstants {
    public static final String DRIVER_NAME = "db.driverName";
    public static final String URL = "db.url";
    public static final String USERNAME = "db.user";
    public static final String PASSWORD = "db.password";
    public static final String MIN_CONNECTION = "db.minConnections";
    public static final String MAX_CONNECTION = "db.maxConnections";
    public static final String BATCH_SIZE = "db.batchSize";

    private DatabaseConstants() {
        throw new IllegalStateException("Constant class");
    }
}
