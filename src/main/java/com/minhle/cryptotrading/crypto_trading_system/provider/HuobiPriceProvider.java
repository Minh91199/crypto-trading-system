package com.minhle.cryptotrading.crypto_trading_system.provider;
import com.minhle.cryptotrading.crypto_trading_system.connector.client.HuobiClient;
import com.minhle.cryptotrading.crypto_trading_system.connector.dto.HuobiTicker;
import com.minhle.cryptotrading.crypto_trading_system.connector.response.HuobiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HuobiPriceProvider implements PriceProvider {

    private final HuobiClient huobiClient;
    private List<HuobiTicker> cachedTickers;

    @Override
    public void fetchPrice() {
        HuobiResponse response = huobiClient.getTickers();
        cachedTickers = response.getData();
    }

    private List<HuobiTicker> getTickersForSymbol(String symbol) {
        return cachedTickers.stream()
                .filter(t -> t.getSymbol().equalsIgnoreCase(symbol.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BigDecimal> getBidPrice(String symbol) {
        return getTickersForSymbol(symbol).stream()
                .map(t -> BigDecimal.valueOf(t.getBid()))
                .max(BigDecimal::compareTo);
    }

    @Override
    public Optional<BigDecimal> getAskPrice(String symbol) {
        return getTickersForSymbol(symbol).stream()
                .map(t -> BigDecimal.valueOf(t.getAsk()))
                .min(BigDecimal::compareTo);
    }
}

