package com.bank.Bank.controller;

import com.bank.Bank.model.Deposit;
import com.bank.Bank.model.enums.CurrencyType;
import com.bank.Bank.model.enums.DepositType;
import com.bank.Bank.service.DepositService;
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
@RequestMapping("/api/deposits")
public class DepositController extends BaseController {

    @Autowired
    private DepositService depositService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Deposit>> getAllDeposits() {
        return ResponseEntity.ok(depositService.getAllDeposits());
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<List<Deposit>> getDepositsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(depositService.getDepositsByUserId(userId));
    }

    @GetMapping("/page")
    public String depositsPage(Model model) {
        Long userId = getCurrentUserId();
        List<Deposit> deposits = depositService.getDepositsByUserId(userId);
        
        model.addAttribute("deposits", deposits);
        model.addAttribute("depositTypes", DepositType.values());
        model.addAttribute("depositTypeNames", EnumLocalizationUtil.getDepositTypeNames());
        model.addAttribute("currencies", CurrencyType.values());
        
        return "deposits";
    }

    @PostMapping
    public String createDeposit(
            @RequestParam DepositType depositType,
            @RequestParam BigDecimal principalAmount,
            @RequestParam BigDecimal interestRate,
            @RequestParam int termMonths,
            @RequestParam(required = false, defaultValue = "false") Boolean isAutoRenewal,
            @RequestParam CurrencyType currency,
            RedirectAttributes redirectAttributes) {
        
        try {
            Long userId = getCurrentUserId();
            depositService.createDeposit(userId, depositType, principalAmount, 
                                        interestRate, termMonths, isAutoRenewal, currency);
            addSuccessMessage(redirectAttributes, "Вклад открыт успешно!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        
        return "redirect:/api/deposits/page";
    }

    @PostMapping("/{id}/calculate-interest")
    public String calculateInterest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            depositService.calculateInterest(id);
            addSuccessMessage(redirectAttributes, "Проценты начислены!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        return "redirect:/api/deposits/page";
    }

    @PostMapping("/{id}/close")
    public String closeDeposit(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            depositService.closeDeposit(id);
            addSuccessMessage(redirectAttributes, "Вклад закрыт, средства возвращены на счет!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        return "redirect:/api/deposits/page";
    }
}
