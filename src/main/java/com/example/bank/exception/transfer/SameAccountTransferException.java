package com.example.bank.exception.transfer;

import com.example.bank.exception.BankException;
import org.springframework.http.HttpStatus;

public class SameAccountTransferException extends BankException {
    public SameAccountTransferException(){
        super(HttpStatus.BAD_REQUEST, "동일한 계좌로는 송금할 수 없습니다.");
    }
}
