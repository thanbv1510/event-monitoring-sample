package dev.thanbv1510.constant;

public class ThreadConstants {
    public static final String RECEIVER_CORE_POOL_SIZE = "thread.receiver.corePoolSize";
    public static final String PROCESSOR_CORE_POOL_SIZE = "thread.processor.corePoolSize";
    public static final String MONITOR_TIME = "thread.monitor.time";

    private ThreadConstants() {
        throw new IllegalStateException("Constant class");
    }
}
