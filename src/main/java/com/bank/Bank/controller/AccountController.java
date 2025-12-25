package com.bank.Bank.controller;

import com.bank.Bank.model.Account;
import com.bank.Bank.model.enums.AccountType;
import com.bank.Bank.model.enums.CurrencyType;
import com.bank.Bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
@RequestMapping("/api/accounts")
public class AccountController extends BaseController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

    @GetMapping("/page")
    public String accountsPage(Model model) {
        Long userId = getCurrentUserId();
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        
        // Вычисление статистики на сервере
        BigDecimal totalBalance = accounts.stream()
            .map(Account::getBalance)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long activeCount = accounts.stream()
            .filter(Account::getIsActive)
            .count();
        
        model.addAttribute("accounts", accounts);
        model.addAttribute("totalBalance", totalBalance);
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("totalCount", accounts.size());
        
        return "accounts";
    }

    @PostMapping
    public String createAccount(
            @RequestParam AccountType accountType,
            @RequestParam CurrencyType currency,
            @RequestParam(required = false, defaultValue = "0") BigDecimal initialBalance,
            RedirectAttributes redirectAttributes) {
        
        try {
            if (initialBalance == null) {
                initialBalance = BigDecimal.ZERO;
            }
            initialBalance = initialBalance.setScale(2, RoundingMode.HALF_UP);
            
            Long userId = getCurrentUserId();
            accountService.createAccount(userId, accountType, currency, initialBalance);
            addSuccessMessage(redirectAttributes, "Счет успешно создан!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        
        return "redirect:/api/accounts/page";
    }
}
