package com.minhle.cryptotrading.crypto_trading_system.repository;

import com.minhle.cryptotrading.crypto_trading_system.entity.AggregatedPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AggregatedPriceRepository extends JpaRepository<AggregatedPrice, Integer> {
    Optional<AggregatedPrice> findTopBySymbolOrderByCreatedAtDesc(String symbol);
}

