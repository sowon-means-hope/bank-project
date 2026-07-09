package com.example.bank.exception.account;

import com.example.bank.exception.BankException;
import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends BankException {
    public AccountNotFoundException(){

        super(HttpStatus.NOT_FOUND, "계좌를 찾을 수 없습니다.");
    }
}
