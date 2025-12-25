package com.bank.Bank.controller;

import com.bank.Bank.model.*;
import com.bank.Bank.repository.UserRepository;
import com.bank.Bank.service.*;
import com.bank.Bank.util.EnumLocalizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MainController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardService cardService;

    @Autowired
    private TransferService transferService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private DepositService depositService;

    @GetMapping("/")
    public String index(Model model) {
        try {
            Long userId = getCurrentUserId();
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
            }
        } catch (Exception e) {
            model.addAttribute("user", null);
        }
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            Long userId = getCurrentUserId();
            Optional<User> user = userRepository.findById(userId);
            if (!user.isPresent()) {
                return "redirect:/";
            }
            
            model.addAttribute("user", user.get());
            
            // Загрузка данных на сервере
            List<Account> accounts = accountService.getAccountsByUserId(userId);
            List<Card> cards = cardService.getCardsByUserId(userId);
            List<Transfer> transfers = transferService.getTransfersByUserId(userId);
            List<Loan> loans = loanService.getLoansByUserId(userId);
            List<Deposit> deposits = depositService.getDepositsByUserId(userId);
            
            // Вычисление статистики на сервере
            BigDecimal totalBalance = accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal totalLoans = loans.stream()
                .map(Loan::getRemainingBalance)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal totalDeposits = deposits.stream()
                .map(Deposit::getPrincipalAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Последние 10 транзакций
            List<Transfer> recentTransfers = transfers.stream()
                .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
                .limit(10)
                .collect(Collectors.toList());
            
            model.addAttribute("totalBalance", totalBalance);
            model.addAttribute("accountsCount", accounts.size());
            model.addAttribute("cardsCount", cards.size());
            model.addAttribute("transfersCount", transfers.size());
            model.addAttribute("cards", cards);
            model.addAttribute("recentTransfers", recentTransfers);
            model.addAttribute("totalLoans", totalLoans);
            model.addAttribute("totalDeposits", totalDeposits);
            model.addAttribute("transferTypeNames", EnumLocalizationUtil.getTransferTypeNames());
            model.addAttribute("userId", userId);
            
            return "user/dashboard";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        try {
            Long userId = getCurrentUserId();
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
            }
        } catch (Exception e) {
            // If database is not ready, continue without user
        }
        return "settings";
    }
}
