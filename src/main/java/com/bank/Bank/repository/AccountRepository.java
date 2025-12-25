package com.bank.Bank.repository;

import com.bank.Bank.model.Account;
import com.bank.Bank.model.enums.AccountType;
import com.bank.Bank.model.enums.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    /**
     * Find all accounts by user ID
     * @param userId user ID
     * @return List of accounts belonging to the user
     */
    List<Account> findByUserId(Long userId);
    
    /**
     * Find account by account number
     * @param accountNumber account number
     * @return Optional Account
     */
    Optional<Account> findByAccountNumber(String accountNumber);
    
    /**
     * Find accounts by user ID and account type
     * @param userId user ID
     * @param accountType account type
     * @return List of accounts
     */
    List<Account> findByUserIdAndAccountType(Long userId, AccountType accountType);
    
    /**
     * Find accounts by user ID and currency
     * @param userId user ID
     * @param currency currency
     * @return List of accounts
     */
    List<Account> findByUserIdAndCurrency(Long userId, CurrencyType currency);
}

