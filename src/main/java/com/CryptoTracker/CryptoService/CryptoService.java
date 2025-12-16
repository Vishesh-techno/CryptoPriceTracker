package com.CryptoTracker.CryptoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.CryptoTracker.model.CryptoCoins;

@Service
public class CryptoService {

    private final String API_URL =
        "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=usd&include_24hr_change=true";

    private static final Map<String, String> COIN_MAP = Map.ofEntries(
        Map.entry("bitcoin", "bitcoin"),
        Map.entry("btc", "bitcoin"),

        Map.entry("ethereum", "ethereum"),
        Map.entry("eth", "ethereum"),

        Map.entry("dogecoin", "dogecoin"),
        Map.entry("doge", "dogecoin"),

        Map.entry("solana", "solana"),
        Map.entry("sol", "solana"),

        Map.entry("cardano", "cardano"),
        Map.entry("ada", "cardano"),

        Map.entry("bnb", "binancecoin"),
        Map.entry("binance", "binancecoin")
    );

    public List<CryptoCoins> getCryptoPrices(List<String> inputCoins) {

        List<CryptoCoins> result = new ArrayList<>();
        List<String> validCoins = new ArrayList<>();

        for (String coin : inputCoins) {
            if (COIN_MAP.containsKey(coin)) {
                validCoins.add(COIN_MAP.get(coin));
            } else {
                System.out.println("Coin not supported: " + coin);
            }
        }

        if (validCoins.isEmpty()) return result;

        String ids = String.join(",", validCoins);
        String url = String.format(API_URL, ids);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        JSONObject json = new JSONObject(response);

        for (String coinId : validCoins) {
            if (json.has(coinId)) {
                JSONObject c = json.getJSONObject(coinId);

                result.add(new CryptoCoins(
                        coinId,
                        coinId.toUpperCase(),
                        c.getDouble("usd"),
                        c.getDouble("usd_24h_change")
                ));
            }
        }

        return result;
    }
}

