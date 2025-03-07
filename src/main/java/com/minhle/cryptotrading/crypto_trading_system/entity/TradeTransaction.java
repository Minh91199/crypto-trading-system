package com.minhle.cryptotrading.crypto_trading_system.entity;

import com.minhle.cryptotrading.crypto_trading_system.enums.TradeType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trade_transaction")
public class TradeTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private CryptoUser user;

    @Column(name = "symbol", nullable = false, length = 10)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false, length = 4)
    private TradeType tradeType;

    @Column(name = "trade_price", nullable = false, precision = 18, scale = 8)
    private BigDecimal tradePrice;

    @Column(name = "quantity", nullable = false, precision = 18, scale = 8)
    private BigDecimal quantity;

    @Column(name = "trade_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime tradeTime = LocalDateTime.now();

    @Column(name = "wallet_before", precision = 18, scale = 8)
    private BigDecimal walletBefore;

    @Column(name = "wallet_after", precision = 18, scale = 8)
    private BigDecimal walletAfter;
}

