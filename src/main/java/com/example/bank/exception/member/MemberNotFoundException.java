package com.example.bank.exception.member;

import com.example.bank.exception.BankException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends BankException {
    public MemberNotFoundException(){

        super(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");
    }
}
