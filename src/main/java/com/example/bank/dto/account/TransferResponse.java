package com.example.bank.dto.account;

import com.example.bank.entity.Transaction;
import com.example.bank.enums.TransactionType;

import java.time.OffsetDateTime;

public record TransferResponse (
    Long transactionId,
    TransactionType transactionType,
    String fromAccountNumber,
    String toAccountNumber,
    String recipientName,
    Long amount,
    Long balanceAfter,
    String description,
    OffsetDateTime transferredAt
){
    public TransferResponse(
            String recipientName,
            Transaction transaction
    ){
        this(
                transaction.getId(),
                transaction.getType(),
                transaction.getAccount().getAccountNumber(),
                transaction.getOpponentAccount(),
                recipientName,
                transaction.getAmount(),
                transaction.getBalanceAfter(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }
}
