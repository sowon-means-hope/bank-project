package com.example.bank.exception.notification;

import com.example.bank.exception.BankException;
import org.springframework.http.HttpStatus;

public class NotificationAlreadyReadException extends BankException {
    public NotificationAlreadyReadException(){
        super(HttpStatus.BAD_REQUEST, "이미 읽은 알림입니다.");
    }
}
