package com.example.bank.dto.transaction;

import com.example.bank.enums.TransactionType;

import java.time.OffsetDateTime;

public record TransactionResponse (
        Long transactionId,
        TransactionType transactionType,
        Long amount,
        Long balanceAfter,
        String opponentAccount,
        String opponentName,
        String description,
        OffsetDateTime createdAt
){
}
