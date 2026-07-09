package com.example.bank.exception.account;

import com.example.bank.exception.BankException;
import org.springframework.http.HttpStatus;

public class AccountUnavailableException extends BankException {
    public AccountUnavailableException(){
        super(HttpStatus.BAD_REQUEST, "이용할 수 없는 계좌입니다.");
    }
}
