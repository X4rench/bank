package com.bank.Bank.model;

import com.bank.Bank.model.enums.PaymentCategory;
import com.bank.Bank.model.enums.PaymentStatus;
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
@Table(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Bill number is required")
    @Size(min = 10, max = 20, message = "Bill number must be between 10 and 20 characters")
    @Column(name = "bill_number", nullable = false, unique = true, length = 20)
    private String billNumber;

    @NotBlank(message = "Provider name is required")
    @Size(max = 100, message = "Provider name must be less than 100 characters")
    @Column(name = "provider_name", nullable = false, length = 100)
    private String providerName;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private PaymentCategory category;

    @Column(name = "account_number", length = 50)
    private String accountNumber;

    @Column(name = "payment_code", length = 50)
    private String paymentCode;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_auto_payment", nullable = false)
    private Boolean isAutoPayment = false;

    @Column(name = "auto_payment_date")
    private LocalDate autoPaymentDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @NotNull(message = "Account is required")
    @JsonIgnore
    private Account account;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

