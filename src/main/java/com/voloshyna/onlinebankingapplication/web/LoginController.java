package com.voloshyna.onlinebankingapplication.web;

import com.voloshyna.onlinebankingapplication.entity.*;
import com.voloshyna.onlinebankingapplication.service.interf.ClientService;
import com.voloshyna.onlinebankingapplication.service.interf.UserService;
import com.voloshyna.onlinebankingapplication.staticData.CurrencyRate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private ClientService clientService;
    @GetMapping("/login")
    public String showLoginForm(){
        return"login-registration";
    }
//    @PostMapping("/default")
//    public String redirectingToHomes(HttpServletRequest request){
//        if(request.isUserInRole("MANAGER")) return "redirect:/manager/dashboard";
//        return "redirect:/home/dashboard";
//    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);

        if (user.getRole().getId().equals(2L)) {

            Manager manager = user.getManager();
            List<Client> clients = manager.getClientList();
            Integer numberOfBankAccounts = calculateManagerClientsBankAccounts(clients);
            Double totalSumOfAllBankAccounts = calculateTotalSumForManagerClients(clients);
            if (manager != null) {
                model.addAttribute("username", manager.getFirstName());
                model.addAttribute("userId", user.getUserId());
                model.addAttribute("user", user);
                model.addAttribute("manager", manager);
                model.addAttribute("clients", clients);
                model.addAttribute("numberOfBankAccounts", numberOfBankAccounts);
                model.addAttribute("totalSumOfAllBankAccounts", totalSumOfAllBankAccounts);
                return "manager-view/manager-dashboard";
            } else {
                return "error-page";
            }
        }
        if ( user.getRole().getId().equals(1L)) {
            Manager adminManager = user.getManager();
            Integer numberOfClients = calculateClients();
            Integer numberOfTotalBankAccounts = calculateNumberOfBankAccounts();
            Double totalSumForAllClients = calculateTotalSumForAllClients();

            model.addAttribute("manager" , adminManager);
            model.addAttribute("numberOfClients", numberOfClients);
            model.addAttribute("numberOfTotalBankAccounts", numberOfTotalBankAccounts);
            model.addAttribute("totalSumForAllClients", totalSumForAllClients);
            return "manager-view/admin-page/admin-dashboard";
        } else {
            Client client = user.getClient();
            List <BankAccount> bankAccounts = client.getAccountList();
            Collections.sort(bankAccounts, Comparator.comparing(BankAccount::getCurrency));

            if (client != null) {
                model.addAttribute("username", client.getFirstName());
                model.addAttribute("userId", user.getUserId());
                model.addAttribute("client", client);
                model.addAttribute("bankAccounts", client.getAccountList());
                return "client-view/dashboard";
            } else {
                return "error-page";
            }
        }
    }

    @GetMapping("/register-success")
    public String signInSuccess(){
        return"client-view/success-registered";
    }


    //MANAGER STATISTIC DATA UTILS
    public Integer calculateManagerClientsBankAccounts(List <Client> clients){
        Integer numberOfAccounts = 0;
        for (Client client:clients) {
            List<BankAccount> bankAccounts = client.getAccountList();
            numberOfAccounts += bankAccounts.size();
        }
        return numberOfAccounts;
    }

    public Double calculateTotalSumForManagerClients(List <Client> clients){
        Double totalSumOfAllBankAccounts = 0.0;
        return calculateSumDueToCurrency(totalSumOfAllBankAccounts, clients);
    }


    // ADMIN STATISTIC DATA UTILS
    public Integer calculateClients(){
        Integer numberOfClients = clientService.getAllClientInDataBase().size();
        return numberOfClients;
    }

    public Integer calculateNumberOfBankAccounts(){
        Integer numberOfBankAccounts = 0;
        List <Client> clients = clientService.getAllClientInDataBase();
        for (Client client:clients) {
            numberOfBankAccounts += client.getAccountList().size();
        }
        return numberOfBankAccounts;
    }

    public Double calculateTotalSumForAllClients(){
        Double totalSumForAllClients = 0.0;
        List <Client> clients = clientService.getAllClientInDataBase();
        return calculateSumDueToCurrency(totalSumForAllClients, clients);
    }

    private Double calculateSumDueToCurrency(Double totalSumForAllClients, List<Client> clients) {
        for (Client client:clients) {
            List<BankAccount> bankAccounts = client.getAccountList();
            for (BankAccount bankAccount: bankAccounts) {
                Double usd = 0.0;
                Double eur = 0.0;
                Double uah = 0.0;

                if(bankAccount.getCurrency().equals(Currency.USD)){
                    usd = bankAccount.getCurrentSum()* CurrencyRate.getUSD();
                }
                if(bankAccount.getCurrency().equals(Currency.EUR)){
                    eur = bankAccount.getCurrentSum()* CurrencyRate.getEUR();
                }
                if(bankAccount.getCurrency().equals(Currency.UAH)){
                    uah = bankAccount.getCurrentSum();
                }

                totalSumForAllClients += (usd+eur+uah);
            }

        }
        return totalSumForAllClients;
    }
}
