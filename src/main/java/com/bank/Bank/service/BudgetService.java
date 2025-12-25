package com.bank.Bank.service;

import com.bank.Bank.model.Budget;
import com.bank.Bank.model.User;
import com.bank.Bank.model.enums.PaymentCategory;
import com.bank.Bank.repository.BudgetRepository;
import com.bank.Bank.repository.ExpenseRepository;
import com.bank.Bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Budget> getBudgetsByUserId(Long userId) {
        return budgetRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Budget> getActiveBudgetsByUserId(Long userId) {
        return budgetRepository.findByUserIdAndIsActive(userId, true);
    }

    @Transactional
    public Budget createBudget(Long userId, PaymentCategory category, BigDecimal limitAmount,
                              LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = new Budget();
        budget.setCategory(category);
        budget.setLimitAmount(limitAmount);
        budget.setStartDate(startDate);
        budget.setEndDate(endDate);
        budget.setSpentAmount(BigDecimal.ZERO);
        budget.setIsActive(true);
        budget.setUser(user);

        return budgetRepository.save(budget);
    }

    @Transactional
    public Budget updateSpentAmount(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        BigDecimal spent = expenseRepository.findByAccountUserId(budget.getUser().getId()).stream()
                .filter(e -> e.getCategory() == budget.getCategory())
                .filter(e -> !e.getExpenseDate().isBefore(budget.getStartDate()) &&
                            !e.getExpenseDate().isAfter(budget.getEndDate()))
                .map(e -> e.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        budget.setSpentAmount(spent);
        return budgetRepository.save(budget);
    }

    @Transactional
    public void deleteBudget(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        budgetRepository.delete(budget);
    }
}

