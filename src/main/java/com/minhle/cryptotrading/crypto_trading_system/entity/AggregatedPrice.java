package com.minhle.cryptotrading.crypto_trading_system.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aggregated_price")
public class AggregatedPrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "symbol", nullable = false, length = 10)
  private String symbol;

  @Column(name = "bid_price", nullable = false, precision = 18, scale = 8)
  private BigDecimal bidPrice;

  @Column(name = "ask_price", nullable = false, precision = 18, scale = 8)
  private BigDecimal askPrice;

  @Column(
      name = "created_at",
      nullable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime createdAt;
}
