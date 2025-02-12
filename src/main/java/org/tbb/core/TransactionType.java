package org.tbb.core;

import com.google.gson.annotations.SerializedName;

public enum TransactionType {
    @SerializedName("transfer")
    TRANSFER,

    @SerializedName("reward")
    REWARD;

    public static TransactionType parse(String type) {
        return switch (type) {
            case "transfer" -> TRANSFER;
            case "reward" -> REWARD;
            default -> throw new IllegalArgumentException("Unknown transaction type: " + type);
        };
    }
}
