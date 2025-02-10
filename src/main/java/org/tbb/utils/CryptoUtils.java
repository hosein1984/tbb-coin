package org.tbb.utils;

import java.security.MessageDigest;

public class CryptoUtils {
    public static String md5(String input) {
        return hash(input, "MD5");
    }

    public static String sha256(String input) {
        return hash(input, "SHA-256");
    }

    public static String sha512(String input) {
        return hash(input, "SHA-512");
    }

    private static String hash(String input, String algorithm) {
        MessageDigest digest = getMessageDigest(algorithm);
        byte[] hash = digest.digest(input.getBytes());
        return bytesToHex(hash);
    }

    private static MessageDigest getMessageDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            throw new RuntimeException("Could not get message digest for algorithm: " + algorithm, e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
