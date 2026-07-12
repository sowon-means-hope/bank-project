package com.example.bank.exception.notification;

import com.example.bank.exception.BankException;
import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends BankException {
    public NotificationNotFoundException(){
        super(HttpStatus.NOT_FOUND, "해당하는 알림이 없습니다.");
    }
}
