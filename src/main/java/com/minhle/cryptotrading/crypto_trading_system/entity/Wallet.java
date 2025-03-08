package com.minhle.cryptotrading.crypto_trading_system.entity;

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
@Table(name = "wallet", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "currency"})})
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private CryptoUser user;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency;

    @Column(name = "balance", nullable = false, precision = 18, scale = 8)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt = LocalDateTime.now();
}

