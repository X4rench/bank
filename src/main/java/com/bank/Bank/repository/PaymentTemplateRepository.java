package com.bank.Bank.repository;

import com.bank.Bank.model.PaymentTemplate;
import com.bank.Bank.model.enums.PaymentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTemplateRepository extends JpaRepository<PaymentTemplate, Long> {
    List<PaymentTemplate> findByAccountUserId(Long userId);
    List<PaymentTemplate> findByAccountUserIdAndIsRecurring(Long userId, Boolean isRecurring);
    List<PaymentTemplate> findByAccountUserIdAndCategory(Long userId, PaymentCategory category);
}


