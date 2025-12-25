package com.bank.Bank.controller;

import com.bank.Bank.model.enums.PaymentCategory;
import com.bank.Bank.service.AnalyticsService;
import com.bank.Bank.util.EnumLocalizationUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/analytics")
public class AnalyticsController extends BaseController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/expenses-by-category")
    @ResponseBody
    public ResponseEntity<Map<PaymentCategory, BigDecimal>> getExpensesByCategory(
            @RequestParam Long userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        Map<PaymentCategory, BigDecimal> expenses = analyticsService.getExpensesByCategory(userId, startDate, endDate);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/total-income")
    @ResponseBody
    public ResponseEntity<BigDecimal> getTotalIncome(
            @RequestParam Long userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        BigDecimal income = analyticsService.getTotalIncome(userId, startDate, endDate);
        return ResponseEntity.ok(income);
    }

    @GetMapping("/total-expenses")
    @ResponseBody
    public ResponseEntity<BigDecimal> getTotalExpenses(
            @RequestParam Long userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        BigDecimal expenses = analyticsService.getTotalExpenses(userId, startDate, endDate);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/page")
    public String analyticsPage(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {
        
        LocalDate start = startDate != null && !startDate.isEmpty() 
            ? LocalDate.parse(startDate) 
            : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null && !endDate.isEmpty() 
            ? LocalDate.parse(endDate) 
            : LocalDate.now();
        
        Long userId = getCurrentUserId();
        Map<PaymentCategory, BigDecimal> expensesByCategory = analyticsService.getExpensesByCategory(userId, start, end);
        BigDecimal totalIncome = analyticsService.getTotalIncome(userId, start, end);
        BigDecimal totalExpenses = analyticsService.getTotalExpenses(userId, start, end);
        BigDecimal balance = totalIncome.subtract(totalExpenses);
        
        // Вычисление процентов и прогнозов на сервере
        Map<String, Object> expensesData = new HashMap<>();
        Map<String, BigDecimal> percentages = new HashMap<>();
        Map<String, BigDecimal> forecasts = new HashMap<>();
        
        for (Map.Entry<PaymentCategory, BigDecimal> entry : expensesByCategory.entrySet()) {
            BigDecimal amount = entry.getValue();
            BigDecimal percentage = totalExpenses.compareTo(BigDecimal.ZERO) > 0 
                ? amount.divide(totalExpenses, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
                : BigDecimal.ZERO;
            BigDecimal forecast = amount.multiply(new BigDecimal("12")); // Годовой прогноз
            
            expensesData.put(entry.getKey().name(), amount);
            percentages.put(entry.getKey().name(), percentage.setScale(1, RoundingMode.HALF_UP));
            forecasts.put(entry.getKey().name(), forecast);
        }
        
        BigDecimal savingsRate = totalIncome.compareTo(BigDecimal.ZERO) > 0
            ? balance.divide(totalIncome, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
            : BigDecimal.ZERO;
        
        model.addAttribute("expensesByCategory", expensesByCategory);
        model.addAttribute("expensesData", expensesData);
        model.addAttribute("percentages", percentages);
        model.addAttribute("forecasts", forecasts);
        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("balance", balance);
        model.addAttribute("savingsRate", savingsRate.setScale(1, RoundingMode.HALF_UP));
        model.addAttribute("startDate", start.toString());
        model.addAttribute("endDate", end.toString());
        model.addAttribute("categoryNames", EnumLocalizationUtil.getPaymentCategoryNames());
        
        return "analytics";
    }

    @GetMapping("/export")
    public void exportToCSV(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            HttpServletResponse response) throws IOException {
        
        LocalDate start = startDate != null && !startDate.isEmpty() 
            ? LocalDate.parse(startDate) 
            : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null && !endDate.isEmpty() 
            ? LocalDate.parse(endDate) 
            : LocalDate.now();
        
        Long userId = getCurrentUserId();
        Map<PaymentCategory, BigDecimal> expensesByCategory = analyticsService.getExpensesByCategory(userId, start, end);
        BigDecimal totalIncome = analyticsService.getTotalIncome(userId, start, end);
        BigDecimal totalExpenses = analyticsService.getTotalExpenses(userId, start, end);
        
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=analytics_" + start + "_" + end + ".csv");
        
        PrintWriter writer = response.getWriter();
        writer.println("\uFEFFКатегория,Сумма"); // BOM для корректного отображения в Excel
        writer.println("Доходы," + totalIncome);
        writer.println("Расходы," + totalExpenses);
        writer.println();
        writer.println("Расходы по категориям:");
        
        Map<PaymentCategory, String> categoryNames = EnumLocalizationUtil.getPaymentCategoryNames();
        for (Map.Entry<PaymentCategory, BigDecimal> entry : expensesByCategory.entrySet()) {
            String categoryName = categoryNames.getOrDefault(entry.getKey(), "Прочее");
            writer.println(categoryName + "," + entry.getValue());
        }
        
        writer.flush();
        writer.close();
    }
}
