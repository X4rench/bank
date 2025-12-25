package com.bank.Bank.model;

import com.bank.Bank.model.enums.CurrencyType;
import com.bank.Bank.model.enums.LoanStatus;
import com.bank.Bank.model.enums.LoanType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Loan number is required")
    @Size(min = 10, max = 20, message = "Loan number must be between 10 and 20 characters")
    @Column(name = "loan_number", nullable = false, unique = true, length = 20)
    private String loanNumber;

    @NotNull(message = "Loan type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type", nullable = false)
    private LoanType loanType;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LoanStatus status;

    @NotNull(message = "Principal amount is required")
    @Positive(message = "Principal amount must be positive")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "principal_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal principalAmount;

    @NotNull(message = "Interest rate is required")
    @Positive(message = "Interest rate must be positive")
    @Digits(integer = 3, fraction = 2)
    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @NotNull(message = "Remaining balance is required")
    @DecimalMin(value = "0.0")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "remaining_balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal remainingBalance;

    @NotNull(message = "Monthly payment is required")
    @Positive(message = "Monthly payment must be positive")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "monthly_payment", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyPayment;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "next_payment_date")
    private LocalDate nextPaymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyType currency;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    @JsonIgnore
    private User user;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

