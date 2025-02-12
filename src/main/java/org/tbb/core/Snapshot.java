package org.tbb.core;

public record Snapshot(String hash) {
    @Override
    public String toString() {
        return hash;
    }
}
