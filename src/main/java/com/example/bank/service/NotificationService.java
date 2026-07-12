package com.example.bank.service;

import com.example.bank.dto.notification.NotificationReadResponse;
import com.example.bank.dto.notification.NotificationResponse;
import com.example.bank.entity.Notification;
import com.example.bank.entity.Transaction;
import com.example.bank.exception.notification.NotificationNotFoundException;
import com.example.bank.exception.transaction.TransactionNotFoundException;
import com.example.bank.repository.NotificationRepository;
import com.example.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveNotifications(
            List<Notification> notifications
    ){
        notificationRepository.saveAll(notifications);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(
            Long memberId
    ){
        List<Notification> notifications =
                notificationRepository.findByMemberId(memberId);

        return notifications.stream()
                .map(NotificationResponse::new)
                .toList();
    }

    @Transactional
    public NotificationReadResponse setRead(
            Long notificationsId,
            Long memberId
    ){
        Notification notification =
                notificationRepository.findByIdAndMemberId(notificationsId, memberId)
                        .orElseThrow(NotificationNotFoundException::new);

        notification.setRead();

        Transaction transaction =
                transactionRepository.findById(notification.getReferenceId())
                        .orElseThrow(TransactionNotFoundException::new);

        return new NotificationReadResponse(
                transaction.getAccount().getAccountNumber(),
                transaction.getId()
        );
    }
}
