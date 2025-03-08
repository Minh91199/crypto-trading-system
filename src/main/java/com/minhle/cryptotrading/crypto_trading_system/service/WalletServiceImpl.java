package com.minhle.cryptotrading.crypto_trading_system.service;

import com.minhle.cryptotrading.crypto_trading_system.entity.CryptoUser;
import com.minhle.cryptotrading.crypto_trading_system.entity.Wallet;
import com.minhle.cryptotrading.crypto_trading_system.model.response.WalletBalanceResponse;
import com.minhle.cryptotrading.crypto_trading_system.repository.CryptoUserRepository;
import com.minhle.cryptotrading.crypto_trading_system.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final CryptoUserRepository cryptoUserRepository;

    @Override
    public List<WalletBalanceResponse> getWalletBalances(Long userId) {
        CryptoUser user = cryptoUserRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        List<Wallet> wallets = walletRepository.findByUser(user);

        return wallets.stream()
                .filter(wallet ->
                        "USDT".equalsIgnoreCase(wallet.getCurrency()) ||
                                wallet.getBalance().compareTo(BigDecimal.ZERO) > 0)
                .map(wallet -> WalletBalanceResponse.builder()
                        .currency(wallet.getCurrency())
                        .balance(wallet.getBalance())
                        .build())
                .collect(Collectors.toList());
    }

}

