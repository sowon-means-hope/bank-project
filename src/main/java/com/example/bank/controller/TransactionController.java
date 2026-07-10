package com.example.bank.controller;

import com.example.bank.dto.transaction.TransactionResponse;
import com.example.bank.dto.transaction.TransactionSearchRequest;
import com.example.bank.security.CustomUserDetails;
import com.example.bank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> searchTransactions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute TransactionSearchRequest request
            ){
        List<TransactionResponse> responses =
                transactionService.searchTransactions(request, userDetails.getMemberId());

        return ResponseEntity.ok(responses);
    }
}
