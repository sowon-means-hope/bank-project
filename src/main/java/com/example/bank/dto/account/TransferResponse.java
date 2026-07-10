package com.example.bank.dto.account;

import com.example.bank.entity.Transaction;
import com.example.bank.enums.TransactionType;

import java.time.OffsetDateTime;

public record TransferResponse (
    Long transactionId,
    String toAccountNumber,
    String recipientName,
    Long amount,
    Long balanceAfter,
    String description,
    OffsetDateTime transferredAt
){
    public TransferResponse(
            Transaction transaction
    ){
        this(
                transaction.getId(),
                transaction.getOpponentAccount(),
                transaction.getOpponentName(),
                transaction.getAmount(),
                transaction.getBalanceAfter(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }
}
