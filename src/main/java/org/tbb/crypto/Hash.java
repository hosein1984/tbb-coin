package org.tbb.crypto;

import java.util.Arrays;
import java.util.HexFormat;
import java.util.Objects;

public class Hash {
    private final byte[] hash;

    private Hash(byte[] hash) {
        Objects.requireNonNull(hash, "Hash bytes cannot be null");
        this.hash = hash;
    }

    public static Hash of(byte[] hash) {
        return new Hash(hash);
    }

    public String toHex() {
        return HexFormat.of().formatHex(hash);
    }

    public static Hash fromHex(String hex) {
        return new Hash(HexFormat.of().parseHex(hex));
    }


    @Override
    public String toString() {
        return toHex();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Hash h && Arrays.equals(hash, h.hash);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash);
    }
}
