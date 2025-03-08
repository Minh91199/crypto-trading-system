package com.minhle.cryptotrading.crypto_trading_system.connector.client;

import com.minhle.cryptotrading.crypto_trading_system.connector.response.HuobiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "huobiFeignClient")
public interface HuobiClient {
    @GetMapping("/market/tickers")
    HuobiResponse getTickers();
}

