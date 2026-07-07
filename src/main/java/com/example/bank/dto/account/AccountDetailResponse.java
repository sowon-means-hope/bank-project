package com.example.bank.dto.account;

import com.example.bank.entity.Account;
import com.example.bank.enums.AccountStatus;

import java.time.OffsetDateTime;

public record AccountDetailResponse (
        String accountNumber,
        Long balance,
        AccountStatus status,
        OffsetDateTime createdAt
){
    public AccountDetailResponse(Account account) {
        this(
                account.getAccountNumber(),
                account.getBalance(),
                account.getStatus(),
                account.getCreatedAt()
        );
    }
}
