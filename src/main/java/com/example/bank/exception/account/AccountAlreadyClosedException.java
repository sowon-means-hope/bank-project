package com.example.bank.exception.account;

import com.example.bank.exception.BankException;
import org.springframework.http.HttpStatus;

public class AccountAlreadyClosedException extends BankException {
    public AccountAlreadyClosedException(){

        super(HttpStatus.BAD_REQUEST, "이미 해지된 계좌입니다.");
    }
}
