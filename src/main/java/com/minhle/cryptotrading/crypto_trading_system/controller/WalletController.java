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
    public ResponseEntity<List<WalletBalanceResponse>> getWalletBalances(
            @RequestParam(value = "userId", defaultValue = "1") Long userId) {
        List<WalletBalanceResponse> response = walletService.getWalletBalances(userId);
        return ResponseEntity.ok(response);
    }
}

