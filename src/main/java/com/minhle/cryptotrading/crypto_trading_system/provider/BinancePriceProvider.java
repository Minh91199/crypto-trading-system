package com.minhle.cryptotrading.crypto_trading_system.provider;

import com.minhle.cryptotrading.crypto_trading_system.connector.client.BinanceClient;
import com.minhle.cryptotrading.crypto_trading_system.connector.dto.BinanceTicker;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BinancePriceProvider implements PriceProvider {

  private final BinanceClient binanceClient;
  private List<BinanceTicker> cachedTickers;

  @Override
  public void fetchPrice() {
    cachedTickers = binanceClient.getTickers();
  }

  private List<BinanceTicker> getTickersForSymbol(String symbol) {
    return cachedTickers.stream()
        .filter(t -> t.getSymbol().equalsIgnoreCase(symbol))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<BigDecimal> getBidPrice(String symbol) {
    List<BinanceTicker> tickersForSymbol = getTickersForSymbol(symbol);
    return tickersForSymbol.stream().map(BinanceTicker::getBidPrice).max(BigDecimal::compareTo);
  }

  @Override
  public Optional<BigDecimal> getAskPrice(String symbol) {
    List<BinanceTicker> tickersForSymbol = getTickersForSymbol(symbol);
    return tickersForSymbol.stream().map(BinanceTicker::getAskPrice).min(BigDecimal::compareTo);
  }
}
