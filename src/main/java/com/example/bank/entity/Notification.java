package com.example.bank.entity;

import com.example.bank.enums.NotificationReferenceType;
import com.example.bank.exception.notification.NotificationAlreadyReadException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", nullable = false)
    private NotificationReferenceType referenceType;

    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    @Column(nullable = false)
    private String title;

    @Column
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public Notification(
            Member member,
            NotificationReferenceType referenceType,
            Long referenceId,
            String title,
            String message
    ){
        this.member = member;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.title = title;
        this.message = message;
    }

    public void setRead(){
        if(isRead){
            throw new NotificationAlreadyReadException();
        }
        isRead = true;
    }
}
