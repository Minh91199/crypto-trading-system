package com.minhle.cryptotrading.crypto_trading_system.connector.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HuobiTicker {
    private String symbol;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double amount;
    private Double vol;
    private Integer count;
    private Double bid;
    private Double bidSize;
    private Double ask;
    private Double askSize;
}

