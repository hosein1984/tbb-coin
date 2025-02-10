package org.tbb.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.tbb.db.Genesis;
import org.tbb.db.Transaction;

public class JsonUtils {
    public static String toJson(Object object) {
        Gson gson = getGson(false);
        return gson.toJson(object);
    }

    public static String toPrettyJson(Object object) {
        Gson gson = getGson(true);
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        Gson gson = getGson(false);
        return gson.fromJson(json, clazz);
    }

    private static Gson getGson(boolean pretty) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (pretty) {
            gsonBuilder.setPrettyPrinting();
        }

        gsonBuilder
                .registerTypeAdapter(Transaction.class, new TransactionSerializer())
                .registerTypeAdapter(Genesis.class, new GenesisSerializer());

        return gsonBuilder.create();
    }
}
