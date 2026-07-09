package com.example.bank.entity;

import com.example.bank.enums.AccountStatus;
import com.example.bank.exception.account.AccountAlreadyClosedException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "account_number", nullable = false, unique = true, length = 12)
    private String accountNumber;

    @Column(nullable = false)
    private Long balance = 0L;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "closed_at")
    private OffsetDateTime closedAt;

    public Account(
            Member member,
            String accountNumber
    ){
        this.member = member;
        this.accountNumber = accountNumber;
    }

    public void deactivate(){
        if(this.status == AccountStatus.CLOSED){
            throw new AccountAlreadyClosedException();
        }
        this.status = AccountStatus.CLOSED;
        this.closedAt = OffsetDateTime.now();
    }
}
