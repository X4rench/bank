package com.bank.Bank.controller;

import com.bank.Bank.model.Account;
import com.bank.Bank.model.Transfer;
import com.bank.Bank.model.enums.TransferType;
import com.bank.Bank.service.AccountService;
import com.bank.Bank.service.TransferService;
import com.bank.Bank.util.EnumLocalizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/transfers")
public class TransferController extends BaseController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountService accountService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        return ResponseEntity.ok(transferService.getAllTransfers());
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<List<Transfer>> getTransfersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(transferService.getTransfersByUserId(userId));
    }

    @GetMapping("/page")
    public String transfersPage(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TransferType transferType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "date") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            Model model) {
        
        LocalDate start = startDate != null && !startDate.isEmpty() ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null && !endDate.isEmpty() ? LocalDate.parse(endDate) : null;
        
        Long userId = getCurrentUserId();
        List<Transfer> transfers = transferService.searchTransfers(userId, search, transferType, start, end, sortBy, sortOrder);
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        
        model.addAttribute("transfers", transfers);
        model.addAttribute("accounts", accounts);
        model.addAttribute("search", search);
        model.addAttribute("transferType", transferType);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("transferTypes", TransferType.values());
        model.addAttribute("transferTypeNames", EnumLocalizationUtil.getTransferTypeNames());
        
        return "transfers";
    }

    @PostMapping
    public String createTransfer(
            @RequestParam Long fromAccountId,
            @RequestParam(required = false) Long toAccountId,
            @RequestParam BigDecimal amount,
            @RequestParam TransferType transferType,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes) {
        
        try {
            transferService.createTransfer(fromAccountId, toAccountId, amount, transferType, description);
            addSuccessMessage(redirectAttributes, "Перевод выполнен успешно!");
        } catch (Exception e) {
            handleException(redirectAttributes, e);
        }
        
        return "redirect:/api/transfers/page";
    }

    @PostMapping("/external")
    @ResponseBody
    public ResponseEntity<Transfer> createExternalTransfer(@RequestParam Long fromAccountId,
                                                           @RequestParam(required = false) String recipientAccount,
                                                           @RequestParam(required = false) String recipientCard,
                                                           @RequestParam(required = false) String recipientPhone,
                                                           @RequestParam BigDecimal amount,
                                                           @RequestParam TransferType transferType,
                                                           @RequestParam(required = false) String description) {
        Transfer transfer = transferService.createExternalTransfer(fromAccountId, recipientAccount, 
                                                                   recipientCard, recipientPhone, 
                                                                   amount, transferType, description);
        return ResponseEntity.ok(transfer);
    }
}
