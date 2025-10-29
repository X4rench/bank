package com.bank.Bank.repository;

import com.bank.Bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    /**
     * Find all transactions by account ID
     * @param accountId account ID
     * @return List of transactions for the account
     */
    List<Transaction> findByAccountId(Long accountId);
}

