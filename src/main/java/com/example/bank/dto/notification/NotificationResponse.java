package com.example.bank.dto.notification;

import com.example.bank.entity.Notification;
import com.example.bank.enums.NotificationReferenceType;

import java.time.OffsetDateTime;

public record NotificationResponse (
    Long notificationId,
    NotificationReferenceType referenceType,
    String title,
    String message,
    boolean isRead,
    OffsetDateTime createdAt
){
    public NotificationResponse(Notification notification){
        this(
                notification.getId(),
                notification.getReferenceType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
