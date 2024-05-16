package com.exchangerate.web.Controller;

import com.exchangerate.application.dto.ExchangeResponse;
import com.exchangerate.application.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @Autowired
    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/exchange")
    public ExchangeResponse getExchange(@RequestParam String amount, @RequestParam String fromCurrency, @RequestParam String toCurrency) {
        return exchangeService.getExchange(amount, fromCurrency, toCurrency);
    }
}
