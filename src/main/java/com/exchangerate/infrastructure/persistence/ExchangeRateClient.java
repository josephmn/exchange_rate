package com.exchangerate.infrastructure.persistence;

import com.exchangerate.application.dto.ExchangeRateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "exchangeRateClient", url = "https://open.er-api.com/v6/latest")
public interface ExchangeRateClient {

    @GetMapping("/{currency}")
    ExchangeRateResponse getExchangeRates(@RequestParam("currency") String currency);

}
