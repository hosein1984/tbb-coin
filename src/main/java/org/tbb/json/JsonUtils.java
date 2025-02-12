package org.tbb.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.tbb.crypto.Hash;
import org.tbb.core.Account;

import java.lang.reflect.Type;
import java.time.Instant;

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

    public static <T> T fromJson(String json, Type targetType) {
        Gson gson = getGson(false);
        return gson.fromJson(json, targetType);
    }

    private static Gson getGson(boolean pretty) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .enableComplexMapKeySerialization();
        if (pretty) {
            gsonBuilder.setPrettyPrinting();
        }

        gsonBuilder
                .registerTypeAdapter(Instant.class, new InstantSerializer())
                .registerTypeAdapter(Account.class, new AccountSerializer())
                .registerTypeAdapter(Hash.class, new HashSerializer());

        return gsonBuilder.create();
    }


}
