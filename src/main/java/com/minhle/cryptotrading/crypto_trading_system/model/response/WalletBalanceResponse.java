package com.minhle.cryptotrading.crypto_trading_system.model.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletBalanceResponse {
  private String currency;
  private BigDecimal balance;
}
