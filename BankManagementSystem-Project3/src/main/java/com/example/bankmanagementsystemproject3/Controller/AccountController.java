package com.example.bankmanagementsystemproject3.Controller;

import com.example.bankmanagementsystemproject3.Api.ApiResponse;
import com.example.bankmanagementsystemproject3.InDTO.AccountInDTO;
import com.example.bankmanagementsystemproject3.Model.MyUser;
import com.example.bankmanagementsystemproject3.Service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    // 1. CRUD
    // 1.1 Create a new bank account
    @PostMapping("/create")
    public ResponseEntity createAccount(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid AccountInDTO accountInDTO) {
        accountService.createAccount(myUser.getId(), accountInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Bank Account Created."));
    }

    // 1.2 View account details
    @GetMapping("/view/{accountId}")
    public ResponseEntity viewAccount(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer accountId) {
        return ResponseEntity.status(200).body(accountService.viewAccount(myUser.getId(), accountId));
    }

    // 1.3 Update account
    @PutMapping("/update/{accountId}")
    public ResponseEntity updateAccount(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer accountId, @RequestBody @Valid AccountInDTO accountInDTO) {
        accountService.updateAccount(myUser.getId(), accountId, accountInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Account Updated."));
    }

    // 1.4 Delete customer
    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity deleteAccount(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer accountId) {
        accountService.deleteAccount(myUser.getId(), accountId);
        return ResponseEntity.status(200).body(new ApiResponse("Account Deleted."));
    }

    // 2. Extra endpoints
    // 2.1 Active a bank account
    @PutMapping("/active/{accountId}")
    public ResponseEntity activeAccount(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer accountId) {
        accountService.activeAccount(myUser.getId(), accountId);
        return ResponseEntity.status(200).body(new ApiResponse("Account Activated."));
    }

    // 2.2 List user's accounts
    @GetMapping("/getAllAccounts")
    public ResponseEntity getAllAccounts(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(accountService.getAllAccounts(myUser.getId()));
    }

    // 2.3 Deposit money
    @PutMapping("/deposit/accountId/{accountId}/amount/{amount}")
    public ResponseEntity deposit(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer accountId, @PathVariable BigDecimal amount) {
        accountService.deposit(myUser.getId(), accountId, amount);
        return ResponseEntity.status(200).body(new ApiResponse("Deposit Completed."));
    }

    // 2.4 Withdraw money
    @PutMapping("/withdraw/accountId/{accountId}/amount/{amount}")
    public ResponseEntity withdraw(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer accountId, @PathVariable BigDecimal amount) {
        accountService.withdraw(myUser.getId(), accountId, amount);
        return ResponseEntity.status(200).body(new ApiResponse("Deposit Completed."));
    }

    // 2.5 Transfer funds between accounts
    @PutMapping("/transfer/senderAccountId/{senderAccountId}/recipientAccountId/{recipientAccountId}/amount/{amount}")
    public ResponseEntity transferFunds(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer senderAccountId, @PathVariable Integer recipientAccountId, @PathVariable BigDecimal amount) {
        accountService.transferFunds(myUser.getId(), senderAccountId, recipientAccountId, amount);
        return ResponseEntity.status(200).body(new ApiResponse("Transfer Completed."));
    }

    // 2.6 Block bank account
    @PutMapping("/block/accountId/{accountId}")
    public ResponseEntity blockAccount(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer accountId) {
        accountService.blockAccount(myUser.getId(), accountId);
        return ResponseEntity.status(200).body(new ApiResponse("Account Blocked."));
    }
}