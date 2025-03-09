package com.minhle.cryptotrading.crypto_trading_system.service;

import com.minhle.cryptotrading.crypto_trading_system.constant.CurrencyConstant;
import com.minhle.cryptotrading.crypto_trading_system.entity.CryptoUser;
import com.minhle.cryptotrading.crypto_trading_system.entity.Wallet;
import com.minhle.cryptotrading.crypto_trading_system.mapper.WalletMapper;
import com.minhle.cryptotrading.crypto_trading_system.model.response.WalletBalanceResponse;
import com.minhle.cryptotrading.crypto_trading_system.repository.CryptoUserRepository;
import com.minhle.cryptotrading.crypto_trading_system.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

  private final WalletRepository walletRepository;
  private final CryptoUserRepository cryptoUserRepository;
  private final WalletMapper walletMapper;

  @Override
  public List<WalletBalanceResponse> getWalletBalances(Long userId) {
    CryptoUser user =
        cryptoUserRepository
            .findById(userId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

    List<Wallet> wallets = walletRepository.findByUser(user);

    return wallets.stream()
        .filter(
            wallet ->
                CurrencyConstant.STABLE_COIN.equalsIgnoreCase(wallet.getCurrency())
                    || wallet.getBalance().compareTo(BigDecimal.ZERO) > 0)
        .map(walletMapper::toResponse)
        .collect(Collectors.toList());
  }
}
