package com.minhle.cryptotrading.crypto_trading_system.connector.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinanceTicker {
  private String symbol;

  private BigDecimal bidPrice;

  private BigDecimal bidQty;

  private BigDecimal askPrice;

  private BigDecimal askQty;
}
