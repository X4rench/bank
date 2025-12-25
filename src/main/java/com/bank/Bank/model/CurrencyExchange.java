package com.bank.Bank.model;

import com.bank.Bank.model.enums.CurrencyType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "currency_exchanges")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "From currency is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "from_currency", nullable = false)
    private CurrencyType fromCurrency;

    @NotNull(message = "To currency is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "to_currency", nullable = false)
    private CurrencyType toCurrency;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "Exchange rate is required")
    @Positive(message = "Exchange rate must be positive")
    @Digits(integer = 10, fraction = 6)
    @Column(name = "exchange_rate", nullable = false, precision = 16, scale = 6)
    private BigDecimal exchangeRate;

    @NotNull(message = "Converted amount is required")
    @Positive(message = "Converted amount must be positive")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "converted_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal convertedAmount;

    @Column(name = "fee", precision = 12, scale = 2)
    private BigDecimal fee = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", nullable = false)
    @NotNull(message = "From account is required")
    @JsonIgnore
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id", nullable = false)
    @NotNull(message = "To account is required")
    @JsonIgnore
    private Account toAccount;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

