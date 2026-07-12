package com.example.bank.event;

import java.time.OffsetDateTime;

public record TransferSucceedEvent(

        Long senderTransactionId,
        Long receiverTransactionId,
        Long amount,

        Long senderMemberId,
        Long receiverMemberId,
        String senderName,
        String receiverName,
        String senderAccountNumber,
        String receiverAccountNumber,
        Long senderBalanceAfter,
        Long receiverBalanceAfter


){
}
