package com.bank.Bank.repository;

import com.bank.Bank.model.Budget;
import com.bank.Bank.model.enums.PaymentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserId(Long userId);
    List<Budget> findByUserIdAndIsActive(Long userId, Boolean isActive);
    List<Budget> findByUserIdAndCategory(Long userId, PaymentCategory category);
    List<Budget> findByUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long userId, LocalDate date1, LocalDate date2);
}


