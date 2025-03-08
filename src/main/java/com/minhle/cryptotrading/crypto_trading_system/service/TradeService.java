package com.minhle.cryptotrading.crypto_trading_system.service;

import com.minhle.cryptotrading.crypto_trading_system.model.request.TradeRequest;
import com.minhle.cryptotrading.crypto_trading_system.model.response.TradeResponse;

public interface TradeService {
    TradeResponse executeTrade(TradeRequest tradeRequest);
}
