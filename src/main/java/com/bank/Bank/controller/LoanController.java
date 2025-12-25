package com.bank.Bank.controller;

import com.bank.Bank.model.Loan;
import com.bank.Bank.model.enums.CurrencyType;
import com.bank.Bank.model.enums.LoanType;
import com.bank.Bank.service.LoanService;
import com.bank.Bank.util.EnumLocalizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/api/loans")
public class LoanController extends BaseController {

    @Autowired
    private LoanService loanService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<List<Loan>> getLoansByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getLoansByUserId(userId));
    }

    @GetMapping("/page")
    public String loansPage(Model model) {
        Long userId = getCurrentUserId();
        List<Loan> loans = loanService.getLoansByUserId(userId);
        
        model.addAttribute("loans", loans);
        model.addAttribute("loanTypes", LoanType.values());
        model.addAttribute("loanTypeNames", EnumLocalizationUtil.getLoanTypeNames());
        model.addAttribute("currencies", CurrencyType.values());
        
        return "loans";
    }

    @PostMapping
    public String createLoan(
            @RequestParam LoanType loanType,
            @RequestParam BigDecimal principalAmount,
            @RequestParam BigDecimal interestRate,
            @RequestParam int termMonths,
            @RequestParam CurrencyType currency,
            RedirectAttributes redirectAttributes) {
        
        try {
            Long userId = getCurrentUserId();
            loanService.createLoan(userId, loanType, principalAmount, interestRate, termMonths, currency);
            addSuccessMessage(redirectAttributes, "Кредит оформлен успешно!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        
        return "redirect:/api/loans/page";
    }

    @PostMapping("/{id}/payment")
    public String makePayment(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            RedirectAttributes redirectAttributes) {
        try {
            loanService.makePayment(id, amount);
            addSuccessMessage(redirectAttributes, "Платеж внесен успешно!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        return "redirect:/api/loans/page";
    }
}
