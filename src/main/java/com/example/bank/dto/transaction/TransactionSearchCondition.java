package com.example.bank.dto.transaction;

import com.example.bank.enums.TransactionType;

import java.time.LocalDateTime;

public record TransactionSearchCondition (
        Long accountId,
        TransactionType transactionType,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        int limit,
        int offset
){
}
