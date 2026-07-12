package com.example.bank.listener;

import com.example.bank.entity.Notification;
import com.example.bank.enums.NotificationReferenceType;
import com.example.bank.event.TransferSucceedEvent;
import com.example.bank.repository.MemberRepository;
import com.example.bank.repository.NotificationRepository;
import com.example.bank.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTransferSucceed(
            TransferSucceedEvent event
    ){
        Notification senderNotification = new Notification(
                memberRepository.getReferenceById(event.senderMemberId()),
                NotificationReferenceType.TRANSACTION,
                event.senderTransactionId(),
                String.format("출금 %,d원", event.amount()),
                String.format("""
                        받은 사람 : %s
                        출금계좌 : %s
                        거래 후 잔액 : %,d원
                        """,
                        event.receiverName(),
                        event.senderAccountNumber(),
                        event.senderBalanceAfter())
        );

        Notification receiverNotification = new Notification(
                memberRepository.getReferenceById(event.receiverMemberId()),
                NotificationReferenceType.TRANSACTION,
                event.receiverTransactionId(),
                String.format("입금 %,d원", event.amount()),
                String.format("""
                        보낸 사람 : %s
                        입금계좌 : %s
                        거래 후 잔액 : %,d원
                        """,
                        event.senderName(),
                        event.receiverAccountNumber(),
                        event.receiverBalanceAfter())
        );


        notificationService.saveNotifications(
                List.of(
                        senderNotification,
                        receiverNotification
                )
        );

    }
}
