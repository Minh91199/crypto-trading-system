package com.minhle.cryptotrading.crypto_trading_system.mapper;
import com.minhle.cryptotrading.crypto_trading_system.entity.Wallet;
import com.minhle.cryptotrading.crypto_trading_system.model.response.WalletBalanceResponse;
import org.mapstruct.Mapper;

/**
 * A simple mapper from Wallet entity to WalletBalanceResponse DTO.
 */
@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletBalanceResponse toResponse(Wallet wallet);
}

