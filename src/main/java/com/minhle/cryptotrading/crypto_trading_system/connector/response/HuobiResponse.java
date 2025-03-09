package com.minhle.cryptotrading.crypto_trading_system.connector.response;

import com.minhle.cryptotrading.crypto_trading_system.connector.dto.HuobiTicker;
import java.util.List;
import lombok.Data;

@Data
public class HuobiResponse {
  private String status;
  private Long ts;
  private List<HuobiTicker> data;
}
