package com.example.bank.exception.transaction;

import com.example.bank.exception.BankException;
import org.springframework.http.HttpStatus;

public class TransactionNotFoundException extends BankException {
    public TransactionNotFoundException(){
        super(HttpStatus.NOT_FOUND, "존재하지 않는 거래내역입니다.");
    }
}
