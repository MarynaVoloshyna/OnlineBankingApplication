package com.voloshyna.onlinebankingapplication.helpers;

import com.voloshyna.onlinebankingapplication.entity.*;
import com.voloshyna.onlinebankingapplication.repository.*;
import com.voloshyna.onlinebankingapplication.entity.Currency;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Utility {
    public static void userOperations(UserRepository userRepository){
       createUser(userRepository);
      // updateUser(userRepository);
       // deleteUser(userRepository);
    // fetchUsers(userRepository);

    }

    public static void rolesOperations(RoleRepository roleRepository, UserRepository userRepository){
        createRole(roleRepository);
       //updateRole(roleRepository);
       // deleteRole(roleRepository);
        //setRoleToUser(userRepository, roleRepository);
       fetchRole(roleRepository);
    }

    public static void clientOperations(ClientRepository clientRepository, UserRepository userRepository, ManagerRepository managerRepository){
        //createClient(clientRepository, userRepository, managerRepository);
       //updateClient(clientRepository);
       // deleteClient(clientRepository,managerRepository);
         //fetchClient(clientRepository); // не работает
        fetchClientByManager(clientRepository, managerRepository);

    }

    private static void fetchClientByManager(ClientRepository clientRepository, ManagerRepository managerRepository) {
        Manager manager = managerRepository.findManagerByLevelIgnoreCase("level1");
        List<Client> clients = clientRepository.findAllByManager(manager);
        for (Client client:clients) {
            System.out.println(client);
        }
    }

    public static void managerOperations(ManagerRepository managerRepository, UserRepository userRepository){
        //createManager(managerRepository, userRepository);
        //updateManager(managerRepository);
        //deleteManager(managerRepository);
        fetchManager(managerRepository);
    }

    public static void bankAccountOperations(ClientRepository clientRepository, BankAccountRepository bankAccountRepository){
        createBankAccount(clientRepository, bankAccountRepository);
      // updateBankAccount(bankAccountRepository);
        //readBankAccount(bankAccountRepository);
        findBankAccount(bankAccountRepository);

    }

    private static void findBankAccount(BankAccountRepository bankAccountRepository) {
        Integer integer = bankAccountRepository.countByClient_IdAndCurrency(1L, Currency.UAH);
        System.out.println(integer);
    }


    //USERS
    private static void createUser(UserRepository userRepository) {
        User user1 = new User("user1@gmail.com", "pass1");
        userRepository.save(user1);
        User user2 = new User("user2@gmail.com", "pass2");
        userRepository.save(user2);
        User user3 = new User("user3@gmail.com", "pass3"); //manager
        userRepository.save(user3);
        User user4 = new User("user4@gmail.com", "pass4"); //manager
        userRepository.save(user4);
        User user5 = new User("user5@gmail.com", "pass5");
        userRepository.save(user5);
    }
    private static void updateUser(UserRepository userRepository) {
        User user = userRepository.findById(5L).orElseThrow(()->new EntityNotFoundException("No such user"));
        user.setEmail("newUser5email@gmail.com");
        userRepository.save(user);

    }
    private static void deleteUser(UserRepository userRepository) {
        User user = userRepository.findById(5L).orElseThrow(()->new EntityNotFoundException("No such user"));
        userRepository.delete(user);
       // userRepository.deleteById(4L); same as finding user by id, then delete it
    }
    private static void fetchUsers(UserRepository userRepository) {
         List <User> users = userRepository.findAll();
        for (User user:users) {
            System.out.println(user);
        }
    }

    //ROLES
    private static void createRole(RoleRepository roleRepository) {
        Role roleAdmin = new Role("ADMIN");
        roleRepository.save(roleAdmin);
        Role roleManager = new Role("MANAGER");
        roleRepository.save(roleManager);
        Role roleUser = new Role("USER");
        roleRepository.save(roleUser);

    }
    private static void updateRole(RoleRepository roleRepository) {
        Role role = roleRepository.findRoleByNameIgnoreCase("ADMIN");
        if (role == null){
            throw new EntityNotFoundException("Role does not found");
        }
        role.setName("NEW ADMIN");
        roleRepository.save(role);
    }

    private static void setRoleToUser(UserRepository userRepository, RoleRepository roleRepository) {

        Role role = roleRepository.findRoleByNameIgnoreCase("USER");
        if (role == null){
            throw new EntityNotFoundException("Role does not found");
        }
        User user = userRepository.findUserByEmail("user1@gmail.com");
        if (user == null){
            throw new EntityNotFoundException("User does not found");
        }
        user.setRole(role);
        userRepository.save(user);
    }

    private static void deleteRole(RoleRepository roleRepository) {
       Role role = roleRepository.findRoleByNameIgnoreCase("NEW ADMIN");
        roleRepository.delete(role);
    }

    private static void fetchRole(RoleRepository roleRepository) {
       roleRepository.findAll().forEach(role -> System.out.println(role.toString()));
    }


    //CLIENT
    private static void createClient(ClientRepository clientRepository, UserRepository userRepository, ManagerRepository managerRepository) {
        User user = userRepository.findUserByEmail("user1@gmail.com");
        User user1 = userRepository.findUserByEmail("user2@gmail.com");
        Manager manager = managerRepository.findManagerByEmail("user3@gmail.com");
        Client client1 = new Client("Name1", "LastName1", LocalDate.now(), 3344L, user, manager );
        clientRepository.save(client1);
        Client client2 = new Client("Name2", "LastName2", LocalDate.now(), 3334L, user1, manager );
        clientRepository.save(client2);
    }

    private static void updateClient(ClientRepository clientRepository) {
        Client client = clientRepository.findById(1L).orElseThrow(()-> new EntityNotFoundException("Not found"));
        client.setTaxNumber(77L);
        clientRepository.save(client);
    }

    private static void deleteClient(ClientRepository clientRepository, ManagerRepository managerRepository) {
        Manager manager = managerRepository.findManagerByEmail("user3@gmail.com");
        Client client = clientRepository.findClientByManager(manager);
        clientRepository.delete(client);
    }
    private static void fetchClient(ClientRepository clientRepository) {
        clientRepository.findAll().forEach(client -> System.out.println(client));
    }

    //MANAGER

    private static void createManager(ManagerRepository managerRepository, UserRepository userRepository) {
        User user1 = userRepository.findUserByEmail("user3@gmail.com");
        User user2 = userRepository.findUserByEmail("user4@gmail.com");
        Manager manager1 = new Manager("ManagerFirstName1", "ManagerLastName1", "Level1", user1 );
        managerRepository.save(manager1);
        Manager manager2 = new Manager("ManagerFirstName2", "ManagerLastName2", "Level2", user2 );
        managerRepository.save(manager2);
    }
    private static void updateManager(ManagerRepository managerRepository) {
        Manager manager = managerRepository.findManagerByLevelIgnoreCase("Level1");
        manager.setLevel("Level2");
        managerRepository.save(manager);
    }
    private static void deleteManager(ManagerRepository managerRepository) {
        Manager manager = managerRepository.findManagerByEmail("user4@gmail.com");
        managerRepository.delete(manager);

    }
    private static void fetchManager(ManagerRepository managerRepository) {
        managerRepository.findAll().forEach(manager-> System.out.println(manager));

    }

    //BANK ACCOUNT
    private static void readBankAccount(BankAccountRepository bankAccountRepository){
//        Date date = new Date();
//        date.getTime();
        List<BankAccount> bankAccounts = bankAccountRepository.findBankAccountByClient_TaxNumber(3344L);
        for (BankAccount account: bankAccounts) {
            System.out.println(account);
        }
    }

    private static void createBankAccount(ClientRepository clientRepository, BankAccountRepository bankAccountRepository) {
        Client client = clientRepository.findClientByEmail("user1@gmail.com");
        BankAccount bankAccount1 = new BankAccount(client,Currency.UAH, 0.0, generate16DigitNumber(),LocalDate.now());
        bankAccountRepository.save(bankAccount1);
        BankAccount bankAccount2 = new BankAccount(client,Currency.USD, 0.0, generate16DigitNumber(), LocalDate.now());
        bankAccountRepository.save(bankAccount2);
        BankAccount bankAccount3 = new BankAccount(client,Currency.EUR, 0.0, generate16DigitNumber(), LocalDate.now());
        bankAccountRepository.save(bankAccount3);
    }
    private static void updateBankAccount(BankAccountRepository bankAccountRepository){
        BankAccount bankAccount = bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase("8326");
        bankAccount.setCurrentSum(10.0);
        bankAccountRepository.save(bankAccount);
    }
    public static String generate16DigitNumber() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            builder.append(random.nextInt(10));
        }
        String random16Digits = builder.toString();
        return random16Digits;
    }

    //TRANSACTION
    private static void createTransaction(BankAccountRepository bankAccountRepository,
                                          TransactionRepository transactionRepository){
        Double transferSum = 5.0;

        BankAccount bankAccountFrom = bankAccountRepository.findBankAccountByCurrency(Currency.UAH);
        BankAccount bankAccountTo  = bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase("993141");
        if(bankAccountTo.getCurrency().equals(Currency.UAH)){
            transferSum = transferSum;
        }
        if(bankAccountTo.getCurrency().equals(Currency.USD)){
            transferSum = transferSum/2;
        }
        if(bankAccountTo.getCurrency().equals(Currency.EUR)){
            transferSum = transferSum/3;
        }

        Double senderBalance = bankAccountFrom.getCurrentSum()-transferSum;
        Double recepientBalance = bankAccountTo.getCurrentSum()+transferSum;
        Transaction transaction = new Transaction(bankAccountFrom, bankAccountTo,transferSum, LocalDateTime.now());
        bankAccountFrom.setCurrentSum(senderBalance);
        bankAccountRepository.save(bankAccountFrom);
        bankAccountTo.setCurrentSum(recepientBalance);
        bankAccountRepository.save(bankAccountTo);
        transactionRepository.save(transaction);

    }
    private static void fetchTransaction(TransactionRepository transactionRepository){
        List<Transaction> transactions = transactionRepository.findAllByAccountFrom_AccountNumberContains("8574");
        for (Transaction transaction:transactions) {
            System.out.println(transaction);
        }
    }

    public static void transactionOperation(BankAccountRepository bankAccountRepository,
                                            TransactionRepository transactionRepository){
        //createTransaction(bankAccountRepository, transactionRepository);
        fetchTransaction(transactionRepository);
    }
}
