package org.tbb.json;

import com.google.gson.*;
import org.tbb.db.Account;
import org.tbb.db.Genesis;
import org.tbb.utils.DateUtils;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * {
 *   "genesis_time": "2019-03-18T00:00:00.000000000Z",
 *   "chain_id": "the-blockchain-bar-ledger",
 *   "balances": {
 *     "andrej": 1000000
 *   }
 * }
 */

public class GenesisSerializer implements JsonSerializer<Genesis>, JsonDeserializer<Genesis> {

    @Override
    public JsonElement serialize(Genesis src, Type typeOfSrc, JsonSerializationContext context) {
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.put("chain_id", src.getChainId());

        jsonMap.put("genesis_time", DateUtils.format(src.getGenesisTime()));
        Map<String, Long> balances = src.getBalances()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(),
                        Map.Entry::getValue,
                        (a, b) -> b,
                        LinkedHashMap::new));
        jsonMap.put("balances", balances);
        return new Gson().toJsonTree(jsonMap);
    }

    @Override
    public Genesis deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String chainId = jsonObject.get("chain_id").getAsString();
        Date genesisTime = DateUtils.mustParse(jsonObject.get("genesis_time").getAsString());
        JsonObject balancesJson = jsonObject.getAsJsonObject("balances");
        Map<Account, Long> balances = balancesJson.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> new Account(entry.getKey()),
                        entry -> entry.getValue().getAsLong(),
                        (a, b) -> b,
                        LinkedHashMap::new));
        return new Genesis(chainId, genesisTime, balances);
    }
}
