package com.minhle.cryptotrading.crypto_trading_system.repository;
import com.minhle.cryptotrading.crypto_trading_system.entity.TradeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeTransactionRepository extends JpaRepository<TradeTransaction, Long> {
}

