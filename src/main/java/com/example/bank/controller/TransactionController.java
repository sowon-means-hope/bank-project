package com.example.bank.controller;

import com.example.bank.dto.transaction.RecentTargetResponse;
import com.example.bank.dto.transaction.TransactionResponse;
import com.example.bank.dto.transaction.TransactionSearchRequest;
import com.example.bank.security.CustomUserDetails;
import com.example.bank.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "거래내역 조회",
            description = "로그인한 사용자가 자신의 계좌번호로 해당 계좌의 거래내역을 조회합니다. 거래타입, 거래기간, 페이지 및 한 페이지 내 갯수를 선택적으로 입력할 수 있습니다."
    )
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
    @Operation(
            summary = "최근 거래 상대 조회",
            description = "빠른 송금을 위하여 로그인한 사용자의 계좌별 최근 거래 상대를 조회합니다."
    )
    public ResponseEntity<List<RecentTargetResponse>> getRecentTargets(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Pattern(
                    regexp = "^[0-9]{12}$",
                    message = "올바르지 않은 계좌번호입니다."
            )
            @PathVariable
            String accountNumber
    ){
        List<RecentTargetResponse> responses =
                transactionService.getRecentTargets(accountNumber, userDetails.getMemberId());

        return ResponseEntity.ok(responses);
    }
}
