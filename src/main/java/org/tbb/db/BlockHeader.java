package org.tbb.db;

import org.tbb.utils.DateUtils;

import java.util.Objects;

public record BlockHeader(String parentHash, long timestamp) {
    public static final String EMPTY_HASH = new String(new char[64]).replace('\0', '0');

    public BlockHeader {
        Objects.requireNonNull(parentHash, "Parent hash cannot be null");
        if (parentHash.length() != 64) {
            throw new IllegalArgumentException("Invalid parent hash length");
        }
    }

    public BlockHeader(String parentHash) {
        this(parentHash, DateUtils.unix());
    }
}
