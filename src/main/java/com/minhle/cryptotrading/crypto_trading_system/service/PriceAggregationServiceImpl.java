package com.minhle.cryptotrading.crypto_trading_system.service;

import com.minhle.cryptotrading.crypto_trading_system.entity.AggregatedPrice;
import com.minhle.cryptotrading.crypto_trading_system.mapper.LatestAggregatedPriceMapper;
import com.minhle.cryptotrading.crypto_trading_system.model.response.LatestAggregatedPrice;
import org.springframework.beans.factory.annotation.Value;
import com.minhle.cryptotrading.crypto_trading_system.provider.PriceProvider;
import com.minhle.cryptotrading.crypto_trading_system.repository.AggregatedPriceRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceAggregationServiceImpl implements PriceAggregationService {

    private final AggregatedPriceRepository aggregatedPriceRepository;
    private final List<PriceProvider> priceProviders;
    private final LatestAggregatedPriceMapper latestAggregatedPriceMapper;

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

    @Override
    public List<LatestAggregatedPrice> getLatestAggregatedPrices() {
        List<LatestAggregatedPrice> response = new ArrayList<>();
        for (String symbol : supportedSymbols) {
            Optional<AggregatedPrice> opt = aggregatedPriceRepository.findTopBySymbolOrderByCreatedAtDesc(symbol);
            opt.ifPresent(ap -> {
                LatestAggregatedPrice dto = latestAggregatedPriceMapper.toLatestAggregatedPrice(ap);
                response.add(dto);
            });
        }
        return response;
    }

    private void fetchPrices() {
        for (PriceProvider provider : priceProviders) {
            provider.fetchPrice();
        }
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

