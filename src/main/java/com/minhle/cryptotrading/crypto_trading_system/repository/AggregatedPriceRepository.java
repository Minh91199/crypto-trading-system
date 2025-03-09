package com.minhle.cryptotrading.crypto_trading_system.repository;

import com.minhle.cryptotrading.crypto_trading_system.entity.AggregatedPrice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AggregatedPriceRepository extends JpaRepository<AggregatedPrice, Long> {
  Optional<AggregatedPrice> findTopBySymbolOrderByCreatedAtDesc(String symbol);
}
