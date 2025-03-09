package com.minhle.cryptotrading.crypto_trading_system.service;

import com.minhle.cryptotrading.crypto_trading_system.model.response.WalletBalanceResponse;
import java.util.List;

public interface WalletService {
  List<WalletBalanceResponse> getWalletBalances(Long userId);
}
