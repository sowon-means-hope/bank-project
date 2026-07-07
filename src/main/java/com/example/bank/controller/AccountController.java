package com.example.bank.controller;

import com.example.bank.dto.ApiResponse;
import com.example.bank.dto.account.AccountDetailResponse;
import com.example.bank.dto.account.AccountResponse;
import com.example.bank.entity.Account;
import com.example.bank.security.CustomUserDetails;
import com.example.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponse<Void>> openAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){

        accountService.openAccount(userDetails.getMemberId());

        return ResponseEntity.ok(ApiResponse.success("계좌 개설 성공"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAccounts(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        List<AccountResponse> accounts =
                accountService.getAccounts(userDetails.getMemberId());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "계좌 조회 성공",
                        accounts
                )
        );
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> getAccountDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String accountNumber
    ){
        AccountDetailResponse accountDetail =
                accountService.getAccountDetail(accountNumber, userDetails.getMemberId());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "계좌 상세 조회 성공",
                        accountDetail
                )
        );
    }

    @PatchMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> closeAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String accountNumber
    ){
        AccountDetailResponse accountDetail =
                accountService.closeAccount(accountNumber, userDetails.getMemberId());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "계좌 해지 성공",
                        accountDetail
                )
        );
    }
}
