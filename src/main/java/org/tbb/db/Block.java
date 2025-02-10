package org.tbb.db;

import org.tbb.json.JsonUtils;
import org.tbb.utils.CryptoUtils;

import java.util.List;
import java.util.Objects;

public record Block(BlockHeader header, BlockBody body) {
    public Block {
        Objects.requireNonNull(header, "Header cannot be null");
        Objects.requireNonNull(body, "Body cannot be null");
    }

    public Block(String parentHash, long timestamp, List<Transaction> transactions) {
        this(new BlockHeader(parentHash, timestamp), new BlockBody(transactions));
    }

    public Block(String parentHash, List<Transaction> transactions) {
        this(new BlockHeader(parentHash), new BlockBody(transactions));
    }

    public Block(List<Transaction> transactions) {
        this(BlockHeader.EMPTY_HASH, transactions);
    }

    @Override
    public String toString() {
        return header.toString();
    }

    public String hash() {
        String json = JsonUtils.toJson(this);
        return CryptoUtils.sha256(json);
    }
}
