package com.minhle.cryptotrading.crypto_trading_system.repository;

import com.minhle.cryptotrading.crypto_trading_system.entity.CryptoUser;
import com.minhle.cryptotrading.crypto_trading_system.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserAndCurrency(CryptoUser user, String currency);
    List<Wallet> findByUser(CryptoUser user);
}

