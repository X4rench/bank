package com.bank.Bank.service;

import com.bank.Bank.model.Account;
import com.bank.Bank.model.Deposit;
import com.bank.Bank.model.User;
import com.bank.Bank.model.enums.CurrencyType;
import com.bank.Bank.model.enums.DepositType;
import com.bank.Bank.repository.AccountRepository;
import com.bank.Bank.repository.DepositRepository;
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
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<Deposit> getAllDeposits() {
        return depositRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Deposit> getDepositsByUserId(Long userId) {
        return depositRepository.findByUserId(userId);
    }

    public Optional<Deposit> getDepositById(Long id) {
        return depositRepository.findById(id);
    }

    @Transactional
    public Deposit createDeposit(Long userId, DepositType depositType, BigDecimal principalAmount,
                                BigDecimal interestRate, int termMonths, Boolean isAutoRenewal,
                                CurrencyType currency) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Найти счет пользователя с нужной валютой для списания средств
        List<Account> userAccounts = accountRepository.findByUserIdAndCurrency(userId, currency);
        if (userAccounts.isEmpty()) {
            throw new RuntimeException("Не найден счет пользователя с валютой " + currency);
        }

        // Используем первый активный счет, если есть
        Account sourceAccount = userAccounts.stream()
                .filter(Account::getIsActive)
                .findFirst()
                .orElse(userAccounts.get(0));

        // Проверка баланса
        if (sourceAccount.getBalance().compareTo(principalAmount) < 0) {
            throw new RuntimeException("Недостаточно средств на счете для открытия вклада");
        }

        // Создание вклада
        Deposit deposit = new Deposit();
        deposit.setDepositNumber(generateDepositNumber());
        deposit.setDepositType(depositType);
        deposit.setPrincipalAmount(principalAmount);
        deposit.setInterestRate(interestRate);
        deposit.setCurrentBalance(principalAmount);
        deposit.setStartDate(LocalDate.now());
        deposit.setEndDate(LocalDate.now().plusMonths(termMonths));
        deposit.setIsAutoRenewal(isAutoRenewal != null ? isAutoRenewal : false);
        deposit.setCurrency(currency);
        deposit.setCreatedAt(LocalDateTime.now());
        deposit.setUser(user);

        // Списание средств со счета
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(principalAmount));
        accountRepository.save(sourceAccount);

        return depositRepository.save(deposit);
    }

    @Transactional
    public Deposit calculateInterest(Long depositId) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new RuntimeException("Deposit not found"));

        // Simple interest calculation
        BigDecimal monthlyInterest = deposit.getCurrentBalance()
                .multiply(deposit.getInterestRate())
                .divide(new BigDecimal("1200"), 2, RoundingMode.HALF_UP);

        deposit.setCurrentBalance(deposit.getCurrentBalance().add(monthlyInterest));

        return depositRepository.save(deposit);
    }

    @Transactional
    public Deposit closeDeposit(Long depositId) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new RuntimeException("Deposit not found"));

        if (deposit.getCurrentBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Вклад уже закрыт");
        }

        // Найти счет пользователя с нужной валютой для зачисления средств
        List<Account> userAccounts = accountRepository.findByUserIdAndCurrency(
                deposit.getUser().getId(), 
                deposit.getCurrency()
        );
        
        if (userAccounts.isEmpty()) {
            throw new RuntimeException("Не найден счет пользователя с валютой " + deposit.getCurrency());
        }

        // Используем первый активный счет, если есть
        Account targetAccount = userAccounts.stream()
                .filter(Account::getIsActive)
                .findFirst()
                .orElse(userAccounts.get(0));

        // Зачисление средств на счет (тело вклада + начисленные проценты)
        BigDecimal amountToReturn = deposit.getCurrentBalance();
        targetAccount.setBalance(targetAccount.getBalance().add(amountToReturn));
        accountRepository.save(targetAccount);

        // Обнуляем баланс вклада (вклад закрыт)
        deposit.setCurrentBalance(BigDecimal.ZERO);

        return depositRepository.save(deposit);
    }

    private String generateDepositNumber() {
        return "DEP" + System.currentTimeMillis();
    }
}

