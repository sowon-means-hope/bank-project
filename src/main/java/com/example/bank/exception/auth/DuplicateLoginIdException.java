package com.example.bank.exception.auth;

import com.example.bank.exception.BankException;
import org.springframework.http.HttpStatus;

public class DuplicateLoginIdException extends BankException {

    public DuplicateLoginIdException() {
        super(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다.");
    }
}
