package com.minhle.cryptotrading.crypto_trading_system.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TradeHistoryResponse {
  private Long tradeId;
  private String symbol;
  private String tradeType;
  private BigDecimal tradePrice;
  private BigDecimal quantity;
  private LocalDateTime tradeTime;
}
