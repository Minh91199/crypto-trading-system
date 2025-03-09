package com.minhle.cryptotrading.crypto_trading_system.controller;
import com.minhle.cryptotrading.crypto_trading_system.model.response.WalletBalanceResponse;
import com.minhle.cryptotrading.crypto_trading_system.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/balance")
    public ResponseEntity<List<WalletBalanceResponse>> getWalletBalances() {
        List<WalletBalanceResponse> response = walletService.getWalletBalances(1L);
        return ResponseEntity.ok(response);
    }
}

