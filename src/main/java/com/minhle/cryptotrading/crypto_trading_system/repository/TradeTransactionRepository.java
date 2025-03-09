package com.minhle.cryptotrading.crypto_trading_system.repository;

import com.minhle.cryptotrading.crypto_trading_system.entity.CryptoUser;
import com.minhle.cryptotrading.crypto_trading_system.entity.TradeTransaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeTransactionRepository extends JpaRepository<TradeTransaction, Long> {
  List<TradeTransaction> findByUserOrderByTradeTimeDesc(CryptoUser user);
}
