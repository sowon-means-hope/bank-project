package com.example.bank.exception.account;

public class AccountAlreadyClosedException extends RuntimeException{
    public AccountAlreadyClosedException(){
        super("이미 해지된 계좌입니다.");
    }
}
