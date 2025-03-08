package com.minhle.cryptotrading.crypto_trading_system.mapper;


import com.minhle.cryptotrading.crypto_trading_system.entity.AggregatedPrice;
import com.minhle.cryptotrading.crypto_trading_system.model.response.LatestAggregatedPrice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LatestAggregatedPriceMapper {
    LatestAggregatedPrice toLatestAggregatedPrice(AggregatedPrice aggregatedPrice);
}

