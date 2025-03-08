package com.minhle.cryptotrading.crypto_trading_system.model.response;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

