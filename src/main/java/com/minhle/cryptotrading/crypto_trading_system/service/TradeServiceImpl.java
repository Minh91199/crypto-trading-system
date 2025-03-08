package com.minhle.cryptotrading.crypto_trading_system.service;

import com.minhle.cryptotrading.crypto_trading_system.entity.AggregatedPrice;
import com.minhle.cryptotrading.crypto_trading_system.entity.CryptoUser;
import com.minhle.cryptotrading.crypto_trading_system.entity.TradeTransaction;
import com.minhle.cryptotrading.crypto_trading_system.entity.Wallet;
import com.minhle.cryptotrading.crypto_trading_system.enums.TradeType;
import com.minhle.cryptotrading.crypto_trading_system.model.request.TradeRequest;
import com.minhle.cryptotrading.crypto_trading_system.model.response.TradeResponse;
import com.minhle.cryptotrading.crypto_trading_system.repository.AggregatedPriceRepository;
import com.minhle.cryptotrading.crypto_trading_system.repository.CryptoUserRepository;
import com.minhle.cryptotrading.crypto_trading_system.repository.TradeTransactionRepository;
import com.minhle.cryptotrading.crypto_trading_system.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final TradeTransactionRepository tradeTransactionRepository;
    private final AggregatedPriceRepository aggregatedPriceRepository;
    private final CryptoUserRepository cryptoUserRepository;
    private final WalletRepository walletRepository;

    @Value("#{'${crypto-trading-system.config.trading.supported-symbols}'.split(',')}")
    private List<String> supportedSymbols;

    @Override
    @Transactional
    public TradeResponse executeTrade(TradeRequest tradeRequest) {
        String symbol = tradeRequest.getSymbol().toUpperCase();

        // Validate if the symbol is supported.
        if (!supportedSymbols.contains(symbol)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Symbol " + symbol + " is not supported");
        }

        CryptoUser user = cryptoUserRepository.findById(tradeRequest.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        // USDT wallet is the base currency.
        String baseCurrency = "USDT";

        // Extract coin currency from symbol
        String coinCurrency = symbol.replace(baseCurrency, "");
        Wallet usdtWallet = walletRepository.findByUserAndCurrency(user, baseCurrency)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "USDT wallet not found"));
        Wallet coinWallet;

        // For BUY orders, auto-create coin wallet if not exists.
        TradeType orderType = parseOrderType(tradeRequest.getOrderType());
        if (orderType == TradeType.BUY) {
            coinWallet = walletRepository.findByUserAndCurrency(user, coinCurrency)
                    .orElseGet(() -> walletRepository.save(
                            Wallet.builder()
                                    .user(user)
                                    .currency(coinCurrency)
                                    .balance(BigDecimal.ZERO)
                                    .updatedAt(LocalDateTime.now())
                                    .build()
                    ));
        } else {
            // SELL order requires existing coin wallet.
            coinWallet = walletRepository.findByUserAndCurrency(user, coinCurrency)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, coinCurrency + " wallet not found"));
        }

        // Retrieve the latest aggregated price for the symbol.
        AggregatedPrice aggregatedPrice = aggregatedPriceRepository.findTopBySymbolOrderByCreatedAtDesc(symbol)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No aggregated price available for symbol " + symbol));

        // Determine executed price based on order type.
        BigDecimal executedPrice = (orderType == TradeType.BUY) ? aggregatedPrice.getAskPrice() : aggregatedPrice.getBidPrice();
        BigDecimal tradeAmount = tradeRequest.getQuantity().multiply(executedPrice);

        // Update wallet balances.
        if (orderType == TradeType.BUY) {
            // For BUY, subtract cost from USDT wallet and add crypto quantity to coin wallet.
            if (usdtWallet.getBalance().compareTo(tradeAmount) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient USDT funds");
            }
            usdtWallet.setBalance(usdtWallet.getBalance().subtract(tradeAmount));
            coinWallet.setBalance(coinWallet.getBalance().add(tradeRequest.getQuantity()));
        } else { // SELL
            // For SELL, subtract crypto quantity from coin wallet and add revenue to USDT wallet.
            if (coinWallet.getBalance().compareTo(tradeRequest.getQuantity()) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient " + coinCurrency + " funds");
            }
            coinWallet.setBalance(coinWallet.getBalance().subtract(tradeRequest.getQuantity()));
            usdtWallet.setBalance(usdtWallet.getBalance().add(tradeAmount));
        }

        LocalDateTime now = LocalDateTime.now();
        usdtWallet.setUpdatedAt(now);
        coinWallet.setUpdatedAt(now);
        walletRepository.save(usdtWallet);
        walletRepository.save(coinWallet);

        TradeTransaction transaction = TradeTransaction.builder()
                .user(user)
                .symbol(symbol)
                .tradeType(orderType)
                .tradePrice(executedPrice)
                .quantity(tradeRequest.getQuantity())
                .tradeTime(LocalDateTime.now())
                .build();
        TradeTransaction savedTransaction = tradeTransactionRepository.save(transaction);

        return TradeResponse.builder()
                .tradeId(savedTransaction.getId())
                .symbol(symbol)
                .orderType(orderType.toString())
                .executedPrice(executedPrice)
                .quantity(tradeRequest.getQuantity())
                .tradeTime(savedTransaction.getTradeTime())
                .message("Trade executed successfully")
                .build();
    }

    private TradeType parseOrderType(String orderTypeStr) {
        try {
            return TradeType.valueOf(orderTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order type: " + orderTypeStr);
        }
    }
}


