package com.minhle.cryptotrading.crypto_trading_system.service;

import com.minhle.cryptotrading.crypto_trading_system.constant.CurrencyConstant;
import com.minhle.cryptotrading.crypto_trading_system.entity.AggregatedPrice;
import com.minhle.cryptotrading.crypto_trading_system.entity.CryptoUser;
import com.minhle.cryptotrading.crypto_trading_system.entity.TradeTransaction;
import com.minhle.cryptotrading.crypto_trading_system.entity.Wallet;
import com.minhle.cryptotrading.crypto_trading_system.enums.TradeType;
import com.minhle.cryptotrading.crypto_trading_system.mapper.TradeTransactionMapper;
import com.minhle.cryptotrading.crypto_trading_system.model.request.TradeRequest;
import com.minhle.cryptotrading.crypto_trading_system.model.response.TradeHistoryResponse;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final TradeTransactionRepository tradeTransactionRepository;
    private final AggregatedPriceRepository aggregatedPriceRepository;
    private final CryptoUserRepository cryptoUserRepository;
    private final WalletRepository walletRepository;
    private final TradeTransactionMapper tradeTransactionMapper;

    @Value("#{'${crypto-trading-system.config.trading.supported-symbols}'.split(',')}")
    private List<String> supportedSymbols;

    @Override
    @Transactional
    public TradeResponse executeTrade(TradeRequest tradeRequest) {
        String symbol = tradeRequest.getSymbol().toUpperCase();
        validateSymbol(symbol);

        // Validate the trade quantity for specific symbols (BTC or ETH)
        validateQuantity(symbol, tradeRequest.getQuantity());

        // fixed user with id = 1
        CryptoUser user = getUser(1L);

        // Prepare wallets
        Wallet usdtWallet = getUsdtWallet(user);
        String coinCurrency = extractCoinCurrency(symbol);
        TradeType orderType = parseOrderType(tradeRequest.getOrderType());
        Wallet coinWallet = (orderType == TradeType.BUY)
                ? getOrCreateCoinWallet(user, coinCurrency)
                : getCoinWallet(user, coinCurrency);

        // Retrieve the best aggregated price
        AggregatedPrice aggregatedPrice = getLatestAggregatedPrice(symbol);

        // Calculate executedPrice and tradeAmount
        BigDecimal executedPrice = (orderType == TradeType.BUY)
                ? aggregatedPrice.getAskPrice()
                : aggregatedPrice.getBidPrice();
        BigDecimal tradeAmount = tradeRequest.getQuantity().multiply(executedPrice);

        // Update wallets
        updateWalletBalances(usdtWallet, coinWallet, orderType, tradeRequest.getQuantity(), tradeAmount, coinCurrency);

        // Create and save trade transaction
        TradeTransaction savedTransaction = createAndSaveTransaction(user, symbol, orderType, executedPrice, tradeRequest.getQuantity());

        return tradeTransactionMapper.toTradeResponse(savedTransaction, "Trade executed successfully");
    }

    @Override
    public List<TradeHistoryResponse> getTradeHistory(Long userId) {
        CryptoUser user = cryptoUserRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User not found"));

        List<TradeTransaction> transactions = tradeTransactionRepository.findByUserOrderByTradeTimeDesc(user);

        return transactions.stream()
                .map(tradeTransactionMapper::toTradeHistoryResponse)
                .collect(Collectors.toList());
    }

    private void validateSymbol(String symbol) {
        if (!supportedSymbols.contains(symbol)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Symbol " + symbol + " is not supported");
        }
    }

    private CryptoUser getUser(Long userId) {
        return cryptoUserRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
    }

    private Wallet getUsdtWallet(CryptoUser user) {
        return walletRepository.findByUserAndCurrency(user, CurrencyConstant.STABLE_COIN)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, CurrencyConstant.STABLE_COIN + " wallet not found"));
    }

    private String extractCoinCurrency(String symbol) {
        return symbol.replace(CurrencyConstant.STABLE_COIN, "");
    }

    private Wallet getOrCreateCoinWallet(CryptoUser user, String coinCurrency) {
        return walletRepository.findByUserAndCurrency(user, coinCurrency)
                .orElseGet(() -> walletRepository.save(
                        Wallet.builder()
                                .user(user)
                                .currency(coinCurrency)
                                .balance(BigDecimal.ZERO)
                                .updatedAt(LocalDateTime.now())
                                .build()
                ));
    }

    private Wallet getCoinWallet(CryptoUser user, String coinCurrency) {
        return walletRepository.findByUserAndCurrency(user, coinCurrency)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient " + coinCurrency + " funds"));
    }

    private AggregatedPrice getLatestAggregatedPrice(String symbol) {
        return aggregatedPriceRepository.findTopBySymbolOrderByCreatedAtDesc(symbol)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No aggregated price available for symbol " + symbol));
    }

    private void updateWalletBalances(Wallet usdtWallet, Wallet coinWallet,
                                      TradeType orderType, BigDecimal quantity,
                                      BigDecimal tradeAmount, String coinCurrency) {
        if (orderType == TradeType.BUY) {
            if (usdtWallet.getBalance().compareTo(tradeAmount) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient "+ CurrencyConstant.STABLE_COIN + " funds");
            }
            usdtWallet.setBalance(usdtWallet.getBalance().subtract(tradeAmount));
            coinWallet.setBalance(coinWallet.getBalance().add(quantity));
        } else {
            if (coinWallet.getBalance().compareTo(quantity) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient " + coinCurrency + " funds");
            }
            coinWallet.setBalance(coinWallet.getBalance().subtract(quantity));
            usdtWallet.setBalance(usdtWallet.getBalance().add(tradeAmount));
        }
        LocalDateTime now = LocalDateTime.now();
        usdtWallet.setUpdatedAt(now);
        coinWallet.setUpdatedAt(now);
        walletRepository.save(usdtWallet);
        walletRepository.save(coinWallet);
    }

    private TradeTransaction createAndSaveTransaction(CryptoUser user, String symbol, TradeType orderType,
                                                      BigDecimal executedPrice, BigDecimal quantity) {
        TradeTransaction transaction = TradeTransaction.builder()
                .user(user)
                .symbol(symbol)
                .tradeType(orderType)
                .tradePrice(executedPrice)
                .quantity(quantity)
                .tradeTime(LocalDateTime.now())
                .build();
        return tradeTransactionRepository.save(transaction);
    }

    private TradeType parseOrderType(String orderTypeStr) {
        try {
            return TradeType.valueOf(orderTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid order type: " + orderTypeStr);
        }
    }

    private void validateQuantity(String symbol, BigDecimal quantity) {
        String coin = symbol.replace(CurrencyConstant.STABLE_COIN, "").toUpperCase();

        BigDecimal minBtcQty = new BigDecimal("0.00001");
        BigDecimal minEthQty = new BigDecimal("0.005");

        if ("BTC".equals(coin) && quantity.compareTo(minBtcQty) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Minimum trade quantity for BTC is " + minBtcQty);
        }
        if ("ETH".equals(coin) && quantity.compareTo(minEthQty) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Minimum trade quantity for ETH is " + minEthQty);
        }
    }

}


