package com.bank.Bank.controller;

import com.bank.Bank.model.Budget;
import com.bank.Bank.model.enums.PaymentCategory;
import com.bank.Bank.service.BudgetService;
import com.bank.Bank.util.EnumLocalizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/budgets")
public class BudgetController extends BaseController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<List<Budget>> getBudgetsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(budgetService.getBudgetsByUserId(userId));
    }

    @GetMapping("/user/{userId}/active")
    @ResponseBody
    public ResponseEntity<List<Budget>> getActiveBudgetsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(budgetService.getActiveBudgetsByUserId(userId));
    }

    @GetMapping("/page")
    public String budgetsPage(Model model) {
        Long userId = getCurrentUserId();
        List<Budget> budgets = budgetService.getBudgetsByUserId(userId);
        
        // Обновляем потраченные суммы для всех бюджетов
        budgets = budgets.stream()
            .map(budget -> budgetService.updateSpentAmount(budget.getId()))
            .collect(Collectors.toList());
        
        // Вычисление статистики на сервере
        BigDecimal totalLimit = budgets.stream()
            .map(Budget::getLimitAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalSpent = budgets.stream()
            .map(Budget::getSpentAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal remaining = totalLimit.subtract(totalSpent);
        BigDecimal percentage = totalLimit.compareTo(BigDecimal.ZERO) > 0
            ? totalSpent.divide(totalLimit, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
            : BigDecimal.ZERO;
        
        long activeCount = budgets.stream()
            .filter(Budget::getIsActive)
            .count();
        
        model.addAttribute("budgets", budgets);
        model.addAttribute("totalLimit", totalLimit);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("remaining", remaining);
        model.addAttribute("percentage", percentage.setScale(1, RoundingMode.HALF_UP));
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("categoryNames", EnumLocalizationUtil.getPaymentCategoryNames());
        
        return "budgets";
    }

    @PostMapping
    public String createBudget(
            @RequestParam PaymentCategory category,
            @RequestParam BigDecimal limitAmount,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            RedirectAttributes redirectAttributes) {
        
        try {
            Long userId = getCurrentUserId();
            budgetService.createBudget(userId, category, limitAmount, startDate, endDate);
            addSuccessMessage(redirectAttributes, "Бюджет создан успешно!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        
        return "redirect:/api/budgets/page";
    }

    @PostMapping("/{id}/update-spent")
    @ResponseBody
    public ResponseEntity<Budget> updateSpentAmount(@PathVariable Long id) {
        Budget budget = budgetService.updateSpentAmount(id);
        return ResponseEntity.ok(budget);
    }

    @PostMapping("/{id}/delete")
    public String deleteBudget(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            budgetService.deleteBudget(id);
            addSuccessMessage(redirectAttributes, "Бюджет удален!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        return "redirect:/api/budgets/page";
    }
}
