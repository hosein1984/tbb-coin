package org.tbb.core;

import java.util.List;
import java.util.Objects;

public record BlockBody(List<Transaction> transactions) {
    public BlockBody {
        Objects.requireNonNull(transactions, "Transactions cannot be null");
    }
}
