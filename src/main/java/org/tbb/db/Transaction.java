package org.tbb.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

public record Transaction(Account from, Account to, long amount, TransactionType type) {
    public Transaction(Account from, Account to, long amount) {
        this(from, to, amount, TransactionType.TRANSFER);
    }

    public boolean isReward() {
        return type == TransactionType.REWARD;
    }

    public static Transaction parseJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);
        String from = rootNode.get("from").asText();
        String to = rootNode.get("to").asText();
        long value = rootNode.get("value").asLong();
        String type = rootNode.get("type").asText();
        TransactionType transactionType = TransactionType.fromString(type);
        return new Transaction(new Account(from), new Account(to), value, transactionType);
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.put("from", from.name());
        jsonMap.put("to", to.name());
        jsonMap.put("value", amount);
        jsonMap.put("type", type.toString());
        return objectMapper.writeValueAsString(jsonMap);
    }
}
