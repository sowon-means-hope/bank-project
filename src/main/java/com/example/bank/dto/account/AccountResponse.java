package com.example.bank.dto.account;

import com.example.bank.entity.Account;
import com.example.bank.enums.AccountStatus;

public record AccountResponse (
        String accountNumber,
        Long balance,
        AccountStatus status
){
    public AccountResponse(Account account){
        this(
          account.getAccountNumber(),
          account.getBalance(),
          account.getStatus()
        );
    }
}
