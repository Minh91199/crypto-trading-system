package com.minhle.cryptotrading.crypto_trading_system.scheduler;

import com.minhle.cryptotrading.crypto_trading_system.service.PriceAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriceAggregationScheduler {

    private final PriceAggregationService priceAggregationService;

    @Scheduled(fixedDelayString = "${crypto-trading-system.config.scheduler.price-aggregation-interval}")
    public void updatePrices() {
        priceAggregationService.syncPrices();
    }
}


