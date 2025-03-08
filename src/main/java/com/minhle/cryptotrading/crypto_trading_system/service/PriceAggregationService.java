package com.minhle.cryptotrading.crypto_trading_system.service;

import com.minhle.cryptotrading.crypto_trading_system.model.response.LatestAggregatedPrice;

import java.util.List;

public interface PriceAggregationService {
    void syncPrices();

    List<LatestAggregatedPrice> getLatestAggregatedPrices();
}

