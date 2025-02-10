package org.tbb.db;

public record Snapshot(String hash) {
    @Override
    public String toString() {
        return hash;
    }
}
