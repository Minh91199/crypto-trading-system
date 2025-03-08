package com.minhle.cryptotrading.crypto_trading_system.model.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TradeRequest {
    @Builder.Default private Long userId = 1L;
    private String symbol;
    private String orderType;
    private BigDecimal quantity;
}

