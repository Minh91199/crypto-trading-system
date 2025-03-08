package com.minhle.cryptotrading.crypto_trading_system.service;

import com.minhle.cryptotrading.crypto_trading_system.entity.AggregatedPrice;
import org.springframework.beans.factory.annotation.Value;
import com.minhle.cryptotrading.crypto_trading_system.provider.BinancePriceProvider;
import com.minhle.cryptotrading.crypto_trading_system.provider.HuobiPriceProvider;
import com.minhle.cryptotrading.crypto_trading_system.provider.PriceProvider;
import com.minhle.cryptotrading.crypto_trading_system.repository.AggregatedPriceRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceAggregationServiceImpl implements PriceAggregationService {

    private final AggregatedPriceRepository aggregatedPriceRepository;
    private final List<PriceProvider> priceProviders;
    private final BinancePriceProvider binancePriceProvider;
    private final HuobiPriceProvider huobiPriceProvider;

    @Value("#{'${crypto-trading-system.config.trading.supported-symbols}'.split(',')}")
    private List<String> supportedSymbols;

    @Override
    public void syncPrices() {
        fetchPrices();

        supportedSymbols.forEach(symbol -> {
            BestPrice bestPrice = calculateBestPrice(symbol);
            AggregatedPrice aggregatedPrice = AggregatedPrice.builder()
                    .symbol(symbol)
                    .bidPrice(bestPrice.getBidPrice())
                    .askPrice(bestPrice.getAskPrice())
                    .createdAt(LocalDateTime.now())
                    .build();
            aggregatedPriceRepository.save(aggregatedPrice);
        });
    }

    private void fetchPrices() {
        binancePriceProvider.refreshTickers();
        huobiPriceProvider.refreshTickers();
    }

    private BestPrice calculateBestPrice(String symbol) {
        Optional<BigDecimal> maxBid = priceProviders.stream()
                .map(provider -> provider.getBidPrice(symbol))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .max(BigDecimal::compareTo);

        Optional<BigDecimal> minAsk = priceProviders.stream()
                .map(provider -> provider.getAskPrice(symbol))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .min(BigDecimal::compareTo);

        return new BestPrice(maxBid.orElse(BigDecimal.ZERO), minAsk.orElse(BigDecimal.ZERO));
    }

    @Data
    @AllArgsConstructor
    private static class BestPrice {
        private BigDecimal bidPrice;
        private BigDecimal askPrice;
    }
}

