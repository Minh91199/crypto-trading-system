package com.minhle.cryptotrading.crypto_trading_system.repository;

import com.minhle.cryptotrading.crypto_trading_system.entity.CryptoUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoUserRepository extends JpaRepository<CryptoUser, Long> {}
