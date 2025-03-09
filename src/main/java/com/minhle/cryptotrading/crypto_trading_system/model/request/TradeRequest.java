package com.minhle.cryptotrading.crypto_trading_system.model.request;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TradeRequest {
  private String symbol;
  private String orderType;
  private BigDecimal quantity;
}
