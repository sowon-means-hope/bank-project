package com.example.bank.dto.notification;

import com.example.bank.entity.Notification;

import java.time.OffsetDateTime;

public record NotificationResponse (
    Long notificationId,
    String title,
    boolean isRead,
    OffsetDateTime createdAt
){
    public NotificationResponse(Notification notification){
        this(
                notification.getId(),
                notification.getTitle(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
