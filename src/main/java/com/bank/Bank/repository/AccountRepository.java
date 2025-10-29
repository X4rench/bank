package com.bank.Bank.repository;

import com.bank.Bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    /**
     * Find all accounts by user ID
     * @param userId user ID
     * @return List of accounts belonging to the user
     */
    List<Account> findByUserId(Long userId);
}

