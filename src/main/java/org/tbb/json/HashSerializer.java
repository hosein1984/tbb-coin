package org.tbb.json;

import com.google.gson.*;
import org.tbb.crypto.Hash;

import java.lang.reflect.Type;


public class HashSerializer implements JsonSerializer<Hash>, JsonDeserializer<Hash> {
    @Override
    public JsonElement serialize(Hash src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toHex());
    }

    @Override
    public Hash deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Hash.fromHex(json.getAsString());
    }
}
