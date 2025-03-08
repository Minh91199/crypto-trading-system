package com.minhle.cryptotrading.crypto_trading_system.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TradeResponse {
    private Long tradeId;
    private String symbol;
    private String orderType;
    private BigDecimal executedPrice;
    private BigDecimal quantity;
    private LocalDateTime tradeTime;
    private String message;
}
