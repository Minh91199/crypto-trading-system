package com.minhle.cryptotrading.crypto_trading_system.model.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class LatestAggregatedPrice {
  private String symbol;
  private BigDecimal bidPrice;
  private BigDecimal askPrice;
}
