package com.minhle.cryptotrading.crypto_trading_system.model.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LatestAggregatedPrice {
  private String symbol;
  private BigDecimal bidPrice;
  private BigDecimal askPrice;
}
