package com.voloshyna.onlinebankingapplication.web;

import com.voloshyna.onlinebankingapplication.entity.BankAccount;
import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Currency;
import com.voloshyna.onlinebankingapplication.entity.User;
import com.voloshyna.onlinebankingapplication.service.interf.BankAccountService;
import com.voloshyna.onlinebankingapplication.service.interf.ClientService;
import com.voloshyna.onlinebankingapplication.service.interf.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
@RequestMapping("/account")
public class BankAccountController {

   private final BankAccountService bankAccountService;
   private final UserService userService;
    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("bankAccount", new BankAccount());
        return "bankAccount-view/account";
    }


    //TODO: get ID from session
    @PostMapping("/save")
    public String save(@Valid BankAccount bankAccount, @RequestParam ("currency") Currency currency, Model model, BindingResult bindingResult, Authentication authentication){

        String userEmail = authentication.getName();
        User user = userService.getUserByEmail(userEmail);
        Client client = user.getClient();
        Long clientId = client.getId();
        if (bindingResult.hasErrors()) {
            // Validation errors occurred
            return "bankAccount-view/account";
        }
        try {
            bankAccountService.createBankAccount(clientId, currency);
            model.addAttribute("successMessage", "Bank account created successfully!");
            return "bankAccount-view/account";
        }catch (RuntimeException ex){
            model.addAttribute("errorMessage", ex.getMessage());
            return "bankAccount-view/account";

        }

    }

}
