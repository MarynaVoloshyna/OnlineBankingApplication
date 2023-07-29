package com.voloshyna.onlinebankingapplication.web;

import com.voloshyna.onlinebankingapplication.dto.ClientDTO;
import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Manager;
import com.voloshyna.onlinebankingapplication.entity.User;
import com.voloshyna.onlinebankingapplication.repository.UserRepository;
import com.voloshyna.onlinebankingapplication.service.interf.BankAccountService;
import com.voloshyna.onlinebankingapplication.service.interf.ClientService;
import com.voloshyna.onlinebankingapplication.service.interf.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Controller
@RequestMapping("/home")
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;

    @GetMapping("/registration")
    public String createClient(Model model){
        model.addAttribute("client", new Client());
        return "registration-login";
    }

    @PostMapping("/save")
    public String saveClient(@Valid Client client, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        User user = userService.getUserByEmail(client.getUser().getEmail());
        if(user != null){bindingResult.rejectValue("user.email", null,
                "Account with this email already registered");}
        if(bindingResult.hasErrors()){return "client-view/registration";}
        clientService.createClient(
                client.getFirstName(),
                client.getLastName(),
                LocalDate.now(),
                client.getTaxNumber(),
                client.getUser().getEmail(),
                client.getUser().getPassword());
        model.addAttribute("successMessage", "You successfully registered to our awesome app!");

        return "redirect:/login";
    }



    @GetMapping("/bank-account")
    public String showClientBankAccount(Model model, Authentication authentication){
        String userEmail = authentication.getName();
        User user = userService.getUserByEmail(userEmail);
        Client client = user.getClient();
        model.addAttribute("listBankAccount", client.getAccountList());
        model.addAttribute("clientName", client.getFirstName());
        return "client-view/accounts";
    }
    @PostMapping("/custom-login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {

        User user = userService.getUserByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            // Неверный email или пароль, выполните необходимую логику (например, отобразите сообщение об ошибке в модели)
            model.addAttribute("error", "Invalid email or password");
            return "redirect:/"; // Вернуть имя представления для отображения
        }

        // Аутентификация прошла успешно, выполните необходимую логику (например, сохраните информацию о текущем пользователе в сессии)

        return "redirect:/home/dashboard"; // Перенаправление пользователя на страницу успешного входа или другую страницу

    }
    @PostMapping("/dashboard")
    public String showClientDashboard(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);
        model.addAttribute("username", user.getClient().getFirstName());
        model.addAttribute("userId", user.getUserId());
        return "client-view/client-dashboard";
    }
    @GetMapping("/client-info")
    public String showClientInfo(Model model, Authentication authentication){
        String userEmail = authentication.getName();
        Long clientId = userService.getUserByEmail(userEmail).getClient().getId();
        model.addAttribute("client", clientService.findClientById(clientId));
        return"client-view/client-info";
    }
    @PostMapping("/save-updating")
    public String saveUpdating(Client client, Authentication authentication, Model model){
        String userEmail = authentication.getName();
        User user = userService.getUserByEmail(userEmail);
        model.addAttribute("user", user);
//       Long taxNumber =  client.getTaxNumber();
//       client.setTaxNumber(taxNumber);
        clientService.updateClient(client);
        return "redirect:/client-info";
    }

    @GetMapping ("/manager-info")
    public String showManagerInfo(Model model, Authentication authentication ){
    String userEmail = authentication.getName();
    User user = userService.getUserByEmail(userEmail);
    Manager manager = user.getClient().getManager();
    if(manager == null){
        model.addAttribute("manager", null);
    }
        model.addAttribute("manager", manager);
        return "client-view/manager-info";

    }
}
