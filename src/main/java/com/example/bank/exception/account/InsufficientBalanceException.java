package com.example.bank.exception.account;

import com.example.bank.exception.BankException;
import org.springframework.http.HttpStatus;

public class InsufficientBalanceException extends BankException {
    public InsufficientBalanceException(){
        super(HttpStatus.BAD_REQUEST, "잔액이 부족합니다.");
    }
}
