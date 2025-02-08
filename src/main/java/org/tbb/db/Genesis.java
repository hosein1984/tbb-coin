package org.tbb.db;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tbb.utils.DateUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;

public class Genesis {
    private Date genesisTime;
    private String chainId;
    private final Map<Account, Long> balances = new HashMap<>();

    public static Genesis initFromDisk(Path filePath) throws IOException, ParseException {
        String content = Files.readString(filePath);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(content);

        Genesis genesis = new Genesis();
        genesis.genesisTime = DateUtils.parse(rootNode.get("genesis_time").asText());
        genesis.chainId = rootNode.get("chain_id").asText();

        JsonNode balancesNode = rootNode.get("balances");
        Iterator<Map.Entry<String, JsonNode>> balancesFields = balancesNode.fields();
        while (balancesFields.hasNext()) {
            Map.Entry<String, JsonNode> entry = balancesFields.next();
            String accountName = entry.getKey();
            long balance = entry.getValue().asLong();
            genesis.balances.put(new Account(accountName), balance);
        }

        return genesis;
    }

    public Date getGenesisTime() {
        return genesisTime;
    }

    public String getChainId() {
        return chainId;
    }

    public Map<Account, Long> getBalances() {
        return Collections.unmodifiableMap(balances);
    }
}
