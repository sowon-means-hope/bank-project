package com.example.bank.service;

import com.example.bank.dto.account.TransferRequest;
import com.example.bank.dto.account.TransferResponse;
import com.example.bank.entity.Account;
import com.example.bank.entity.Member;
import com.example.bank.entity.Notification;
import com.example.bank.entity.Transaction;
import com.example.bank.enums.NotificationReferenceType;
import com.example.bank.enums.TransactionType;
import com.example.bank.exception.account.InsufficientBalanceException;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.MemberRepository;
import com.example.bank.repository.NotificationRepository;
import com.example.bank.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceIntegrationTest {
    // for Given
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AccountRepository accountRepository;
    // for When
    @Autowired
    AccountService accountService;
    // for Then
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    JdbcTemplate jdbcTemplate; // to check trigger

    @Test
    void 송금_통합테스트(){

        // Given
        Member fromMember = new Member(
                "test111",
                "test123!@#",
                "test1",
                "01011111111",
                "test1@example.com"
        );
        Member toMember = new Member(
                "test222",
                "test456$%^",
                "test2",
                "01022222222",
                "test2@example.com"
        );
        memberRepository.save(fromMember);
        memberRepository.save(toMember);

        Account fromAccount = new Account(
                fromMember, "100111111111"
        );
        Account toAccount = new Account(
                toMember, "100222222222"
        );
        fromAccount.deposit(100000L);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        TransferRequest request = new TransferRequest(
                "100111111111",
                "100222222222",
                10000L,
                "테스트 송금"
        );

        // When
        TransferResponse response =
                accountService.transfer(request, fromMember.getId());

        // Then
        // 1) Response
        Transaction tx = transactionRepository.findById(
                response.transactionId()
        ).orElseThrow();

        assertEquals(tx.getId(), response.transactionId());
        assertEquals(toAccount.getAccountNumber(), response.toAccountNumber());
        assertEquals(toMember.getName(), response.recipientName());
        assertEquals(tx.getAmount(), response.amount());
        assertEquals(tx.getBalanceAfter(), response.balanceAfter());
        assertEquals(tx.getDescription(), response.description());
        assertEquals(tx.getCreatedAt().toInstant(), response.transferredAt().toInstant());

        // 2) Account(DB)
        Account updatedFromAccount = accountRepository.findById(fromAccount.getId()).orElseThrow();
        Account updatedToAccount = accountRepository.findById(toAccount.getId()).orElseThrow();

        assertEquals(90000L, updatedFromAccount.getBalance());
        assertEquals(10000L, updatedToAccount.getBalance());

        // 3) Transaction(DB)
        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(2, transactions.size());

        Transaction fromTransaction = transactions.getFirst();
        Transaction toTransaction = transactions.getLast();

        assertNotNull(fromTransaction.getId());
        assertEquals(updatedFromAccount.getId(), fromTransaction.getAccount().getId());
        assertEquals(TransactionType.TRANSFER_OUT, fromTransaction.getType());
        assertEquals(10000L, fromTransaction.getAmount());
        assertEquals(90000L, fromTransaction.getBalanceAfter());
        assertEquals("100222222222", fromTransaction.getOpponentAccount());
        assertEquals("test2", fromTransaction.getOpponentName());
        assertEquals("테스트 송금", fromTransaction.getDescription());
        assertNotNull(fromTransaction.getCreatedAt());

        assertNotNull(toTransaction.getId());
        assertEquals(updatedToAccount.getId(), toTransaction.getAccount().getId());
        assertEquals(TransactionType.TRANSFER_IN, toTransaction.getType());
        assertEquals(10000L, toTransaction.getAmount());
        assertEquals(10000L, toTransaction.getBalanceAfter());
        assertEquals("100111111111", toTransaction.getOpponentAccount());
        assertEquals("test1", toTransaction.getOpponentName());
        assertEquals("테스트 송금", toTransaction.getDescription());
        assertNotNull(toTransaction.getCreatedAt());

        // 4) Notification(DB)
        List<Notification> notifications = notificationRepository.findAll();
        assertEquals(2, notifications.size());

        Notification fromNotification = notifications.getFirst();
        Notification toNotification = notifications.getLast();

        Member updatedFromMember = memberRepository.findByLoginId("test111").orElseThrow();
        Member updatedToMember = memberRepository.findByLoginId("test222").orElseThrow();

        assertNotNull(fromNotification.getId());
        assertEquals(updatedFromMember.getId(), fromNotification.getMember().getId());
        assertEquals(NotificationReferenceType.TRANSACTION, fromNotification.getReferenceType());
        assertEquals(fromTransaction.getId(), fromNotification.getReferenceId());
        assertEquals(String.format("출금 %,d원", fromTransaction.getAmount()), fromNotification.getTitle());
        assertEquals(
                String.format("""
                        받은 사람 : %s
                        출금계좌 : %s
                        거래 후 잔액 : %,d원
                        """,
                        fromTransaction.getOpponentName(),
                        fromAccount.getAccountNumber(),
                        fromTransaction.getBalanceAfter()
                ),
                fromNotification.getMessage()
        );
        assertFalse(fromNotification.isRead());
        assertNotNull(fromNotification.getCreatedAt());

        assertNotNull(toNotification.getId());
        assertEquals(updatedToMember.getId(), toNotification.getMember().getId());
        assertEquals(NotificationReferenceType.TRANSACTION, toNotification.getReferenceType());
        assertEquals(toTransaction.getId(), toNotification.getReferenceId());
        assertEquals(String.format("입금 %,d원", toTransaction.getAmount()), toNotification.getTitle());
        assertEquals(
                String.format("""
                        보낸 사람 : %s
                        입금계좌 : %s
                        거래 후 잔액 : %,d원
                        """,
                        toTransaction.getOpponentName(),
                        toAccount.getAccountNumber(),
                        toTransaction.getBalanceAfter()
                ),
                toNotification.getMessage()
        );
        assertFalse(toNotification.isRead());
        assertNotNull(toNotification.getCreatedAt());

        // 5) Trigger(DB, audit_log)
        List<Map<String, Object>> notificationLogs =
                jdbcTemplate.queryForList(
                        """
                        SELECT *
                        FROM audit_log
                        WHERE table_name='notification'
                        """
                );
        assertEquals(2, notificationLogs.size());

        Map<String, Object> log = notificationLogs.getFirst();
        assertEquals("INSERT", log.get("action"));
        assertEquals(fromNotification.getId(), ((Number)log.get("target_id")).longValue());

    }

    @AfterEach
    void tearDown(){
        jdbcTemplate.execute("""
            TRUNCATE TABLE
                audit_log,
                notification,
                transaction_history,
                account,
                member
            RESTART IDENTITY CASCADE
        """);
    }

    @Test
    void 잔액부족_통합테스트(){
        // Given
        Member fromMember = new Member(
                "test111",
                "test123!@#",
                "test1",
                "01011111111",
                "test1@example.com"
        );
        Member toMember = new Member(
                "test222",
                "test456$%^",
                "test2",
                "01022222222",
                "test2@example.com"
        );
        memberRepository.save(fromMember);
        memberRepository.save(toMember);

        Account fromAccount = new Account(
                fromMember, "100111111111"
        );
        Account toAccount = new Account(
                toMember, "100222222222"
        );
        fromAccount.deposit(5000L);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        TransferRequest request = new TransferRequest(
                "100111111111",
                "100222222222",
                10000L,
                "테스트 송금"
        );

        // When & Then
        assertThrows(
                InsufficientBalanceException.class,
                () -> accountService.transfer(request, fromMember.getId())
        );

        Account updatedFromAccount = accountRepository.findById(fromAccount.getId()).orElseThrow();
        Account updatedToAccount = accountRepository.findById(toAccount.getId()).orElseThrow();

        assertEquals(5000L, updatedFromAccount.getBalance());
        assertEquals(toAccount.getBalance(), updatedToAccount.getBalance());

        assertTrue(transactionRepository.findAll().isEmpty());

        assertTrue(notificationRepository.findAll().isEmpty());

        List<Map<String, Object>> notificationLogs =
                jdbcTemplate.queryForList(
                        """
                        SELECT *
                        FROM audit_log
                        WHERE table_name='notification'
                        """
                );
        assertTrue(notificationLogs.isEmpty());


    }

}
