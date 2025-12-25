package com.bank.Bank.repository;

import com.bank.Bank.model.Loan;
import com.bank.Bank.model.enums.LoanStatus;
import com.bank.Bank.model.enums.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long userId);
    List<Loan> findByUserIdAndStatus(Long userId, LoanStatus status);
    Optional<Loan> findByLoanNumber(String loanNumber);
    List<Loan> findByUserIdAndLoanType(Long userId, LoanType loanType);
}


