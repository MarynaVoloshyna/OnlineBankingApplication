package com.voloshyna.onlinebankingapplication.web;

import com.voloshyna.onlinebankingapplication.entity.*;
import com.voloshyna.onlinebankingapplication.service.interf.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
@RequestMapping("/manager")
public class ManagerController {
    private final ManagerService managerService;
    private final UserService userService;
    private final ClientService clientService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final Environment environment;


    //MANAGER REGISTRATION
    @GetMapping("/registration")
    public String createManager(Model model) {
        model.addAttribute("manager", new Manager());
        return "manager-view/manager-form";
    }
    @PostMapping("/save")
    public String saveManager(@Valid Manager manager, BindingResult bindingResult, Model model) {
        User user = userService.getUserByEmail(manager.getUser().getEmail());
        String friendlyBankDomain = environment.getProperty("friendly.bank.domain");

        if (user != null) {
            bindingResult.rejectValue("user.email", null, "Account with this email already registered");
        }
        if (!(manager.getUser().getEmail().contains(friendlyBankDomain))) {
            bindingResult.rejectValue("user.email", null, "You are not allowed to be a manager");
        }

        if (bindingResult.hasErrors()) {
            return "manager-view/manager-form";
        }

        managerService.createManager(manager.getFirstName(),
                manager.getLastName(),
                manager.getLevel(),
                manager.getUser().getEmail(),
                manager.getUser().getPassword());
        model.addAttribute("successRegistered", "You have been successfully registered. Please log in!");

        return "manager-view/manager-form";
    }

//    @PostMapping("/save")
//    public String saveManager(@Valid Manager manager, BindingResult bindingResult) {
//        User user = userService.getUserByEmail(manager.getUser().getEmail());
//        String friendlyBankDomain = environment.getProperty("friendly.bank.domain");
//
//        if (user != null) {
//            bindingResult.rejectValue("user.email", null,
//                    "Account with this email already registered");
//        }
//        if (!manager.getUser().getEmail().contains(friendlyBankDomain)) {
//            bindingResult.rejectValue("manager.user.email", null, "You are not allowed to be a manager");
//        }
//
//        if (bindingResult.hasErrors()) {
//            return "manager-view/manager-form";
//        }
//
//        managerService.createManager(manager.getFirstName(),
//                manager.getLastName(),
//                manager.getLevel(),
//                manager.getUser().getEmail(),
//                manager.getUser().getPassword());
//        return "redirect:/login";
//
//    }

    @PostMapping("/dashboard")
    public String showManagerDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);
        Manager manager = managerService.findByEmail(username);
        System.out.println(manager);
        model.addAttribute("manager", manager);
        model.addAttribute("username", user.getManager().getFirstName());
        model.addAttribute("userId", user.getUserId());
        return "manager-view/manager-dashboard";
    }

    //MANAGER INFO
    @GetMapping("/info")
    public String showManagerInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);
        Manager manager = managerService.findByEmail(username);
        model.addAttribute("manager", manager);
        return "manager-view/manager-info";

    }


// FETCH ALL CLIENTS

    @GetMapping("/clients/all")
    public String showClientByManager(Model model,
                                      @RequestParam(value = "sort", required = false) String sort,
                                      Authentication authentication,
                                      RedirectAttributes redirectAttributes,
                                      HttpSession http) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        Long managerId = user.getManager().getId();
        Manager manager = managerService.findById(managerId);
        redirectAttributes.addFlashAttribute("managerId", managerId);
        http.setAttribute("managerId", managerId);
        List<Client> clients = clientService.getAllClientsByManagerId(managerId);

        // Sort clients based on the provided sorting parameter
        if (sort != null) {
            switch (sort) {
                case "firstName":
                    clients.sort(Comparator.comparing(Client::getFirstName));
                    break;
                case "lastName":
                    clients.sort(Comparator.comparing(Client::getLastName));
                    break;
                case "taxNumber":
                    clients.sort(Comparator.comparing(Client::getTaxNumber));
                    break;
                case "dateOfRegistration":
                    clients.sort(Comparator.comparing(Client::getDateOfRegistration));
                    break;
            }
        }
        model.addAttribute("managerId", managerId);
        model.addAttribute("clients", clients);
        model.addAttribute("manager", manager);
        return "manager-view/clients";
    }

    // CHECK EACH CLIENTS BANK ACCOUNT (TRANSACTIONS CHECK AVAILABLE)
