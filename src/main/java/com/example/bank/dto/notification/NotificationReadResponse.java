package com.example.bank.dto.notification;

public record NotificationReadResponse (
        String accountNumber,
        Long transactionId
){
}
