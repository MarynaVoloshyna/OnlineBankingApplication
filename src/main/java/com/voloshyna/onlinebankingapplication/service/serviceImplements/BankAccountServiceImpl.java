package com.voloshyna.onlinebankingapplication.service.serviceImplements;

import com.voloshyna.onlinebankingapplication.entity.BankAccount;
import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Currency;
import com.voloshyna.onlinebankingapplication.repository.BankAccountRepository;
import com.voloshyna.onlinebankingapplication.repository.ClientRepository;
import com.voloshyna.onlinebankingapplication.service.interf.BankAccountService;
import com.voloshyna.onlinebankingapplication.staticData.CurrencyRate;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    BankAccountRepository bankAccountRepository;

    /*
    Creating of bank account.
    It's possible to create bank account in three currencies - UAH, USD, EUR.
    Client may create ony one bank account per each currency.
    Total client may have only 3 bank accounts.
    */
    @Override
    public BankAccount createBankAccount(Long clientId, Currency currency) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Integer numbersOfBankAccount = checkingBankAccount(client.getId(), currency);
        if (numbersOfBankAccount == 1) {
            throw new RuntimeException("You already have account in " + currency + " currency");
        }
        BankAccount bankAccount = new BankAccount(client, currency, 0.0, generate16DigitNumber(), LocalDate.now());
        bankAccountRepository.save(bankAccount);
        return bankAccount;
    }


    //Searching of bank account by ID.
    @Override
    public BankAccount findBankAccountById(Long bankAccountId) {
        return bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new EntityNotFoundException("Bank account not found"));
    }


    //Searching of bank account by account number.
    @Override
    public BankAccount findBankAccountByAccountNumber(String accountNumber) {

        BankAccount bankAccount = bankAccountRepository.findBankAccountByAccountNumberContainsIgnoreCase(accountNumber);
        if (bankAccount == null) {
            throw new EntityNotFoundException("Bank Account Not Found");
        }
        return bankAccount;
    }


    // Finding of amount on bank account by client ID and account number
    @Override
    public Double getBankAccountCurrentAmount(Long clientId, String accountNumber) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new EntityNotFoundException("Client not found"));
        List<BankAccount> bankAccounts = client.getAccountList();
        Double currentAmount = 0.0;
        for (BankAccount bankAccount : bankAccounts) {
            if (bankAccount.getAccountNumber().equals(accountNumber)) {
                currentAmount = bankAccount.getCurrentSum();
            }

        }
        return currentAmount;
    }


    // Finding of amount on  all client's bank accounts by client ID
    @Override
    public Double getBankAccountsTotalSum(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new EntityNotFoundException("Client not found"));
        List<BankAccount> bankAccounts = client.getAccountList();
        Double uah = 0.0;
        Double usd = 0.0;
        Double eur = 0.0;
        Double totalSum = 0.0;
        for (BankAccount bankAccount : bankAccounts) {
            if (bankAccount.getCurrency().equals(Currency.USD)) {
                usd = bankAccount.getCurrentSum() * CurrencyRate.getUSD();
            }
            if (bankAccount.getCurrency().equals(Currency.EUR)) {
                eur = bankAccount.getCurrentSum() * CurrencyRate.getEUR();
            }
            if (bankAccount.getCurrency().equals(Currency.UAH)) {
                uah = bankAccount.getCurrentSum();
            }
            totalSum = uah + usd + eur;
        }
        return totalSum;
    }

    // Fetch bank accounts list by client ID
    @Override
    public List<BankAccount> getBankAccountsByClient(Long clientId) {
        List<BankAccount> clientAccounts = bankAccountRepository.findAllByClientId(clientId);
        return clientAccounts;
    }

    // Fetch list of bank accounts by closest matches of account number (for admin role only)
    @Override
    public List<BankAccount> findBankAccountsByAccountNumber(String accountNumber) {
        List<BankAccount> bankAccounts = bankAccountRepository.findBankAccountsByAccountNumberContainsIgnoreCase(accountNumber);
        return bankAccounts;
    }


    //UTIL Methods
    //Checking account
    public Integer checkingBankAccount(Long userId, Currency currency) {
        Integer numbersOfBankAccount = bankAccountRepository.countByClient_IdAndCurrency(userId, currency);
        return numbersOfBankAccount;
    }

    //Generate card number
    public static String generate16DigitNumber() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        int randomNumber = random.nextInt(1000000000);
        long accountNumber = 4441000000000000L + randomNumber;
        String random16Digits = String.valueOf(accountNumber);
        return "4441" + random16Digits.substring(4);
    }
}
