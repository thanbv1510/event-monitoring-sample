package dev.thanbv1510.utils;

import dev.thanbv1510.constant.MQConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

public class PropertyUtils {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class);
    private static final Properties props = new Properties();

    static {
        try (InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(MQConstants.PROPERTIES)) {
            if (in == null) {
                throw new FileNotFoundException("==> Property file not found in the class path");
            }

            props.load(in);
        } catch (Exception e) {
            logger.error("Ex: {}", e.getMessage());
        }
    }

    private PropertyUtils() {
        throw new IllegalStateException("Util class");
    }

    public static <T> Optional<T> getProperty(String key, Class<T> clazz) {
        try {
            final Map<Class<?>, Function<String, ?>> convertData = new HashMap<>();
            convertData.put(Integer.class, Integer::valueOf);
            convertData.put(Float.class, Float::valueOf);
            convertData.put(Boolean.class, Boolean::valueOf);
            convertData.put(String.class, data -> data);

            String data = props.getProperty(key);
            if (data == null) {
                throw new IllegalArgumentException(String.format("==> Property key %s not found in the property file", key));
            }

            Function<String, ?> handler = convertData.get(clazz);
            if (handler != null) {
                return Optional.of(clazz.cast(handler.apply(data)));
            }
            throw new IllegalArgumentException(String.format("==> Cannot cast Property key %s with value: %s", key, data));
        } catch (Exception e) {
            logger.error("Ex: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
