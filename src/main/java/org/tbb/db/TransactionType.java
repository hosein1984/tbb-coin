package org.tbb.db;

public enum TransactionType {
    TRANSFER,
    REWARD;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static TransactionType fromString(String value) {
        return TransactionType.valueOf(value.toUpperCase());
    }
}
