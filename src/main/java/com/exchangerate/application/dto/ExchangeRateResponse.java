package com.exchangerate.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {
    private String result;
    private String base_code;
    private Map<String, Double> rates;
}
