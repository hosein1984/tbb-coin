package org.tbb.json;

import com.google.gson.*;
import org.tbb.db.Account;
import org.tbb.db.Transaction;
import org.tbb.db.TransactionType;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class TransactionSerializer implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {
    @Override
    public JsonElement serialize(Transaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.put("from", transaction.from().name());
        jsonMap.put("to", transaction.to().name());
        jsonMap.put("value", transaction.amount());
        jsonMap.put("type", transaction.type().toString());
        return new Gson().toJsonTree(jsonMap);
    }

    @Override
    public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String from = jsonObject.get("from").getAsString();
        String to = jsonObject.get("to").getAsString();
        long value = jsonObject.get("value").getAsLong();
        String type = jsonObject.get("type").getAsString();
        TransactionType transactionType = TransactionType.fromString(type);
        return new Transaction(new Account(from), new Account(to), value, transactionType);
    }
}

