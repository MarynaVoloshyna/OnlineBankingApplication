package com.voloshyna.onlinebankingapplication.web;

import com.voloshyna.onlinebankingapplication.entity.BankAccount;
import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Transaction;
import com.voloshyna.onlinebankingapplication.entity.User;
import com.voloshyna.onlinebankingapplication.service.interf.BankAccountService;
import com.voloshyna.onlinebankingapplication.service.interf.ClientService;
import com.voloshyna.onlinebankingapplication.service.interf.TransactionService;
import com.voloshyna.onlinebankingapplication.service.interf.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.io.IOException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;


@AllArgsConstructor
@Controller
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;
    private final ClientService clientService;
    private final UserService userService;

    private HttpServletResponse httpServletResponse;

    @GetMapping("/replenishment")
    public String showTransactionForm(Model model, @RequestParam(value = "error", required = false) String error, Authentication authentication) {
        String userEmail = authentication.getName();
        User user = userService.getUserByEmail(userEmail);
        Client client = user.getClient();
        Long clientId = client.getId();



        model.addAttribute("bankAccounts", client.getAccountList());
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("errorMessage", error); // Add the error message to the model

        return "transaction-view/primary-form";
    }

    @PostMapping("/transfer")
    public String transferMoney(@Valid Transaction transaction, BindingResult bindingResult, Model model,
                                @RequestParam("accountNumber") String accountNumber,
       @RequestParam("amount") Double amount) throws IOException {
        if (bindingResult.hasErrors()) {
            return "redirect:/transactions/replenishment";
        }
        try {
            transactionService.primaryTransaction(accountNumber.substring(0,15),amount);
            return "redirect:/transactions/all";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "redirect:/transactions/replenishment?error=" + ex.getMessage();


        }
    }
    @GetMapping("/withdraw")
    public String showWithdrawForm(Model model, @RequestParam(value = "error", required = false) String error, Authentication authentication){

        String userEmail = authentication.getName();
        User user = userService.getUserByEmail(userEmail);
        Client client = user.getClient();
        Long clientId = client.getId();

        model.addAttribute("bankAccounts", client.getAccountList());
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("errorMessage", error); // Add the error message to the model

        return "transaction-view/transaction-form";
    }
    @PostMapping("/makeTransfer")
    public String transferBetweenAccounts(@Valid Transaction transaction, BindingResult bindingResult, Model model,
                                          @RequestParam("accountNumber") String accountNumber,
                                          @RequestParam("recipientAccountNumber") String recipientAccountNumber,
                                          @RequestParam("amount") Double amount) throws IOException {
        BankAccount senderBankAccount = bankAccountService.findBankAccountByAccountNumber(accountNumber.substring(0,15));
        BankAccount recipientBankAccount = bankAccountService.findBankAccountByAccountNumber(recipientAccountNumber);
        if (bindingResult.hasErrors()) {
            return "redirect:/transactions/withdraw";
        }
        try {
            transactionService.makeTransaction(senderBankAccount,recipientBankAccount,amount);
            model.addAttribute("accountNumber", accountNumber);
            model.addAttribute("recipientAccountNumber", recipientAccountNumber);
            return "redirect:/transactions/all";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "redirect:/transactions/withdraw?error=" + ex.getMessage();


        }
    }
    // Transaction list by bank account number
    @PostMapping("/history")
    public String showTransactionHistory(@RequestParam(value = "bankAccountNumber") String bankAccountNumber,
                                         Model model){
        List <Transaction> transactions = transactionService.transactionsHistoryByBankAccountNumber(bankAccountNumber);
        BankAccount bankAccount = bankAccountService.findBankAccountByAccountNumber(bankAccountNumber);
        model.addAttribute("bankAccount", bankAccount);
        model.addAttribute("bankAccountNumber", bankAccountNumber);
        model.addAttribute("transactions", transactions);
        return "transaction-view/transaction-history";
    }

    @GetMapping("/all")
    public String showAllTransactions(
            @RequestParam (value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDate startDate,
            @RequestParam (value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDate endDate,
            Model model,
            Authentication authentication){


        String userEmail = authentication.getName();
        User user = userService.getUserByEmail(userEmail);
        Client client = user.getClient();
        Long clientId = client.getId();
        List<Transaction> transactions;


        transactions = transactionService.getAllTransactionByClient(clientId);
            if(startDate != null && !startDate.equals("") && endDate != null && !endDate.equals("")){
                LocalTime startTime = LocalTime.MIN; // Початок дня
                LocalTime endTime = LocalTime.MAX; // Кінець дня
                LocalDateTime startDateTime =LocalDateTime.of(startDate, LocalTime.MIN);
                LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
                transactions = transactionService.getTransactionByPeriod(startDateTime, endDateTime, clientId);
            }



        Collections.sort(transactions, Comparator.comparing(Transaction::getTransactionDate));

        model.addAttribute("transactions", transactions);
        if(user.getRole().getId() == 1L || user.getRole().getId() == 2L ){return"transaction-view/transactions-list";}
        return "client-view/transactions";


    }

@GetMapping("/downloadReceipt")
public void downloadReceipt(HttpServletResponse response, @RequestParam ("transactionId") Long transactionId) {
        Transaction transaction = transactionService.findTransactionById(transactionId);

    try {
        // Generate PDF receipt
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        // Set starting position for text and image
        float startX = 150;
        float startY = page.getMediaBox().getHeight() - 150;
        float imageX = 150;
        float imageY = page.getMediaBox().getHeight() - 550;

        // Load the image file
        PDImageXObject logoImage = PDImageXObject.createFromFile("/Users/maryna/Desktop/OnlineBankingApplication/src/main/resources/static/image/logoYellow.png", document);


        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 700);
        contentStream.showText("Transaction Receipt");
        contentStream.newLineAtOffset(0, -20);
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.showText("Transaction ID: " + transaction.getId());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Account Number: " + transaction.getAccountFrom().getAccountNumber());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Amount: " + transaction.getTransactionSum());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Date: " + transaction.getTransactionDate());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Type: " + transaction.getTransactionType());
        contentStream.endText();
        contentStream.drawImage(logoImage, imageX, imageY, logoImage.getWidth(), logoImage.getHeight());

        contentStream.close();
        httpServletResponse.setContentType("application/pdf");
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=transaction_receipt.pdf");
        document.save(httpServletResponse.getOutputStream());
        document.close();

        } catch (IOException e) {
        e.printStackTrace();
    }
}

}

