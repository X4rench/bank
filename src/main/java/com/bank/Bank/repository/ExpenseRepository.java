package com.bank.Bank.repository;

import com.bank.Bank.model.Expense;
import com.bank.Bank.model.enums.PaymentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByAccountUserId(Long userId);
    List<Expense> findByAccountUserIdAndCategory(Long userId, PaymentCategory category);
    List<Expense> findByAccountUserIdAndExpenseDateBetween(Long userId, LocalDate start, LocalDate end);
}


