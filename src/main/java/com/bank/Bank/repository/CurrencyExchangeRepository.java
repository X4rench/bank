package com.bank.Bank.repository;

import com.bank.Bank.model.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long> {
    List<CurrencyExchange> findByFromAccountUserId(Long userId);
    List<CurrencyExchange> findByFromAccountUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}


