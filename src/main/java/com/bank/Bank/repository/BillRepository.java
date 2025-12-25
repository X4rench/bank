package com.bank.Bank.repository;

import com.bank.Bank.model.Bill;
import com.bank.Bank.model.enums.PaymentCategory;
import com.bank.Bank.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByAccountUserId(Long userId);
    List<Bill> findByAccountUserIdAndStatus(Long userId, PaymentStatus status);
    List<Bill> findByAccountUserIdAndCategory(Long userId, PaymentCategory category);
    Optional<Bill> findByBillNumber(String billNumber);
}


