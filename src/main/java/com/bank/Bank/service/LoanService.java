package com.bank.Bank.service;

import com.bank.Bank.model.Account;
import com.bank.Bank.model.Loan;
import com.bank.Bank.model.User;
import com.bank.Bank.model.enums.CurrencyType;
import com.bank.Bank.model.enums.LoanStatus;
import com.bank.Bank.model.enums.LoanType;
import com.bank.Bank.repository.AccountRepository;
import com.bank.Bank.repository.LoanRepository;
import com.bank.Bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Loan> getLoansByUserId(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    @Transactional
    public Loan createLoan(Long userId, LoanType loanType, BigDecimal principalAmount,
                          BigDecimal interestRate, int termMonths, CurrencyType currency) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Calculate monthly payment using simple interest formula
        BigDecimal monthlyRate = interestRate.divide(new BigDecimal("1200"), 4, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = principalAmount.multiply(monthlyRate)
                .divide(BigDecimal.ONE.subtract(BigDecimal.ONE.divide(
                        BigDecimal.ONE.add(monthlyRate).pow(termMonths), 4, RoundingMode.HALF_UP)), 2, RoundingMode.HALF_UP);

        Loan loan = new Loan();
        loan.setLoanNumber(generateLoanNumber());
        loan.setLoanType(loanType);
        loan.setStatus(LoanStatus.APPROVED);
        loan.setPrincipalAmount(principalAmount);
        loan.setInterestRate(interestRate);
        loan.setRemainingBalance(principalAmount);
        loan.setMonthlyPayment(monthlyPayment);
        loan.setStartDate(LocalDate.now());
        loan.setEndDate(LocalDate.now().plusMonths(termMonths));
        loan.setNextPaymentDate(LocalDate.now().plusMonths(1));
        loan.setCurrency(currency);
        loan.setCreatedAt(LocalDateTime.now());
        loan.setUser(user);

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan makePayment(Long loanId, BigDecimal amount) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() == LoanStatus.PAID_OFF) {
            throw new RuntimeException("Кредит уже полностью погашен");
        }

        // Ограничиваем сумму платежа остатком долга
        if (amount.compareTo(loan.getRemainingBalance()) > 0) {
            amount = loan.getRemainingBalance();
        }

        // Найти счет пользователя с нужной валютой для списания средств
        List<Account> userAccounts = accountRepository.findByUserIdAndCurrency(
                loan.getUser().getId(), 
                loan.getCurrency()
        );
        
        if (userAccounts.isEmpty()) {
            throw new RuntimeException("Не найден счет пользователя с валютой " + loan.getCurrency());
        }

        // Используем первый активный счет, если есть
        Account sourceAccount = userAccounts.stream()
                .filter(Account::getIsActive)
                .findFirst()
                .orElse(userAccounts.get(0));

        // Проверка баланса
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Недостаточно средств на счете для погашения кредита");
        }

        // Списание средств со счета
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        accountRepository.save(sourceAccount);

        // Уменьшение остатка долга
        loan.setRemainingBalance(loan.getRemainingBalance().subtract(amount));

        // Проверка полного погашения
        if (loan.getRemainingBalance().compareTo(BigDecimal.ZERO) <= 0) {
            loan.setStatus(LoanStatus.PAID_OFF);
            loan.setRemainingBalance(BigDecimal.ZERO);
        } else {
            loan.setNextPaymentDate(loan.getNextPaymentDate().plusMonths(1));
        }

        return loanRepository.save(loan);
    }

    private String generateLoanNumber() {
        return "LOAN" + System.currentTimeMillis();
    }
}

