package com.example.bank.controller;

import com.example.bank.dto.account.*;
import com.example.bank.security.CustomUserDetails;
import com.example.bank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
    @Operation(
            summary = "계좌 생성",
            description = "로그인한 사용자의 계좌를 한 개씩 생성합니다."
    )
    public ResponseEntity<AccountDetailResponse> openAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){

        AccountDetailResponse accountDetail  = accountService.openAccount(userDetails.getMemberId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountDetail);
    }

    @GetMapping
    @Operation(
            summary = "계좌 조회",
            description = "로그인한 사용자의 모든 계좌를 조회합니다."
    )
    public ResponseEntity<List<AccountResponse>> getAccounts(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        List<AccountResponse> accounts =
                accountService.getAccounts(userDetails.getMemberId());

        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/{accountNumber}/deposit")
    @Operation(
            summary = "입금",
            description = "송금 테스트를 위하여 해당 계좌번호의 잔액에 금액을 더합니다."
    )
    public ResponseEntity<AccountResponse> deposit(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String accountNumber,
            @Valid @RequestBody DepositRequest request
    ){
        AccountResponse response =
                accountService.deposit(request, accountNumber, userDetails.getMemberId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountNumber}")
    @Operation(
            summary = "계좌 상세 조회",
            description = "로그인한 사용자의 계좌번호로 계좌의 디테일한 사항들을 조회합니다."
    )
    public ResponseEntity<AccountDetailResponse> getAccountDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String accountNumber
    ){
        AccountDetailResponse accountDetail =
                accountService.getAccountDetail(accountNumber, userDetails.getMemberId());

        return ResponseEntity.ok(accountDetail);
    }

    @PatchMapping("/{accountNumber}")
    @Operation(
            summary = "계좌 해지",
            description = "로그인한 사용자의 계좌번호로 해당 계좌를 해지합니다."
    )
    public ResponseEntity<AccountDetailResponse> closeAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String accountNumber
    ){
        AccountDetailResponse accountDetail =
                accountService.closeAccount(accountNumber, userDetails.getMemberId());

        return ResponseEntity.ok(accountDetail);
    }

    @GetMapping("/verify")
    @Operation(
            summary = "예금주 조회",
            description = "로그인한 사용자가 송금 전 계좌번호로 상대방의 이름을 확인합니다."
    )
    public ResponseEntity<VerifyAccountResponse> verifyAccount(
            @RequestParam
            @NotBlank(message = "계좌번호를 입력해주세요.")
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
    @Operation(
            summary = "송금",
            description = "로그인한 사용자의 출금할 자신의 계쫘번호, 입금될 상대 계좌번호, 금액으로 송금을 진행합니다."
    )
    public ResponseEntity<TransferResponse> transfer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody TransferRequest request
            ){

        TransferResponse response =
                accountService.transfer(request, userDetails.getMemberId());

        return ResponseEntity.ok(response);
    }

}
