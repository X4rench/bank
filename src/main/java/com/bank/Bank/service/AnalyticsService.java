package com.bank.Bank.service;

import com.bank.Bank.model.Expense;
import com.bank.Bank.model.Transaction;
import com.bank.Bank.model.enums.PaymentCategory;
import com.bank.Bank.repository.ExpenseRepository;
import com.bank.Bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Transactional(readOnly = true)
    public Map<PaymentCategory, BigDecimal> getExpensesByCategory(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByAccountUserIdAndExpenseDateBetween(userId, startDate, endDate);

        Map<PaymentCategory, BigDecimal> categoryMap = new HashMap<>();
        for (Expense expense : expenses) {
            categoryMap.merge(expense.getCategory(), expense.getAmount(), BigDecimal::add);
        }

        return categoryMap;
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalIncome(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findByAccountUserId(userId);
        return transactions.stream()
                .filter(t -> t.getType().name().equals("CREDIT"))
                .filter(t -> !t.getTimestamp().toLocalDate().isBefore(startDate) &&
                            !t.getTimestamp().toLocalDate().isAfter(endDate))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalExpenses(Long userId, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByAccountUserIdAndExpenseDateBetween(userId, startDate, endDate).stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

