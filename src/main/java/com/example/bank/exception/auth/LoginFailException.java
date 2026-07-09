package com.example.bank.exception.auth;

import com.example.bank.exception.BankException;
import org.springframework.http.HttpStatus;

public class LoginFailException extends BankException {
    public LoginFailException(){

        super(HttpStatus.UNAUTHORIZED, "없는 아이디이거나 비밀번호가 틀립니다.");
    }
}
