package com.voloshyna.onlinebankingapplication.service.serviceImplements;

import com.voloshyna.onlinebankingapplication.entity.BankAccount;
import com.voloshyna.onlinebankingapplication.entity.Currency;
import com.voloshyna.onlinebankingapplication.entity.Transaction;
import com.voloshyna.onlinebankingapplication.repository.BankAccountRepository;
import com.voloshyna.onlinebankingapplication.repository.TransactionRepository;
import com.voloshyna.onlinebankingapplication.service.interf.TransactionService;
import com.voloshyna.onlinebankingapplication.staticData.CurrencyRate;
import com.voloshyna.onlinebankingapplication.staticData.TransactionType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Value("${spring.maxUAH}")
    private Double maxUAH;
    @Value("${spring.maxForeignCurrency}")
    private Double maxForeignCurrency;


    @Override
    public Transaction primaryTransaction(String accountNumber, Double amount) {
        BankAccount bankAccount = bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase(accountNumber);
        Double currentSum = bankAccount.getCurrentSum();
        Currency currency = bankAccount.getCurrency();
        if(currentSum > 0){
            throw new IllegalArgumentException("You can't make primary transaction. You already has money on your balance. " +
                    "The sum is " + currentSum);
        }
        if(currency.equals(Currency.UAH) && amount > maxUAH){
            throw new IllegalArgumentException("Max sum of replenishment is 1000 UAH ");
        }
        if((currency.equals(Currency.USD) || currency.equals(Currency.EUR)) && amount > maxForeignCurrency){
            throw new IllegalArgumentException("Max sum of replenishment is " +maxForeignCurrency + "  USD or EUR ");
        }

        Transaction transaction = new Transaction(bankAccount, amount, LocalDateTime.now(), TransactionType.REPLENISHMENT);
        bankAccount.setCurrentSum(amount);
        transactionRepository.save(transaction);
        bankAccountRepository.save(bankAccount);
        return transaction;
    }

    @Override
    public Transaction findTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(()-> new EntityNotFoundException("Transaction not found"));
        return transaction;
    }


    ////?????????////
//    @Override
//    public Transaction makeTransaction(BankAccount senderAccount, BankAccount recipientAccount, Double amount) {
//        Double senderBalance = senderAccount.getCurrentSum();
//        Double recipientBalance = recipientAccount.getCurrentSum();
//
//        if(senderAccount.getAccountNumber().equals(recipientAccount.getAccountNumber())){
//            throw new IllegalArgumentException("It's not allowed to send money by yourself :)");
//        }
//
//        if (recipientAccount.equals(null)){
//            throw new EntityNotFoundException("Account not found");
//        }
//
//        if (senderBalance < amount) {
//           throw new IllegalArgumentException("Not enough money in your balance");
//        }
//
//        Double newSenderBalance = senderBalance - amount;
//        senderAccount.setCurrentSum(newSenderBalance);
//        Transaction transactionWithdraw = new Transaction(senderAccount, amount, new Date(),TransactionType.WITHDRAW);
//        transactionRepository.save(transactionWithdraw);
//        bankAccountRepository.save(senderAccount);
//
//        Double transactionAmount = calculateTransactionSum(senderAccount,recipientAccount, amount );
//        recipientAccount.setCurrentSum(recipientBalance + transactionAmount);
//        Transaction transactionDeposit = new Transaction(recipientAccount, transactionAmount, new Date(), TransactionType.DEPOSIT);
//        transactionDeposit.setAccountTo(recipientAccount); // Assign recipientAccount to AccountTo field
//
//        bankAccountRepository.save(recipientAccount);
//        transactionRepository.save(transactionDeposit);
//        return transactionWithdraw;
//    }
    public Transaction makeTransaction(BankAccount accountFrom, BankAccount accountTo, Double amount) {
        if(accountFrom.getAccountNumber().equals(accountTo.getAccountNumber())){
            throw new IllegalArgumentException("It's not allowed to send money by yourself :)");
        }

        // Check if accountFrom and accountTo are not null
        if (accountFrom == null || accountTo == null) {
            throw new IllegalArgumentException("Invalid bank accounts");
        }

        // Check if accountFrom has sufficient balance
        if (accountFrom.getCurrentSum() < amount) {
            throw new IllegalArgumentException("Insufficient balance in the account");
        }

        // Perform the transaction
        Double newFromBalance = accountFrom.getCurrentSum() - amount;
        accountFrom.setCurrentSum(newFromBalance);

//        Double newToBalance = accountTo.getCurrentSum() + amount;
        Double transactionSum = calculateTransactionSum(accountFrom, accountTo, amount);
        Double newToBalance = accountTo.getCurrentSum() + transactionSum;
        accountTo.setCurrentSum(newToBalance);



        Date transactionDate = new Date();
        TransactionType transactionType = TransactionType.REPLENISHMENT;

        // Create the transaction objects for both accounts
        Transaction transactionFrom = new Transaction(accountFrom,amount, LocalDateTime.now(), TransactionType.WITHDRAW, accountTo);
        Transaction transactionTo = new Transaction(accountTo, transactionSum,  LocalDateTime.now(), TransactionType.DEPOSIT,  accountFrom);

        // Set the transactions for the accounts
        accountFrom.getTransactionListFrom().add(transactionFrom);
        accountTo.getTransactionListTo().add(transactionTo);

        // Save the transactions and update the accounts
        transactionRepository.save(transactionFrom);
        transactionRepository.save(transactionTo);

        return transactionFrom;
    }
