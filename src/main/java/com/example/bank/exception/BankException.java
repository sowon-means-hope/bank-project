package com.example.bank.exception;

import org.springframework.http.HttpStatus;

public abstract class BankException extends RuntimeException{
    private final HttpStatus status;

    protected BankException(HttpStatus status, String message){
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus(){
        return status;
    }
}
