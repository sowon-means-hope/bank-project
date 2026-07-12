package com.example.bank.controller;

import com.example.bank.dto.transaction.RecentTargetResponse;
import com.example.bank.dto.transaction.TransactionResponse;
import com.example.bank.dto.transaction.TransactionSearchRequest;
import com.example.bank.security.CustomUserDetails;
import com.example.bank.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions/{accountNumber}")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> searchTransactions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Pattern(
                    regexp = "^[0-9]{12}$",
                    message = "올바르지 않은 계좌번호입니다."
            )
            @PathVariable
            String accountNumber,
            @Valid @ModelAttribute TransactionSearchRequest request
            ){
        List<TransactionResponse> responses =
                transactionService.searchTransactions(accountNumber, request, userDetails.getMemberId());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/recent-targets")
    public ResponseEntity<List<RecentTargetResponse>> getRecentTargets(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam
            @NotBlank(message = "계좌를 선택해주세요.")
            @Pattern(
                    regexp = "^[0-9]{12}$",
                    message = "올바르지 않은 계좌번호입니다."
            )
            String accountNumber
    ){
        List<RecentTargetResponse> responses =
                transactionService.getRecentTargets(accountNumber, userDetails.getMemberId());

        return ResponseEntity.ok(responses);
    }
}