//    @Override
//    public Transaction makeTransaction(BankAccount senderAccount, BankAccount recipientAccount, Double amount) {
//        Double senderBalance = senderAccount.getCurrentSum();
//        Double recipientBalance = recipientAccount.getCurrentSum();
//
//        if (senderAccount.getAccountNumber().equals(recipientAccount.getAccountNumber())) {
//            throw new IllegalArgumentException("It's not allowed to send money to yourself :)");
//        }
//
//        if (recipientAccount == null) {
//            throw new EntityNotFoundException("Recipient account not found");
//        }
//
//        if (senderBalance < amount) {
//            throw new IllegalArgumentException("Not enough money in your balance");
//        }
//
//        Double newSenderBalance = senderBalance - amount;
//        senderAccount.setCurrentSum(newSenderBalance);
//        Transaction transactionWithdraw = new Transaction(senderAccount, amount, new Date(), TransactionType.WITHDRAW);
//        transactionRepository.save(transactionWithdraw);
//        bankAccountRepository.save(senderAccount);
//
//        // ...
//        Double transactionAmount = calculateTransactionSum(senderAccount, recipientAccount, amount);
//        recipientAccount.setCurrentSum(recipientBalance + transactionAmount);
//        Transaction transactionDeposit = new Transaction(senderAccount, amount, new Date(), TransactionType.DEPOSIT,  recipientAccount); // Use the correct constructor
//        transactionDeposit.setAccountTo(recipientAccount); // Assign recipientAccount to AccountTo field
//
//        bankAccountRepository.save(recipientAccount);
//        transactionRepository.save(transactionDeposit);
//        return transactionWithdraw;
//// ...
//
//    }


