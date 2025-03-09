package com.minhle.cryptotrading.crypto_trading_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class CryptoTradingSystemApplication {

  public static void main(String[] args) {
    SpringApplication.run(CryptoTradingSystemApplication.class, args);
  }
}
