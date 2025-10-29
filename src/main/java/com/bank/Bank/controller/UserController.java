package com.bank.Bank.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user/dashboard")
    public String userDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        model.addAttribute("username", username);
        return "user/dashboard";
    }

    @GetMapping("/user/accounts")
    public String userAccounts() {
        return "user/accounts";
    }

    @GetMapping("/user/transactions")
    public String userTransactions() {
        return "user/transactions";
    }
}

