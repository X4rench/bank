package com.bank.Bank.service;

import com.bank.Bank.model.Account;
import com.bank.Bank.model.Card;
import com.bank.Bank.model.enums.CardStatus;
import com.bank.Bank.model.enums.CardType;
import com.bank.Bank.repository.CardRepository;
import com.bank.Bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Card> getCardsByUserId(Long userId) {
        return cardRepository.findByAccountUserId(userId);
    }

    public Optional<Card> getCardById(Long id) {
        return cardRepository.findById(id);
    }

    @Transactional
    public Card createCard(Long accountId, CardType cardType, String cardholderName) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Card card = new Card();
        card.setCardNumber(generateCardNumber());
        card.setCardholderName(cardholderName);
        card.setExpiryDate(LocalDate.now().plusYears(3));
        card.setCvv(generateCVV());
        card.setCardType(cardType);
        card.setStatus(CardStatus.ACTIVE);
        card.setCurrency(account.getCurrency());
        card.setBalance(BigDecimal.ZERO);
        card.setIsVirtual(false);
        card.setCreatedAt(LocalDateTime.now());
        card.setAccount(account);

        if (cardType == CardType.CREDIT) {
            card.setCreditLimit(new BigDecimal("100000"));
        }

        return cardRepository.save(card);
    }

    @Transactional
    public Card blockCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        card.setStatus(CardStatus.BLOCKED);
        return cardRepository.save(card);
    }

    @Transactional
    public Card unblockCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        card.setStatus(CardStatus.ACTIVE);
        return cardRepository.save(card);
    }

    @Transactional
    public Card createVirtualCard(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Card card = new Card();
        card.setCardNumber(generateCardNumber());
        card.setCardholderName(account.getUser().getFirstName() + " " + account.getUser().getLastName());
        card.setExpiryDate(LocalDate.now().plusYears(1));
        card.setCvv(generateCVV());
        card.setCardType(CardType.VIRTUAL);
        card.setStatus(CardStatus.ACTIVE);
        card.setCurrency(account.getCurrency());
        card.setBalance(BigDecimal.ZERO);
        card.setIsVirtual(true);
        card.setCreatedAt(LocalDateTime.now());
        card.setAccount(account);

        return cardRepository.save(card);
    }

    @Transactional
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        cardRepository.delete(card);
    }

    private String generateCardNumber() {
        return "4" + String.format("%015d", System.currentTimeMillis() % 1000000000000000L);
    }

    private String generateCVV() {
        return String.format("%03d", (int)(Math.random() * 1000));
    }
}

