package com.bank.Bank.repository;

import com.bank.Bank.model.Card;
import com.bank.Bank.model.User;
import com.bank.Bank.model.enums.CardStatus;
import com.bank.Bank.model.enums.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByAccountUserId(Long userId);
    List<Card> findByAccountUserIdAndStatus(Long userId, CardStatus status);
    Optional<Card> findByCardNumber(String cardNumber);
    List<Card> findByAccountUserIdAndCardType(Long userId, CardType cardType);
}


