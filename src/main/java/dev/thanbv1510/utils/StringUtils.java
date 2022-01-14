package dev.thanbv1510.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class StringUtils {
    private StringUtils() {
        throw new IllegalStateException("Util class");
    }

    public static String encryptString(String string) {
        byte[] bytesEncoded = Base64.getEncoder().encode(string.getBytes(StandardCharsets.UTF_8));
        return new String(bytesEncoded);
    }

    public static String decryptString(String string) {
        try {
            byte[] valueDecoded = Base64.getDecoder().decode(string);
            return new String(valueDecoded);
        } catch (Exception ex) {
            return string;
        }
    }
}
