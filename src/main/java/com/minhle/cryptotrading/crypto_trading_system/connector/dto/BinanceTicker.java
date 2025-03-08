package com.minhle.cryptotrading.crypto_trading_system.connector.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BinanceTicker {
    private String symbol;

    private BigDecimal bidPrice;

    private BigDecimal bidQty;

    private BigDecimal askPrice;

    private BigDecimal askQty;
}

