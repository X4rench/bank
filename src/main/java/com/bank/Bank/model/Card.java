package com.bank.Bank.model;

import com.bank.Bank.model.enums.CardStatus;
import com.bank.Bank.model.enums.CardType;
import com.bank.Bank.model.enums.CurrencyType;
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
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 19, message = "Card number must be between 16 and 19 characters")
    @Column(name = "card_number", nullable = false, unique = true, length = 19)
    private String cardNumber;

    @NotBlank(message = "Cardholder name is required")
    @Size(max = 100, message = "Cardholder name must be less than 100 characters")
    @Column(name = "cardholder_name", nullable = false, length = 100)
    private String cardholderName;

    @NotNull(message = "Expiry date is required")
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Size(min = 3, max = 4, message = "CVV must be 3 or 4 characters")
    @Column(name = "cvv", length = 4)
    private String cvv;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyType currency;

    @DecimalMin(value = "0.0", message = "Balance cannot be negative")
    @Column(name = "balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Credit limit cannot be negative")
    @Column(name = "credit_limit", precision = 12, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "is_virtual", nullable = false)
    private Boolean isVirtual = false;

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
        if (status == null) {
            status = CardStatus.PENDING;
        }
    }
}

