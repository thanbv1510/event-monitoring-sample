package dev.thanbv1510.constant;

public class ThreadConstants {
    public static final String RECEIVER_CORE_POOL_SIZE = "thread.receiver.corePoolSize";
    public static final String PROCESSOR_CORE_POOL_SIZE = "thread.processor.corePoolSize";

    private ThreadConstants() {
        throw new IllegalStateException("Constant class");
    }
}
