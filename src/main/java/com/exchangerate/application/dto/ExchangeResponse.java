package com.exchangerate.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeResponse {
    private BigDecimal amount;
    private BigDecimal convertedAmount;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal exchangeRate;
}
