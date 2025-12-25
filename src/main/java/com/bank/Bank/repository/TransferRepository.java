package com.bank.Bank.repository;

import com.bank.Bank.model.Transfer;
import com.bank.Bank.model.enums.PaymentStatus;
import com.bank.Bank.model.enums.TransferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByFromAccountUserId(Long userId);
    List<Transfer> findByFromAccountUserIdAndStatus(Long userId, PaymentStatus status);
    List<Transfer> findByFromAccountUserIdAndTransferType(Long userId, TransferType transferType);
    List<Transfer> findByFromAccountUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}


