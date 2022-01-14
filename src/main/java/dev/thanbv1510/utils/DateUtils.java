package dev.thanbv1510.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private DateUtils() {
        throw new IllegalStateException("Util class");
    }

    public static LocalDateTime formatToLocalDateTime(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
            return LocalDateTime.parse(dateStr, formatter);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
