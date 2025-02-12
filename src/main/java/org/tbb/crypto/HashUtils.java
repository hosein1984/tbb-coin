package org.tbb.crypto;

import java.security.MessageDigest;

public class HashUtils {
    private static final int SHA1_LENGTH = 20;
    private static final int SHA224_LENGTH = 28;
    private static final int SHA256_LENGTH = 32;
    private static final int SHA512_LENGTH = 64;
    private static final int MD5_LENGTH = 16;

    public static Hash emptySha256() {
        return Hash.of(new byte[SHA256_LENGTH]);
    }

    public static Hash emptySha512() {
        return Hash.of(new byte[SHA512_LENGTH]);
    }

    public static Hash emptySha224() {
        return Hash.of(new byte[SHA224_LENGTH]);
    }

    public static Hash emptySha1() {
        return Hash.of(new byte[SHA1_LENGTH]);
    }

    public static Hash emptyMd5() {
        return Hash.of(new byte[MD5_LENGTH]);
    }

    public static Hash empty() {
        return emptySha256();
    }

    public static Hash md5(String input) {
        return hash(input, "MD5");
    }

    public static Hash sha256(String input) {
        return hash(input, "SHA-256");
    }

    public static Hash sha512(String input) {
        return hash(input, "SHA-512");
    }

    private static Hash hash(String input, String algorithm) {
        MessageDigest digest = getMessageDigest(algorithm);
        byte[] hash = digest.digest(input.getBytes());
        return Hash.of(hash);
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
