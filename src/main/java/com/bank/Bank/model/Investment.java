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
@Table(name = "investments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Investment number is required")
    @Size(min = 10, max = 20, message = "Investment number must be between 10 and 20 characters")
    @Column(name = "investment_number", nullable = false, unique = true, length = 20)
    private String investmentNumber;

    @NotBlank(message = "Instrument name is required")
    @Size(max = 100, message = "Instrument name must be less than 100 characters")
    @Column(name = "instrument_name", nullable = false, length = 100)
    private String instrumentName;

    @NotBlank(message = "Instrument type is required")
    @Size(max = 50, message = "Instrument type must be less than 50 characters")
    @Column(name = "instrument_type", nullable = false, length = 50)
    private String instrumentType; // STOCK, BOND, ETF, etc.

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @NotNull(message = "Purchase price is required")
    @Positive(message = "Purchase price must be positive")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "purchase_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    @NotNull(message = "Current price is required")
    @Positive(message = "Current price must be positive")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "current_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentPrice;

    @NotNull(message = "Total value is required")
    @DecimalMin(value = "0.0")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "total_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyType currency;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    @JsonIgnore
    private User user;
}

