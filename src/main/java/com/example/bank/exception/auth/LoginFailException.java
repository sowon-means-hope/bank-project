package com.example.bank.exception.auth;

public class LoginFailException extends RuntimeException{
    public LoginFailException(){
        super("없는 아이디이거나 비밀번호가 틀립니다.");
    }
}
