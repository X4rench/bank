package com.bank.Bank.repository;

import com.bank.Bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    /**
     * Find all transactions by account ID
     * @param accountId account ID
     * @return List of transactions for the account
     */
    List<Transaction> findByAccountId(Long accountId);
    
    /**
     * Find all transactions by user ID through account
     * @param userId user ID
     * @return List of transactions for the user
     */
    List<Transaction> findByAccountUserId(Long userId);
    
    /**
     * Find transactions by user ID and date range
     * @param userId user ID
     * @param start start date
     * @param end end date
     * @return List of transactions
     */
    List<Transaction> findByAccountUserIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);
}