//    @Override
//    public Transaction makeTransaction(BankAccount senderAccount, BankAccount recipientAccount, Double amount) {
//        // Validate inputs
//        if (senderAccount == null || recipientAccount == null) {
//            throw new IllegalArgumentException("Invalid sender or recipient account");
//        }
//
//        if (senderAccount.getAccountNumber().equals(recipientAccount.getAccountNumber())) {
//            throw new IllegalArgumentException("It's not allowed to send money to yourself");
//        }
//
//        if (senderAccount.getCurrentSum() < amount) {
//            throw new IllegalArgumentException("Not enough money in your balance");
//        }
//
//        // Perform the transaction
//        Transaction transaction = new Transaction();
//        transaction.setTransactionDate(new Date());
//
//        // Withdrawal
//        transaction.setAccountFrom(senderAccount);
//        transaction.setTransactionSum(amount);
//        transaction.setTransactionType(TransactionType.WITHDRAW);
//        transactionRepository.save(transaction);
//
//        Double newSenderBalance = senderAccount.getCurrentSum() - amount;
//        senderAccount.setCurrentSum(newSenderBalance);
//        bankAccountRepository.save(senderAccount);
//
//        // Deposit
//       // transaction.setAccountFrom(null);  // Reset account from
//        transaction.setAccountTo(recipientAccount);
//        Double transactionAmount = calculateTransactionSum(senderAccount, recipientAccount, amount);
//        transaction.setTransactionSum(transactionAmount);
//        transaction.setTransactionType(TransactionType.DEPOSIT);
//        transactionRepository.save(transaction);
//
//        recipientAccount.setCurrentSum(recipientAccount.getCurrentSum() + transactionAmount);
//        bankAccountRepository.save(recipientAccount);
//
//        return transaction;
//    }




    @Override
    public List<Transaction> getAllTransactionByBankAccount(BankAccount bankAccount) {
        String accountNumber = bankAccount.getAccountNumber();
        List<Transaction> transactions = transactionRepository.findAllByAccountFrom_AccountNumberContains(accountNumber);
        return transactions;

    }

    @Override
    public List<Transaction> transactionsHistoryByBankAccountNumber(String bankAccountNumber) {
        return transactionRepository.findAllByAccountFrom_AccountNumberContains(bankAccountNumber);
    }

    @Override
    public List<Transaction> getAllTransactionByClient(Long clientId) {
        List<Transaction> transactions = transactionRepository.findTransactionsByClient(clientId);
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionBySum(Long clientId, Double amount) {
        List<Transaction> transactions = getAllTransactionByClient(clientId);
        List<Transaction> transactionsByAmount = new ArrayList<>();
        for (Transaction transaction: transactions) {
            if(transaction.getTransactionSum().equals(amount)){
                transactionsByAmount.add(transaction);
            }
        }
        return transactionsByAmount;
    }
    @Override
    public List<Transaction> getAllTransactionByTransactionType(Long clientId, TransactionType transactionType) {
        List<Transaction> allTransactionByClient = getAllTransactionByClient(clientId);
        List<Transaction> transactionsByType = new ArrayList<>();
        for (Transaction transaction: allTransactionByClient) {
            if(transaction.getTransactionType().equals(transactionType)){
                transactionsByType.add(transaction);
            }
        }
        return transactionsByType;
    }



    @Override
    public List<Transaction> findTransactionsByTransactionDateBetweenAndClientId(LocalDateTime startDate, LocalDateTime endDate, Long clientId) {
        List<Transaction> transactions = transactionRepository.findTransactionsByTransactionDateBetweenAndClientId(startDate,endDate, clientId );
        return transactions;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions;
    }

    @Override
    public List<Transaction> findTransactionsByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findTransactionsByTransactionDateBetween(startDate, endDate);
    }

    @Override
    public List<Transaction> findTransactionsByPeriodAndBankAccountId(LocalDateTime startDate, LocalDateTime endDate, Long bankAccountId) {
        return transactionRepository.findTransactionsByTransactionDateBetweenAndAccountId(startDate, endDate, bankAccountId);
    }

    @Override
    public List<Transaction> findTransactionsByRecipientBankAccountNumber(String bankAccountNumber) {
        return transactionRepository.findTransactionByAccountToContains(bankAccountNumber);
    }


    @Override
    public List<Transaction> getTransactionByPeriod(LocalDateTime startDate, LocalDateTime endDate, Long clientId) {
         List<Transaction> transactions = transactionRepository.findTransactionsByTransactionDateBetweenAndClientId(startDate, endDate, clientId);
        return transactions;
    }


    //UTIL methods
    public Currency checkCurrency(BankAccount bankAccount){
        Currency currency = bankAccount.getCurrency();
        return currency;
    }

    public Double calculateTransactionSum(BankAccount senderAccount, BankAccount recipientAccount, Double amount){
        Double transactionAmount = 0.0;

        Double senderBalance = senderAccount.getCurrentSum();
        Double recipientBalance = recipientAccount.getCurrentSum();

        Currency senderCurrency = senderAccount.getCurrency();
        Currency recipientCurrency = recipientAccount.getCurrency();
        if(senderCurrency.equals(recipientCurrency)){
            transactionAmount = amount;
        }

        if(senderCurrency.equals(Currency.USD) && recipientCurrency.equals(Currency.UAH)){
            transactionAmount = amount* CurrencyRate.getUSD();
        }
        if(senderCurrency.equals(Currency.EUR) && recipientCurrency.equals(Currency.UAH)){
            transactionAmount = amount* CurrencyRate.getEUR();
        }
        if(senderCurrency.equals(Currency.USD) && recipientCurrency.equals(Currency.EUR)){
            transactionAmount = amount* CurrencyRate.getCrossRate();
        }
        if(senderCurrency.equals(Currency.EUR) && recipientCurrency.equals(Currency.USD)){
            transactionAmount = amount/ CurrencyRate.getCrossRate();
        }
        if(senderCurrency.equals(Currency.UAH) && recipientCurrency.equals(Currency.USD)){
            transactionAmount = amount/ CurrencyRate.getUSD();
        }
        if(senderCurrency.equals(Currency.UAH) && recipientCurrency.equals(Currency.EUR)){
            transactionAmount = amount/ CurrencyRate.getEUR();
        }

        return transactionAmount;
    }
}
