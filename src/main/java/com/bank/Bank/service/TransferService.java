package com.bank.Bank.service;

import com.bank.Bank.model.Account;
import com.bank.Bank.model.Transfer;
import com.bank.Bank.model.enums.PaymentStatus;
import com.bank.Bank.model.enums.TransferType;
import com.bank.Bank.repository.TransferRepository;
import com.bank.Bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Transfer> getTransfersByUserId(Long userId) {
        return transferRepository.findByFromAccountUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Transfer> searchTransfers(Long userId, String searchTerm, TransferType transferType, 
                                         LocalDate startDate, LocalDate endDate, String sortBy, String sortOrder) {
        List<Transfer> transfers = transferRepository.findByFromAccountUserId(userId);
        
        // Фильтрация по поисковому запросу
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            String lowerSearch = searchTerm.toLowerCase();
            transfers = transfers.stream()
                .filter(t -> 
                    (t.getDescription() != null && t.getDescription().toLowerCase().contains(lowerSearch)) ||
                    (t.getAmount() != null && t.getAmount().toString().contains(lowerSearch)) ||
                    (t.getTransferType() != null && t.getTransferType().name().toLowerCase().contains(lowerSearch)) ||
                    (t.getTransferNumber() != null && t.getTransferNumber().contains(lowerSearch))
                )
                .collect(Collectors.toList());
        }
        
        // Фильтрация по типу
        if (transferType != null) {
            transfers = transfers.stream()
                .filter(t -> t.getTransferType() == transferType)
                .collect(Collectors.toList());
        }
        
        // Фильтрация по дате
        if (startDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            transfers = transfers.stream()
                .filter(t -> t.getCreatedAt() != null && !t.getCreatedAt().isBefore(startDateTime))
                .collect(Collectors.toList());
        }
        
        if (endDate != null) {
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            transfers = transfers.stream()
                .filter(t -> t.getCreatedAt() != null && !t.getCreatedAt().isAfter(endDateTime))
                .collect(Collectors.toList());
        }
        
        // Сортировка
        if (sortBy != null && !sortBy.isEmpty()) {
            final String sortField = sortBy;
            final boolean ascending = sortOrder == null || !sortOrder.equalsIgnoreCase("desc");
            
            transfers = transfers.stream()
                .sorted((t1, t2) -> {
                    int result = 0;
                    switch (sortField.toLowerCase()) {
                        case "date":
                            result = t1.getCreatedAt().compareTo(t2.getCreatedAt());
                            break;
                        case "amount":
                            result = t1.getAmount().compareTo(t2.getAmount());
                            break;
                        case "type":
                            result = t1.getTransferType().name().compareTo(t2.getTransferType().name());
                            break;
                        default:
                            result = t1.getCreatedAt().compareTo(t2.getCreatedAt());
                    }
                    return ascending ? result : -result;
                })
                .collect(Collectors.toList());
        } else {
            // Сортировка по умолчанию - по дате (новые сначала)
            transfers = transfers.stream()
                .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
                .collect(Collectors.toList());
        }
        
        return transfers;
    }

    public Optional<Transfer> getTransferById(Long id) {
        return transferRepository.findById(id);
    }

    @Transactional
    public Transfer createTransfer(Long fromAccountId, Long toAccountId, BigDecimal amount, 
                                   TransferType transferType, String description) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("From account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        Account toAccount = null;
        if (toAccountId != null) {
            toAccount = accountRepository.findById(toAccountId)
                    .orElseThrow(() -> new RuntimeException("To account not found"));
        }

        Transfer transfer = new Transfer();
        transfer.setTransferNumber(generateTransferNumber());
        transfer.setAmount(amount);
        transfer.setTransferType(transferType);
        transfer.setStatus(PaymentStatus.PENDING);
        transfer.setCurrency(fromAccount.getCurrency());
        transfer.setDescription(description);
        transfer.setFee(calculateFee(amount, transferType));
        transfer.setCreatedAt(LocalDateTime.now());
        transfer.setFromAccount(fromAccount);
        transfer.setToAccount(toAccount);

        // Process transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount).subtract(transfer.getFee()));
        if (toAccount != null) {
            toAccount.setBalance(toAccount.getBalance().add(amount));
        }

        transfer.setStatus(PaymentStatus.COMPLETED);
        transfer.setCompletedAt(LocalDateTime.now());

        accountRepository.save(fromAccount);
        if (toAccount != null) {
            accountRepository.save(toAccount);
        }

        return transferRepository.save(transfer);
    }

    @Transactional
    public Transfer createExternalTransfer(Long fromAccountId, String recipientAccount, 
                                          String recipientCard, String recipientPhone,
                                          BigDecimal amount, TransferType transferType, String description) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("From account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        Transfer transfer = new Transfer();
        transfer.setTransferNumber(generateTransferNumber());
        transfer.setAmount(amount);
        transfer.setTransferType(transferType);
        transfer.setStatus(PaymentStatus.PENDING);
        transfer.setCurrency(fromAccount.getCurrency());
        transfer.setRecipientAccount(recipientAccount);
        transfer.setRecipientCard(recipientCard);
        transfer.setRecipientPhone(recipientPhone);
        transfer.setDescription(description);
        transfer.setFee(calculateFee(amount, transferType));
        transfer.setCreatedAt(LocalDateTime.now());
        transfer.setFromAccount(fromAccount);

        // Process transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount).subtract(transfer.getFee()));

        transfer.setStatus(PaymentStatus.COMPLETED);
        transfer.setCompletedAt(LocalDateTime.now());

        accountRepository.save(fromAccount);

        return transferRepository.save(transfer);
    }

    private BigDecimal calculateFee(BigDecimal amount, TransferType transferType) {
        if (transferType == TransferType.INTERNAL || transferType == TransferType.SELF) {
            return BigDecimal.ZERO;
        } else if (transferType == TransferType.SBP) {
            return new BigDecimal("0.5");
        } else {
            return amount.multiply(new BigDecimal("0.01")).min(new BigDecimal("100"));
        }
    }

    private String generateTransferNumber() {
        return "TRF" + System.currentTimeMillis();
    }
}
