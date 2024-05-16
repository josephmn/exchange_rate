package com.exchangerate.application.service;

import com.exchangerate.application.dto.ExchangeRateResponse;
import com.exchangerate.application.dto.ExchangeResponse;
import com.exchangerate.application.dto.ExchangeSaveResponse;
import com.exchangerate.domain.model.Exchange;
import com.exchangerate.domain.repository.ExchangeRepository;
import com.exchangerate.infrastructure.persistence.ExchangeRateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeService {

    private final ExchangeRateClient exchangeRateClient;
    private final ExchangeRepository exchangeRepository;

    @Autowired
    public ExchangeService(ExchangeRateClient exchangeRateClient, ExchangeRepository exchangeRepository) {
        this.exchangeRateClient = exchangeRateClient;
        this.exchangeRepository = exchangeRepository;
    }

    public List<ExchangeSaveResponse> getExchangeSave() {
        List<Exchange> exchanges = (List<Exchange>) this.exchangeRepository.findAll();

        return exchanges.stream()
                .map(exchange-> new ExchangeSaveResponse(
                        exchange.getId(),
                        exchange.getAmount(),
                        exchange.getFromCurrency(),
                        exchange.getToCurrency(),
                        exchange.getExchangeRate(),
                        exchange.getConvertedAmount(),
                        exchange.getLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                ))
                .toList();
    }

    public ExchangeResponse getExchange(String amount, String fromCurrency, String toCurrency) {
        ExchangeRateResponse exchangeRateResponse = exchangeRateClient.getExchangeRates(fromCurrency);
        Double rate = exchangeRateResponse.getRates().get(toCurrency);

        BigDecimal amountBigDecimal = new BigDecimal(amount).setScale(6, RoundingMode.HALF_UP);
        BigDecimal rateBigDecimal = new BigDecimal(rate).setScale(6, RoundingMode.HALF_UP);
        BigDecimal convertedAmount = amountBigDecimal.multiply(rateBigDecimal).setScale(4, RoundingMode.HALF_UP);

        Exchange exchange = new Exchange();
        exchange.setAmount(amountBigDecimal);
        exchange.setFromCurrency(fromCurrency);
        exchange.setToCurrency(toCurrency);
        exchange.setExchangeRate(rateBigDecimal);
        exchange.setConvertedAmount(convertedAmount);

        exchangeRepository.save(exchange);

        return new ExchangeResponse(amountBigDecimal, convertedAmount, fromCurrency, toCurrency, rateBigDecimal);
    }
}
