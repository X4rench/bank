package com.bank.Bank.repository;

import com.bank.Bank.model.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    List<Insurance> findByUserId(Long userId);
    List<Insurance> findByUserIdAndIsActive(Long userId, Boolean isActive);
    Optional<Insurance> findByPolicyNumber(String policyNumber);
}


