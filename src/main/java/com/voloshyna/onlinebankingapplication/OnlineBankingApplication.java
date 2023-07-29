package com.voloshyna.onlinebankingapplication;

import com.voloshyna.onlinebankingapplication.dto.UserRegistrationDTO;
import com.voloshyna.onlinebankingapplication.entity.*;
import com.voloshyna.onlinebankingapplication.helpers.Utility;
import com.voloshyna.onlinebankingapplication.repository.*;
import com.voloshyna.onlinebankingapplication.service.serviceImplements.*;
import com.voloshyna.onlinebankingapplication.staticData.TransactionType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SpringBootApplication
public class OnlineBankingApplication implements CommandLineRunner {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private ManagerRepository managerRepository;
//    @Autowired
//    private ClientRepository clientRepository;
//    @Autowired
//    private BankAccountRepository bankAccountRepository;
//    @Autowired
//    private TransactionRepository transactionRepository;
//    @Autowired
//    private UserRegistrationDTO userRegistrationDTO;
//    @Autowired
//    private UserServiceImpl service;
//    @Autowired
//    private BankAccountServiceImpl bankAccountService;
//    @Autowired
//    private TransactionServiceImpl transactionService;
//    @Autowired
//    private ClientServiceImpl clientService;
//    @Autowired
//    private UserServiceImpl userService;
    @Autowired
    private ManagerServiceImpl managerService;


    public static void main(String[] args) {
        SpringApplication.run(OnlineBankingApplication.class, args);

    }


    @Override
    public void run(String... args) throws Exception {
//       managerService.createManager("i","i", "i", "manager2_muytrioi@friendlybank.com", "11");
//        clientService.changeClientManager("jhgf@MAIL.COM", "i@friendlybank.com");

       // Utility.userOperations(userRepository);
        //Utility.rolesOperations(roleRepository, userRepository);
      //Utility.managerOperations(managerRepository,userRepository);
       //Utility.clientOperations(clientRepository, userRepository, managerRepository);
       //Utility.bankAccountOperations(clientRepository, bankAccountRepository);
        //Utility.transactionOperation(bankAccountRepository, transactionRepository);
        //UserRegistrationDTO userRegistrationDTO1 = new UserRegistrationDTO("dto@mail.com", "dtopassword");
       // UserServiceImpl userService = new UserServiceImpl(userRepository, userRegistrationDTO1);
        //userService.registrateUser("11111111111111@friendlybank.com", "111111");
       // userService.setRoleToUser("nnnnnn@gmail.com");

        //bankAccountService.createBankAccount(2L, Currency.USD );
//        transactionService.makeTransaction(bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase("2479758326928574"),
//                bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase("4644998249602325"),
//                "4644998249602325", 10.0);
        //transactionService.primaryTransaction("2479758326928574", 500.0);
//        BankAccount account = bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase("2479758326928574");
//        Client client = clientRepository.findById(1L).orElseThrow(()-> new EntityNotFoundException("NOT FOUND"));
//        List<Transaction> transactions = transactionService.getAllTransactionByClient(1L);
//        for (Transaction transaction:
//                transactions) {
//            System.out.println(transaction);
//        }
        //User user = userRepository.findUserByEmail("user5@gmail.com");
        //Manager manager = managerRepository.findManagerByEmail("user4@gmail.com");
       // managerService.createManager("manager", "manager", "firstLevel", "n@friendlybank.com");
        //clientService.createClient("Mae", "Vol", new Date(), 8792L, "nn@gmail.com");

       // bankAccountService.createBankAccount(7L, Currency.USD );
       // bankAccountService.createBankAccount(7L, Currency.UAH );
        //bankAccountService.createBankAccount(7L, Currency.EUR );
        //transactionService.primaryTransaction("4441000759667945", 100.0);
//        transactionService.makeTransaction(bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase("4441000759667945"),
//               bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase("4441000297662400"),
//                10.0);
//        Double sum = transactionService.calculateTransactionSum(bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase("4441000485120120"),
//        bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase("4441000297662400"),
//        400.0);
//        System.out.println(sum);
//        List <Transaction> transactions1 = transactionService.getAllTransactionByTransactionType(3L, TransactionType.REPLENISHMENT);
//        for (Transaction transaction:
//                transactions1) {
//            System.out.println(transaction);
//        }

//        List <Transaction> transactions1 = transactionService.getTransactionBySum(3L, 500.0);
//        for (Transaction transaction: transactions1) {
//            System.out.println(transaction);
//        }
        //userService.setRoleToUser("user2@gmail.com", "manager");
//       List<User> users = userService.getUsersByRole("manager");
//       for(User user:users){
//           System.out.println(user.getEmail());
//       }
      //  Manager manager = clientService.findFreeManager();
      //  System.out.println(manager);
       // System.out.println( managerService.getAllManager());
       // System.out.println(managerRepository.findManagerByEmail("user3@gmail.com").getClientList().size());
        //clientService.changeClientManager("nn@gmail.com", "user3@gmail.com");



//        managerService.setClientToManager("n@friendlybank.com", "dto@mail.com");
//        List <Client> clients = clientService.getAllClientsByManager("n@friendlybank.com");
//        for(Client client: clients){
//            System.out.println(client.getTaxNumber() + "  " + client.getLastName());
//        }

//        Double sum = bankAccountService.getBankAccountsTotalSum(7L );
//        System.out.println(sum);
    }
}
