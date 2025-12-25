package com.bank.Bank.service;

import com.bank.Bank.model.Account;
import com.bank.Bank.model.Bill;
import com.bank.Bank.model.PaymentTemplate;
import com.bank.Bank.model.enums.PaymentCategory;
import com.bank.Bank.model.enums.PaymentStatus;
import com.bank.Bank.repository.BillRepository;
import com.bank.Bank.repository.PaymentTemplateRepository;
import com.bank.Bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private PaymentTemplateRepository paymentTemplateRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Bill> getBillsByUserId(Long userId) {
        return billRepository.findByAccountUserId(userId);
    }

    @Transactional
    public Bill createBill(Long accountId, String providerName, BigDecimal amount, 
                          PaymentCategory category, String accountNumber, String paymentCode) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Bill bill = new Bill();
        bill.setBillNumber(generateBillNumber());
        bill.setProviderName(providerName);
        bill.setAmount(amount);
        bill.setStatus(PaymentStatus.PENDING);
        bill.setCategory(category);
        bill.setAccountNumber(accountNumber);
        bill.setPaymentCode(paymentCode);
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setIsAutoPayment(false);
        bill.setCreatedAt(LocalDateTime.now());
        bill.setAccount(account);

        return billRepository.save(bill);
    }

    @Transactional
    public Bill payBill(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        Account account = bill.getAccount();

        if (account.getBalance().compareTo(bill.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(bill.getAmount()));
        bill.setStatus(PaymentStatus.COMPLETED);
        bill.setPaidDate(LocalDate.now());

        accountRepository.save(account);
        return billRepository.save(bill);
    }

    @Transactional(readOnly = true)
    public List<PaymentTemplate> getTemplatesByUserId(Long userId) {
        return paymentTemplateRepository.findByAccountUserId(userId);
    }

    @Transactional
    public PaymentTemplate createTemplate(Long accountId, String templateName, String providerName,
                                         BigDecimal amount, PaymentCategory category, 
                                         String accountNumber, String paymentCode, Boolean isRecurring) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        PaymentTemplate template = new PaymentTemplate();
        template.setTemplateName(templateName);
        template.setProviderName(providerName);
        template.setAmount(amount);
        template.setCategory(category);
        template.setAccountNumber(accountNumber);
        template.setPaymentCode(paymentCode);
        template.setIsRecurring(isRecurring != null ? isRecurring : false);
        template.setCreatedAt(LocalDateTime.now());
        template.setAccount(account);

        return paymentTemplateRepository.save(template);
    }

    @Transactional
    public void deleteTemplate(Long id) {
        PaymentTemplate template = paymentTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        paymentTemplateRepository.delete(template);
    }

    private String generateBillNumber() {
        return "BILL" + System.currentTimeMillis();
    }
}

