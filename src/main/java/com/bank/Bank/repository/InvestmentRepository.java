package com.bank.Bank.repository;

import com.bank.Bank.model.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    List<Investment> findByUserId(Long userId);
    Optional<Investment> findByInvestmentNumber(String investmentNumber);
    List<Investment> findByUserIdAndInstrumentType(Long userId, String instrumentType);
}


