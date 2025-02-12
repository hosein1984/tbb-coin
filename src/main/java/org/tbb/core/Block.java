package org.tbb.core;

import org.tbb.crypto.Hash;
import org.tbb.json.JsonUtils;
import org.tbb.crypto.HashUtils;

import java.util.List;
import java.util.Objects;

public record Block(BlockHeader header, BlockBody body) {
    public Block {
        Objects.requireNonNull(header, "Header cannot be null");
        Objects.requireNonNull(body, "Body cannot be null");
    }

    public Block(Hash parentHash, long number, long timestamp, List<Transaction> transactions) {
        this(new BlockHeader(parentHash, number, timestamp), new BlockBody(transactions));
    }

    public Block(Hash parentHash, long number, List<Transaction> transactions) {
        this(new BlockHeader(parentHash, number), new BlockBody(transactions));
    }

    public Block(List<Transaction> transactions) {
        this(new BlockHeader(HashUtils.empty()), new BlockBody(transactions));
    }

    public static Block empty() {
        return new Block(HashUtils.empty(), -1, List.of());
    }

    @Override
    public String toString() {
        return header.toString();
    }

    public Hash hash() {
        String json = JsonUtils.toJson(this);
        return HashUtils.sha256(json);
    }

    public long number() {
        return header.number();
    }
}
