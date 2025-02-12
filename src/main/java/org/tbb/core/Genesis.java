package org.tbb.core;

import org.tbb.json.JsonUtils;
import org.tbb.utils.DateUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;

public class Genesis {
    private final String chainId;
    private final Instant genesisTime;
    private final Map<Account, Long> balances = new LinkedHashMap<>();

    public static final Genesis DEFAULT = new Genesis(
            "the-blockchain-bar-ledger",
            DateUtils.mustParse("2019-03-18T00:00:00.000000000Z"),
            Map.of(
                    new Account("andrej"), 1000000L
            ));

    public Genesis(String chainId, Instant genesisTime, Map<Account, Long> balances) {
        this.chainId = chainId;
        this.genesisTime = genesisTime;
        this.balances.putAll(balances);
    }

    public static Genesis initFromDisk(Path filePath) throws IOException {
        String content = Files.readString(filePath);
        return JsonUtils.fromJson(content, Genesis.class);
    }

    public Instant getGenesisTime() {
        return genesisTime;
    }

    public String getChainId() {
        return chainId;
    }

    public Map<Account, Long> getBalances() {
        return Collections.unmodifiableMap(balances);
    }
}
