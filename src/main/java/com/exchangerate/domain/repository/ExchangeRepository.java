package com.exchangerate.domain.repository;

import com.exchangerate.domain.model.Exchange;
import org.springframework.data.repository.CrudRepository;

public interface ExchangeRepository extends CrudRepository<Exchange, Long> {
}
