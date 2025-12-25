package com.bank.Bank.service;

import com.bank.Bank.model.Account;
import com.bank.Bank.model.User;
import com.bank.Bank.model.enums.AccountType;
import com.bank.Bank.model.enums.CurrencyType;
import com.bank.Bank.repository.AccountRepository;
import com.bank.Bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Transactional
    public Account createAccount(Long userId, AccountType accountType, CurrencyType currency, BigDecimal initialBalance) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(initialBalance != null ? initialBalance : BigDecimal.ZERO);
        account.setAccountType(accountType);
        account.setCurrency(currency);
        account.setUser(user);
        account.setIsActive(true);
        account.setCreatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    @Transactional
    public Account updateAccount(Long id, Account accountDetails) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (accountDetails.getAccountType() != null) {
            account.setAccountType(accountDetails.getAccountType());
        }
        if (accountDetails.getCurrency() != null) {
            account.setCurrency(accountDetails.getCurrency());
        }
        if (accountDetails.getIsActive() != null) {
            account.setIsActive(accountDetails.getIsActive());
        }

        return accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        accountRepository.delete(account);
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }
}

