package com.bank.Bank.model;

import com.bank.Bank.model.enums.PaymentCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Template name is required")
    @Size(max = 100, message = "Template name must be less than 100 characters")
    @Column(name = "template_name", nullable = false, length = 100)
    private String templateName;

    @NotBlank(message = "Provider name is required")
    @Size(max = 100, message = "Provider name must be less than 100 characters")
    @Column(name = "provider_name", nullable = false, length = 100)
    private String providerName;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private PaymentCategory category;

    @Column(name = "account_number", length = 50)
    private String accountNumber;

    @Column(name = "payment_code", length = 50)
    private String paymentCode;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_recurring", nullable = false)
    private Boolean isRecurring = false;

    @Column(name = "recurring_day")
    private Integer recurringDay;

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

