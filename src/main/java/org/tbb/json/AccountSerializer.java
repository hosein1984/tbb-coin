package org.tbb.json;

import com.google.gson.*;
import org.tbb.core.Account;

import java.lang.reflect.Type;

public class AccountSerializer implements JsonSerializer<Account>, JsonDeserializer<Account> {
    @Override
    public JsonElement serialize(Account src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name());
    }

    @Override
    public Account deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Account(json.getAsString());
    }
}
