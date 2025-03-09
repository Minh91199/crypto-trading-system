package com.minhle.cryptotrading.crypto_trading_system.controller;

import com.minhle.cryptotrading.crypto_trading_system.model.response.LatestAggregatedPrice;
import com.minhle.cryptotrading.crypto_trading_system.service.PriceAggregationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {
  private final PriceAggregationService priceAggregationService;

  @GetMapping("/latest")
  public ResponseEntity<List<LatestAggregatedPrice>> getLatestAggregatedPrices() {
    List<LatestAggregatedPrice> response = priceAggregationService.getLatestAggregatedPrices();
    return ResponseEntity.ok(response);
  }
}
