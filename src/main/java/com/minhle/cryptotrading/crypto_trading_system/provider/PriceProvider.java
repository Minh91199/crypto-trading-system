package com.minhle.cryptotrading.crypto_trading_system.provider;

import java.math.BigDecimal;
import java.util.Optional;

public interface PriceProvider {
  void fetchPrice();

  Optional<BigDecimal> getBidPrice(String symbol);

  Optional<BigDecimal> getAskPrice(String symbol);
}
