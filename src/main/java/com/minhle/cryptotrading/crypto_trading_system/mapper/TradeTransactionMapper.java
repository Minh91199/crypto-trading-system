package com.minhle.cryptotrading.crypto_trading_system.mapper;

import com.minhle.cryptotrading.crypto_trading_system.entity.TradeTransaction;
import com.minhle.cryptotrading.crypto_trading_system.model.response.TradeHistoryResponse;
import com.minhle.cryptotrading.crypto_trading_system.model.response.TradeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TradeTransactionMapper {
  @Mapping(target = "message", expression = "java(message)")
  @Mapping(target = "tradeId", source = "tradeTransaction.id")
  @Mapping(target = "symbol", source = "tradeTransaction.symbol")
  @Mapping(target = "orderType", source = "tradeTransaction.tradeType")
  @Mapping(target = "executedPrice", source = "tradeTransaction.tradePrice")
  @Mapping(target = "quantity", source = "tradeTransaction.quantity")
  @Mapping(target = "tradeTime", source = "tradeTransaction.tradeTime")
  TradeResponse toTradeResponse(TradeTransaction tradeTransaction, String message);

  @Mapping(target = "tradeId", source = "id")
  @Mapping(target = "symbol", source = "symbol")
  @Mapping(target = "tradeType", source = "tradeType") // Enum -> String
  @Mapping(target = "tradePrice", source = "tradePrice")
  @Mapping(target = "quantity", source = "quantity")
  @Mapping(target = "tradeTime", source = "tradeTime")
  TradeHistoryResponse toTradeHistoryResponse(TradeTransaction tx);
}
