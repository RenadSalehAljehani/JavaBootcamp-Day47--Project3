package com.example.bankmanagementsystemproject3.Service;

import com.example.bankmanagementsystemproject3.Api.ApiException;
import com.example.bankmanagementsystemproject3.InDTO.AccountInDTO;
import com.example.bankmanagementsystemproject3.Model.Account;
import com.example.bankmanagementsystemproject3.Model.Customer;
import com.example.bankmanagementsystemproject3.Model.Employee;
import com.example.bankmanagementsystemproject3.OutDTO.AccountOutDTO;
import com.example.bankmanagementsystemproject3.Repository.AccountRepository;
import com.example.bankmanagementsystemproject3.Repository.AuthRepository;
import com.example.bankmanagementsystemproject3.Repository.CustomerRepository;
import com.example.bankmanagementsystemproject3.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthRepository authRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    // 1. CRUD
    // 1.1 Create a new bank account
    public void createAccount(Integer userId, AccountInDTO accountInDTO) {
        // Validate the customer
        Employee employee = employeeRepository.findEmployeeById(userId);
        if (employee == null) {
            throw new ApiException("Employee Not Found.");
        }
        // Validate and find the customer associated with the account creation
        Customer customer = customerRepository.findCustomerById(accountInDTO.getCustomerId());
        if (customer == null) {
            throw new ApiException("Customer Not Found.");
        }
        Account account = new Account(null, accountInDTO.getAccountNumber(), accountInDTO.getBalance(), false, customer);
        accountRepository.save(account);
    }

    // 1.2 View account details
    public AccountOutDTO viewAccount(Integer userId, Integer accountId) {
        // Validate the customer
        Customer customer = customerRepository.findCustomerById(userId);
        if (customer == null) {
            throw new ApiException("Customer Not Found.");
        }
        // Validate that the account exists and belong to this customer
        Account account = accountRepository.findAccountByIdAndCustomer(accountId, customer);
        if (account == null) {
            throw new ApiException("Account Not Found.");
        }
        return new AccountOutDTO(account.getAccountNumber(), account.getBalance());
    }

    // 1.3 Update account
    public void updateAccount(Integer userId, Integer accountId, AccountInDTO accountInDTO) {
        Employee employee = employeeRepository.findEmployeeById(userId);
        if (employee == null) {
            throw new ApiException("Employee Not Found.");
        }

        // Validate that the account to be updated exists
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account Not Found.");
        }

        // Update account details
        account.setAccountNumber(accountInDTO.getAccountNumber());
        account.setBalance(account.getBalance());

        // Save changes
        accountRepository.save(account);
    }

    // 1.4 Delete account
    public void deleteAccount(Integer userId, Integer accountId) {
        Employee employee = employeeRepository.findEmployeeById(userId);
        if (employee == null) {
            throw new ApiException("Employee Not Found.");
        }

        // Validate that the account to be updated exists
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account Not Found.");
        }
        accountRepository.delete(account);
    }

    // 2. Extra endpoints
    // 2.1 Active a bank account
    public void activeAccount(Integer userId, Integer accountId) {
        Employee employee = employeeRepository.findEmployeeById(userId);
        if (employee == null) {
            throw new ApiException("Employee Not Found.");
        }

        // Validate that the account to be activated exists
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account Not Found.");
        }

        if (account.getIsActive()) {
            throw new ApiException("Account Already Active.");
        }

        account.setIsActive(true);
        accountRepository.save(account);
    }

    // 2.2 List user's accounts
    public List<AccountOutDTO> getAllAccounts(Integer userId) {
        Employee employee = employeeRepository.findEmployeeById(userId);
        if (employee == null) {
            throw new ApiException("Employee Not Found.");
        }
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new ApiException("No Accounts Found.");
        }

        List<AccountOutDTO> accountOutDTOS = new ArrayList<>();
        for (Account account : accounts) {
            AccountOutDTO accountOutDTO = new AccountOutDTO(account.getAccountNumber(), account.getBalance());
            accountOutDTOS.add(accountOutDTO);
        }
        return accountOutDTOS;
    }

    // 2.3 Deposit money
    public void deposit(Integer userId, Integer accountId, BigDecimal amount) {
        // Validate the customer
        Customer customer = customerRepository.findCustomerById(userId);
        if (customer == null) {
            throw new ApiException("Customer Not Found.");
        }
        // Validate that the account exists and belong to this customer
        Account account = accountRepository.findAccountByIdAndCustomer(accountId, customer);
        if (account == null) {
            throw new ApiException("Account Not Found.");
        }

        // Check if whether account is active or not
        if (!account.getIsActive()) {
            throw new ApiException("Account is not active.");
        }

        // Update balance
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    // 2.4 Withdraw money
    public void withdraw(Integer userId, Integer accountId, BigDecimal amount) {
        // Validate the customer
        Customer customer = customerRepository.findCustomerById(userId);
        if (customer == null) {
            throw new ApiException("Customer Not Found.");
        }
        // Validate that the account exists and belong to this customer
        Account account = accountRepository.findAccountByIdAndCustomer(accountId, customer);
        if (account == null) {
            throw new ApiException("Account Not Found.");
        }

        // Check if whether account is active or not
        if (!account.getIsActive()) {
            throw new ApiException("Account is not active.");
        }

        // Check if customer has enough balance
        if (account.getBalance().compareTo(amount) < 0) {
            throw new ApiException("Insufficient funds.");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    // 2.5 Transfer funds between accounts
    public void transferFunds(Integer userId, Integer senderAccountId, Integer recipientAccountId, BigDecimal amount) {
        // Validate the customer
        Customer customer = customerRepository.findCustomerById(userId);
        if (customer == null) {
            throw new ApiException("Customer Not Found.");
        }

        // Validate that the sender account exists and belong to this customer
        Account senderAccount = accountRepository.findAccountByIdAndCustomer(senderAccountId, customer);
        if (senderAccount == null) {
            throw new ApiException("Sender Account Not Found.");
        }

        // Check if the sender account is active
        if (!senderAccount.getIsActive()) {
            throw new ApiException("Sender account is not active.");
        }

        // Check if the sender account has enough balance
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new ApiException("Insufficient funds in the sender's account.");
        }


        // Validate that the recipient account exists
        Account recipientAccount = accountRepository.findAccountById(recipientAccountId);
        if (recipientAccount == null) {
            throw new ApiException("Recipient Account Not Found.");
        }

        // Check if the recipient account is active
        if (!recipientAccount.getIsActive()) {
            throw new ApiException("Recipient account is not active.");
        }

        // Perform withdrawal from the sender account
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        accountRepository.save(senderAccount);

        // Perform deposit to the recipient account
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));
        accountRepository.save(recipientAccount);
    }

    // 2.6 Block bank account
    public void blockAccount(Integer userId, Integer accountId) {
        Employee employee = employeeRepository.findEmployeeById(userId);
        if (employee == null) {
            throw new ApiException("Employee Not Found.");
        }

        // Validate that the account to be blocked exists
        Account account = accountRepository.findAccountById(accountId);
        if (!account.getIsActive()) {
            throw new ApiException("Account Already Blocked.");
        }
        account.setIsActive(false);
        accountRepository.save(account);
    }
}