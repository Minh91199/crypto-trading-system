package com.minhle.cryptotrading.crypto_trading_system.model.response;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class WalletBalanceResponse {
    private String currency;
    private BigDecimal balance;
}

