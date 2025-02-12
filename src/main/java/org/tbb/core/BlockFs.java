package org.tbb.core;

import org.tbb.crypto.Hash;

import java.util.Objects;

public record BlockFs(Hash hash, Block block) {
    public BlockFs {
        Objects.requireNonNull(hash, "Hash cannot be null");
        Objects.requireNonNull(block, "Block cannot be null");
    }
}
