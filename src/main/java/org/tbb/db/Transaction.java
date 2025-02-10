package org.tbb.db;


public record Transaction(Account from, Account to, long amount, TransactionType type) {
    public Transaction(Account from, Account to, long amount) {
        this(from, to, amount, TransactionType.TRANSFER);
    }

    public Transaction(String from, String to, long amount) {
        this(new Account(from), new Account(to), amount);
    }

    public Transaction(String from, String to, long amount, TransactionType type) {
        this(new Account(from), new Account(to), amount, type);
    }

    public boolean isReward() {
        return type == TransactionType.REWARD;
    }
}
