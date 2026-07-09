package com.example.bank.controller;

import com.example.bank.dto.ApiResponse;
import com.example.bank.dto.account.AccountDetailResponse;
import com.example.bank.dto.account.AccountResponse;
import com.example.bank.dto.account.VerifyAccountResponse;
import com.example.bank.entity.Account;
import com.example.bank.security.CustomUserDetails;
import com.example.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDetailResponse> openAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){

        AccountDetailResponse accountDetail  = accountService.openAccount(userDetails.getMemberId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountDetail);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccounts(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        List<AccountResponse> accounts =
                accountService.getAccounts(userDetails.getMemberId());

        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDetailResponse> getAccountDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String accountNumber
    ){
        AccountDetailResponse accountDetail =
                accountService.getAccountDetail(accountNumber, userDetails.getMemberId());

        return ResponseEntity.ok(accountDetail);
    }

    @PatchMapping("/{accountNumber}")
    public ResponseEntity<AccountDetailResponse> closeAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String accountNumber
    ){
        AccountDetailResponse accountDetail =
                accountService.closeAccount(accountNumber, userDetails.getMemberId());

        return ResponseEntity.ok(accountDetail);
    }

    @GetMapping("/verify")
    public ResponseEntity<VerifyAccountResponse> verifyAccount(
            @RequestParam String accountNumber
    ){
        VerifyAccountResponse response =
                accountService.verifyAccount(accountNumber);

        return ResponseEntity.ok(response);
    }
}
