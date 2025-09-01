package utils;

import java.security.MessageDigest;

public final class HashUtils {
    public static byte[] sha1(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return md.digest(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
