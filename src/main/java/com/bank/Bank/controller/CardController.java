package com.bank.Bank.controller;

import com.bank.Bank.model.Account;
import com.bank.Bank.model.Card;
import com.bank.Bank.model.enums.CardType;
import com.bank.Bank.service.AccountService;
import com.bank.Bank.service.CardService;
import com.bank.Bank.util.EnumLocalizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/api/cards")
public class CardController extends BaseController {

    @Autowired
    private CardService cardService;

    @Autowired
    private AccountService accountService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<List<Card>> getCardsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(cardService.getCardsByUserId(userId));
    }

    @GetMapping("/page")
    public String cardsPage(Model model) {
        Long userId = getCurrentUserId();
        List<Card> cards = cardService.getCardsByUserId(userId);
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        
        model.addAttribute("cards", cards);
        model.addAttribute("accounts", accounts);
        model.addAttribute("cardTypes", CardType.values());
        model.addAttribute("cardTypeNames", EnumLocalizationUtil.getCardTypeNames());
        
        return "cards";
    }

    @PostMapping
    public String createCard(
            @RequestParam Long accountId,
            @RequestParam CardType cardType,
            @RequestParam String cardholderName,
            RedirectAttributes redirectAttributes) {
        
        try {
            cardService.createCard(accountId, cardType, cardholderName);
            addSuccessMessage(redirectAttributes, "Карта успешно выпущена!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        
        return "redirect:/api/cards/page";
    }

    @PostMapping("/{id}/block")
    public String blockCard(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cardService.blockCard(id);
            addSuccessMessage(redirectAttributes, "Карта заблокирована!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        return "redirect:/api/cards/page";
    }

    @PostMapping("/{id}/unblock")
    public String unblockCard(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cardService.unblockCard(id);
            addSuccessMessage(redirectAttributes, "Карта разблокирована!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        return "redirect:/api/cards/page";
    }

    @PostMapping("/virtual")
    public String createVirtualCard(
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) Long cardId,
            RedirectAttributes redirectAttributes) {
        try {
            Long accId = accountId;
            if (accId == null && cardId != null) {
                Card card = cardService.getCardById(cardId)
                    .orElseThrow(() -> new RuntimeException("Card not found"));
                accId = card.getAccount().getId();
            }
            if (accId == null) {
                // Используем первый доступный счет
                Long userId = getCurrentUserId();
                List<Account> accounts = accountService.getAccountsByUserId(userId);
                if (accounts.isEmpty()) {
                    throw new RuntimeException("No accounts found");
                }
                accId = accounts.get(0).getId();
            }
            cardService.createVirtualCard(accId);
            addSuccessMessage(redirectAttributes, "Виртуальная карта создана!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        return "redirect:/api/cards/page";
    }
}
