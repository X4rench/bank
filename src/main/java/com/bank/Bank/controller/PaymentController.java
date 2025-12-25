package com.bank.Bank.controller;

import com.bank.Bank.model.Account;
import com.bank.Bank.model.Bill;
import com.bank.Bank.model.PaymentTemplate;
import com.bank.Bank.model.enums.PaymentCategory;
import com.bank.Bank.service.AccountService;
import com.bank.Bank.service.PaymentService;
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
@RequestMapping("/api/payments")
public class PaymentController extends BaseController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/bills")
    @ResponseBody
    public ResponseEntity<List<Bill>> getAllBills() {
        return ResponseEntity.ok(paymentService.getAllBills());
    }

    @GetMapping("/bills/user/{userId}")
    @ResponseBody
    public ResponseEntity<List<Bill>> getBillsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getBillsByUserId(userId));
    }

    @GetMapping("/page")
    public String paymentsPage(Model model) {
        Long userId = getCurrentUserId();
        List<Bill> bills = paymentService.getBillsByUserId(userId);
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        
        model.addAttribute("bills", bills);
        model.addAttribute("accounts", accounts);
        model.addAttribute("categories", PaymentCategory.values());
        model.addAttribute("categoryNames", EnumLocalizationUtil.getPaymentCategoryNames());
        
        return "payments";
    }

    @PostMapping("/bills")
    public String createBill(
            @RequestParam Long accountId,
            @RequestParam String providerName,
            @RequestParam BigDecimal amount,
            @RequestParam PaymentCategory category,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String paymentCode,
            RedirectAttributes redirectAttributes) {
        
        try {
            paymentService.createBill(accountId, providerName, amount, category, accountNumber, paymentCode);
            addSuccessMessage(redirectAttributes, "Счет создан успешно!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        
        return "redirect:/api/payments/page";
    }

    @PostMapping("/bills/{id}/pay")
    public String payBill(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            paymentService.payBill(id);
            addSuccessMessage(redirectAttributes, "Счет оплачен успешно!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        return "redirect:/api/payments/page";
    }

    @GetMapping("/templates/user/{userId}")
    @ResponseBody
    public ResponseEntity<List<PaymentTemplate>> getTemplatesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getTemplatesByUserId(userId));
    }

    @PostMapping("/templates")
    @ResponseBody
    public ResponseEntity<PaymentTemplate> createTemplate(@RequestParam Long accountId,
                                                          @RequestParam String templateName,
                                                          @RequestParam String providerName,
                                                          @RequestParam BigDecimal amount,
                                                          @RequestParam PaymentCategory category,
                                                          @RequestParam(required = false) String accountNumber,
                                                          @RequestParam(required = false) String paymentCode,
                                                          @RequestParam(required = false) Boolean isRecurring) {
        PaymentTemplate template = paymentService.createTemplate(accountId, templateName, providerName, 
                                                                 amount, category, accountNumber, 
                                                                 paymentCode, isRecurring);
        return ResponseEntity.ok(template);
    }

    @DeleteMapping("/templates/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        paymentService.deleteTemplate(id);
        return ResponseEntity.ok().build();
    }
}
