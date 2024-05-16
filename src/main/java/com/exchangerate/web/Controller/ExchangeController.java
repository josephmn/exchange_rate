package com.exchangerate.web.Controller;

import com.exchangerate.application.dto.ExchangeResponse;
import com.exchangerate.application.dto.ExchangeSaveResponse;
import com.exchangerate.application.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @Autowired
    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/listexchangesave")
    public ResponseEntity<List<ExchangeSaveResponse>> getExchangeSave() {
        List<ExchangeSaveResponse> responseList = this.exchangeService.getExchangeSave();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/exchange")
    public ResponseEntity<ExchangeResponse> getExchange(@RequestParam String amount, @RequestParam String fromCurrency, @RequestParam String toCurrency) {
        ExchangeResponse exchangeResponse = this.exchangeService.getExchange(amount, fromCurrency, toCurrency);
        return ResponseEntity.ok(exchangeResponse);
    }
}
