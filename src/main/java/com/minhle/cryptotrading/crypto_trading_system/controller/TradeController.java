package com.minhle.cryptotrading.crypto_trading_system.controller;

import com.minhle.cryptotrading.crypto_trading_system.model.request.TradeRequest;
import com.minhle.cryptotrading.crypto_trading_system.model.response.TradeHistoryResponse;
import com.minhle.cryptotrading.crypto_trading_system.model.response.TradeResponse;
import com.minhle.cryptotrading.crypto_trading_system.service.TradeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trades")
@RequiredArgsConstructor
public class TradeController {

  private final TradeService tradeService;

  @PostMapping
  public ResponseEntity<TradeResponse> executeTrade(@RequestBody TradeRequest tradeRequest) {
    TradeResponse response = tradeService.executeTrade(tradeRequest);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/history")
  public ResponseEntity<List<TradeHistoryResponse>> getTradeHistory() {
    final var tradeHistory = tradeService.getTradeHistory(1L);

    return ResponseEntity.ok(tradeHistory);
  }
}
