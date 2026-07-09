package com.example.bank.controller;

import com.example.bank.dto.account.AccountDetailResponse;
import com.example.bank.dto.account.AccountResponse;
import com.example.bank.dto.account.VerifyAccountResponse;
import com.example.bank.dto.account.TransferRequest;
import com.example.bank.dto.account.TransferResponse;
import com.example.bank.security.CustomUserDetails;
import com.example.bank.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
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
            @RequestParam
            @Pattern(
                    regexp = "^[0-9]{12}$",
                    message = "계좌번호는 12자리 숫자여야 합니다."
            )
            String accountNumber
    ){
        VerifyAccountResponse response =
                accountService.verifyAccount(accountNumber);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody TransferRequest request
            ){

        TransferResponse response =
                accountService.transfer(request, userDetails.getMemberId());

        return ResponseEntity.ok(response);
    }
}
