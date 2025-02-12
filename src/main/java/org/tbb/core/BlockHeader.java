package org.tbb.core;

import org.tbb.crypto.Hash;
import org.tbb.utils.DateUtils;

import java.util.Objects;

public record BlockHeader(Hash parentHash, long number, long timestamp) {
    public BlockHeader {
        Objects.requireNonNull(parentHash, "Parent hash cannot be null");
    }

    public BlockHeader(Hash parentHash, long number) {
        this(parentHash, number, DateUtils.unix());
    }

    public BlockHeader(Hash parentHash) {
        this(parentHash, 0);
    }
}