//    @RequestMapping(value = "/view-client-accounts", method = {RequestMethod.GET, RequestMethod.POST})
    @GetMapping("/view-client-accounts")
    public String viewClientAccounts(@RequestParam("clientId") Long clientId,
                                     Model model,
                                     HttpSession session) {
        Client client = clientService.findClientById(clientId);
        if (client != null) {
            model.addAttribute("listBankAccount", client.getAccountList());
            model.addAttribute("clientName", client.getFirstName());
            model.addAttribute("clientId", client.getId());
            return "manager-view/client-bankAccount";
        } else {
            return "redirect:/manager/clients/all";
        }
    }

    //CHECK TRANSACTIONS BY EACH BANK ACCOUNT WITH DATES FILTERING
    @GetMapping("/view-client-accounts/check-transactions")
    public String checkTransactions(@RequestParam("bankAccountId") Long bankAccountId,
                                    @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDate startDate,
                                    @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDate endDate,
                                    Model model) {
        BankAccount bankAccount = bankAccountService.findBankAccountById(bankAccountId);
        List<Transaction> transactionList = transactionService.getAllTransactionByBankAccount(bankAccount);
        if (startDate != null && !startDate.equals("") && endDate != null && !endDate.equals("")) {
            LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
            LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
            transactionList = transactionService.findTransactionsByPeriodAndBankAccountId(startDateTime, endDateTime, bankAccount.getId());

        }
        model.addAttribute("bankAccount", bankAccount);
        model.addAttribute("transactionList", transactionList);

        return "manager-view/check-transactions";
    }

    // CREATING BANK ACCOUNT FOR CLIENT
    @GetMapping("/client-service/create-account")
    public String showBankAccountForm(@RequestParam("clientId") Long clientId,
                                      Model model) {
        model.addAttribute("clientId", clientId);
        model.addAttribute("bankAccount", new BankAccount());
        return "manager-view/create-bankAccount";
    }

    @PostMapping("/client-service/save-bankAccount")
    public String saveBankAccount(@Valid BankAccount bankAccount,
                                  @RequestParam("currency") Currency currency,
                                  @RequestParam("clientId") Long  clientId,
                                  Model model,
                                  BindingResult bindingResult,
                                  HttpSession session) {
        Client client = clientService.findClientById(clientId);
        System.out.println(client);
        model.addAttribute("client", client);
        System.out.println(client);
        model.addAttribute("clientId", clientId);
        System.out.println(clientId);
        if (bindingResult.hasErrors()) {
            return "manager-view/create-bankAccount";
        }
        try {
            bankAccountService.createBankAccount(clientId, currency);
            model.addAttribute("successMessage", "Bank account created successfully!");
            return "manager-view/create-bankAccount";
        } catch (RuntimeException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "manager-view/create-bankAccount";

        }

    }

                                        // ADMIN METHODS//
    // FETCH ALL MANAGERS IN BANK
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public String showAllManager(Model model,
                                 @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        List<Manager> managers = managerService.searchManager(keyword);
        model.addAttribute("managers", managers);
        model.addAttribute("keyword", keyword);
        return "manager-view/admin-page/all-managers";
    }

    // CHECK CLIENTS BY EACH MANAGER
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all/check-clients")
    public String checkClientsByManager(Model model,
                                        @RequestParam(name = "name", defaultValue = "") String name,
                                        @RequestParam("managerId") Long managerId) {
        System.out.println("Controller method called with name: " + name + ", managerId: " + managerId);

        Manager manager = managerService.findById(managerId);
        List<Client> clients = clientService.getAllClientsByManagerId(managerId);
        List<Client> clientsList = clientService.getClientsByName(name);

        System.out.println("Retrieved " + clients.size() + " clients for managerId: " + managerId);
        System.out.println("Filtered clients by name: " + clientsList.size());

        model.addAttribute("clients", clients);
        model.addAttribute("manager", manager);
        model.addAttribute("name", name);

        return "manager-view/check-clients";
    }

    // FETCH ALL CLIENTS IN BANK (WITH CHANGING MANAGERS AND CHEKING BANK ACCOUNTS FUNCTIONALITIES)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all/clients")
    public String showAllClientsInBank(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                       Model model) {
        List<Client> clients = clientService.getClientsByName(keyword);
        model.addAttribute("clients", clients);
        model.addAttribute("keyword", keyword);
        return "manager-view/admin-page/all-clients";
    }

    // CHANGE CLIENT'S MANAGER
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/update-client")
    public String updateClient(@RequestParam(name = "clientId") Long clientId,
                               String managerEmail,
                               Model model) {
        Client client = clientService.findClientById(clientId);
        List<Manager> managers = managerService.findAll();
        model.addAttribute("client", client);
        model.addAttribute("managers", managers);
        return "manager-view/client-form";
    }

    @PostMapping("/save-client-change")
    public String saveClientChange(@ModelAttribute("client") Client client,
                                   @RequestParam("managerId") Long managerId,
                                   String managerEmail, Model model) {
        List<Manager> managers = managerService.findAll();
        Long clientId = client.getId();
        clientService.changeClientManager(clientId, managerId);
        model.addAttribute("client", client);
        model.addAttribute("managers", managers);
        return "redirect:/manager/all/clients";
    }


    // FETCH ALL BANK ACCOUNTS IN BANK (WITH CHEKING TRANSACTIONS BY EACH BANK ACCOUNT)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all/accounts")
    public String showAllAccountsInBank(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                        Model model) {
        List<BankAccount> bankAccounts = bankAccountService.findBankAccountsByAccountNumber(keyword);
        model.addAttribute("bankAccounts", bankAccounts);
        model.addAttribute("keyword", keyword);
        return "manager-view/admin-page/all-bankaccounts";
    }
    // FETCH ALL TRANSACTIONS FOR ALL BANK ACCOUNTS. FILTERING BY DATE AND BANK ACCOUNT NUMBER - BOTH OR SEPARATELY (WITH ABILITY DOWNLOAD RECEIPT)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all/transactions")
    public String showAllTransactions(
            @RequestParam(value = "useDateFilter", defaultValue = "false") boolean useDateFilter,
            @RequestParam(value = "useAccountNumberFilter", defaultValue = "false") boolean useAccountNumberFilter,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "accountNumber", required = false) String accountNumber,
            Model model) {

        List<Transaction> transactions;

        if (useAccountNumberFilter && accountNumber != null && !accountNumber.isEmpty()) {
            BankAccount bankAccount = bankAccountService.findBankAccountByAccountNumber(accountNumber);
            transactions = transactionService.getAllTransactionByBankAccount(bankAccount);
        } else {
            transactions = transactionService.findAll();
        }

        if (useDateFilter && startDate != null && endDate != null) {
            LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
            LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);

            if (useAccountNumberFilter) {
                transactions = transactions.stream()
                        .filter(transaction -> transaction.getTransactionDate().isAfter(startDateTime) &&
                                transaction.getTransactionDate().isBefore(endDateTime))
                        .collect(Collectors.toList());
            } else {
                transactions = transactionService.findTransactionsByPeriod(startDateTime, endDateTime);
            }
        }

        model.addAttribute("transactions", transactions);
        return "manager-view/admin-page/all-transactions";
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping("/global-searching")
//    public String searchingInManagerInfo() {
//        return "manager-view/searching-page";
//    }


    // SEARCH MANAGER BY EMAIL
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/global-searching/find/email")

    public String findManagerByEmail(Model model, @RequestParam(name = "email", defaultValue = "") String email) {
        List<Manager> managers = managerService.findManagersByEmail(email);
        model.addAttribute("managers", managers);
        model.addAttribute("email", email);
        return "manager-view/admin-page/global-mail";
    }
    // SEARCH MANAGER BY CLIENT'S TAX NUMBER
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/global-searching/find/tax")
    public String findManagerByClientTaxNumber(Model model, @RequestParam(name = "taxNumber", defaultValue = "") Long taxNumber) {
        List<Manager> managers = managerService.findByClientTaxNumber(taxNumber);
        model.addAttribute("managers", managers);
        model.addAttribute("taxNumber", taxNumber);
        return "manager-view/admin-page/global-tax";
    }
    // SEARCH MANAGER BY CLIENT'S BANK ACCOUNT NUMBER
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/global-searching/find/account-number")
    public String findManagerByClientAccountNumber(Model model, @RequestParam(name = "bankAccountNumber", defaultValue = "") String bankAccountNumber) {
        List<Manager> managers = managerService.findByClientBankAccountNumber(bankAccountNumber);
        model.addAttribute("managers", managers);
        model.addAttribute("bankAccountNumber", bankAccountNumber);
        return "manager-view/admin-page/global-accountnumber";
    }
    // SEARCH MANAGER BY MANAGER'S LEVEL
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/global-searching/find/level")
    public String findManagerByLevel(Model model, @RequestParam(name = "level", defaultValue = "") String level) {
        List<Manager> managers = managerService.findByManagerLevel(level);
        model.addAttribute("managers", managers);
        model.addAttribute("level", level);
        return "manager-view/admin-page/global-level";
    }
    // UPDATING MANAGER'S INFO (EXCEPT OF ID NUMBER)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/global-searching/find/change-manager")
    public String findManagerById(Model model,
                                  @RequestParam(name = "managerId", defaultValue = "") Long managerId,
                                  @RequestParam(name = "email", defaultValue = "") String email) {

        findManagerByEmail(model, email);
        Manager manager = managerService.findByEmail(email);
        model.addAttribute("manager", manager);
        return "manager-view/admin-page/global-change";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/change-manager/level")
    public String changeManagerLevel(Model model,
                                     @RequestParam(name = "managerId", defaultValue = "") Long managerId) {
        Manager manager = managerService.findById(managerId);
        System.out.println("Manager ID in change method " + manager.getId());
        model.addAttribute("manager", manager);
        return "manager-view/admin-page/updating-form-level"; // -> сохранить изменения

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/changes-save")
    public String saveManagerChanges(@ModelAttribute("manager") Manager manager) {
        System.out.println("Manager in save method " + manager);
        Long managerId = manager.getId();
        managerService.saveOrUpdateManager(managerId, manager);
        System.out.println(manager);
        return "redirect:/manager/global-searching/find/change-manager?managerId=" + managerId;
    }


}
