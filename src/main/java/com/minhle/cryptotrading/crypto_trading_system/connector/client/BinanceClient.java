package com.minhle.cryptotrading.crypto_trading_system.connector.client;

import com.minhle.cryptotrading.crypto_trading_system.connector.dto.BinanceTicker;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "binanceFeignClient")
public interface BinanceClient {
  @GetMapping("/api/v3/ticker/bookTicker")
  List<BinanceTicker> getTickers();
}
