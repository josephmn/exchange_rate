package com.exchangerate.domain.repository;

import com.exchangerate.domain.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
}
