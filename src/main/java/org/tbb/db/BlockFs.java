package org.tbb.db;

import java.util.Objects;

public record BlockFs(String hash, Block block) {
    public BlockFs {
        Objects.requireNonNull(hash, "Hash cannot be null");
        Objects.requireNonNull(block, "Block cannot be null");
    }
}
