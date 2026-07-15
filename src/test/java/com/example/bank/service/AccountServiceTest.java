package com.example.bank.service;

import com.example.bank.dto.account.TransferRequest;
import com.example.bank.dto.account.TransferResponse;
import com.example.bank.entity.Account;
import com.example.bank.entity.Member;
import com.example.bank.entity.Transaction;
import com.example.bank.enums.TransactionType;
import com.example.bank.event.TransferSucceedEvent;
import com.example.bank.exception.account.InsufficientBalanceException;
import com.example.bank.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AccountService accountService;

    @Test
    void 송금_성공(){

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
        ReflectionTestUtils.setField(fromMember, "id", 1L);
        ReflectionTestUtils.setField(toMember, "id", 2L);

        Account fromAccount = new Account(
                fromMember, "100111111111"
        );
        Account toAccount = new Account(
                toMember, "100222222222"
        );
        ReflectionTestUtils.setField(fromAccount, "id", 1L);
        ReflectionTestUtils.setField(toAccount, "id", 2L);
        fromAccount.deposit(100000L);

        TransferRequest request = new TransferRequest(
                "100111111111",
                "100222222222",
                10000L,
                "테스트 송금"
        );

        Transaction fromTransaction = new Transaction(
                fromAccount,
                TransactionType.TRANSFER_OUT,
                10000L,
                90000L,
                "100222222222",
                "test2",
                "테스트 송금"
        );
        Transaction toTransaction = new Transaction(
                toAccount,
                TransactionType.TRANSFER_IN,
                10000L,
                10000L,
                "100111111111",
                "test1",
                "테스트 송금"
        );
        ReflectionTestUtils.setField(fromTransaction, "id", 1L);
        ReflectionTestUtils.setField(toTransaction, "id", 2L);

        OffsetDateTime fixedTime =
                OffsetDateTime.parse("2026-07-13T14:00:00+09:00");

        ReflectionTestUtils.setField(fromTransaction, "createdAt", fixedTime);
        ReflectionTestUtils.setField(toTransaction, "createdAt", fixedTime);

        when(accountRepository.findByAccountNumberAndMemberIdWithLock(
                fromAccount.getAccountNumber(),
                fromMember.getId()
        )).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumberWithLock(
                toAccount.getAccountNumber()
        )).thenReturn(Optional.of(toAccount));

        when(transactionService.saveTransaction(
                any(Account.class),
                any(TransactionType.class),
                anyLong(),
                anyString(),
                anyString(),
                anyString()
        ))
                .thenReturn(fromTransaction)
                .thenReturn(toTransaction);


        // When

        TransferResponse response =
                accountService.transfer(request, fromMember.getId());


        // Then

        // 1) check value of Response
        assertNotNull(response);

        assertEquals(1L, response.transactionId());
        assertEquals("100222222222", response.toAccountNumber());
        assertEquals("test2", response.recipientName());
        assertEquals(10000L, response.amount());
        assertEquals(90000L, response.balanceAfter());
        assertEquals("테스트 송금", response.description());
        assertEquals(fromTransaction.getCreatedAt(), response.transferredAt());

        // 2) check state of Entity
        assertEquals(90000L, fromAccount.getBalance());
        assertEquals(10000L, toAccount.getBalance());

        // 3) check call of Repository
        verify(accountRepository)
                .findByAccountNumberAndMemberIdWithLock(
                        fromAccount.getAccountNumber(),
                        fromMember.getId()
                );
        verify(accountRepository)
                .findByAccountNumberWithLock(
                        toAccount.getAccountNumber()
                );


        // pre-4/5) check count/order of call
        InOrder inOrder = inOrder(transactionService, eventPublisher);

        // 4) check call + parameters of Service
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        ArgumentCaptor<TransactionType> typeCaptor = ArgumentCaptor.forClass(TransactionType.class);
        ArgumentCaptor<Long> amountCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> opponentAccountNumberCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> opponentNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> descriptionCaptor = ArgumentCaptor.forClass(String.class);

        inOrder.verify(transactionService, times(2))
                .saveTransaction(
                        accountCaptor.capture(),
                        typeCaptor.capture(),
                        amountCaptor.capture(),
                        opponentAccountNumberCaptor.capture(),
                        opponentNameCaptor.capture(),
                        descriptionCaptor.capture()
                );

        List<Account> accounts = accountCaptor.getAllValues();
        List<TransactionType> types = typeCaptor.getAllValues();
        List<Long> amounts = amountCaptor.getAllValues();
        List<String> opponentAccountNumbers = opponentAccountNumberCaptor.getAllValues();
        List<String> opponentNames = opponentNameCaptor.getAllValues();
        List<String> descriptions = descriptionCaptor.getAllValues();

        // first call
        assertEquals(fromAccount, accounts.getFirst());
        assertEquals(TransactionType.TRANSFER_OUT, types.getFirst());
        assertEquals(10000L, amounts.getFirst());
        assertEquals("100222222222", opponentAccountNumbers.getFirst());
        assertEquals("test2", opponentNames.getFirst());
        assertEquals("테스트 송금", descriptions.getFirst());

        // second call
        assertEquals(toAccount, accounts.getLast());
        assertEquals(TransactionType.TRANSFER_IN, types.getLast());
        assertEquals(10000L, amounts.getLast());
        assertEquals("100111111111", opponentAccountNumbers.getLast());
        assertEquals("test1", opponentNames.getLast());
        assertEquals("테스트 송금", descriptions.getLast());

        // 5) check Event
        ArgumentCaptor<TransferSucceedEvent> eventCaptor = ArgumentCaptor.forClass(TransferSucceedEvent.class);

        inOrder.verify(eventPublisher).publishEvent(eventCaptor.capture());

        TransferSucceedEvent event = eventCaptor.getValue();

        assertEquals(1L, event.senderTransactionId());
        assertEquals(2L, event.receiverTransactionId());
        assertEquals(10000L, event.amount());
        assertEquals(1L, event.senderMemberId());
        assertEquals(2L, event.receiverMemberId());
        assertEquals("test1", event.senderName());
        assertEquals("test2", event.receiverName());
        assertEquals("100111111111", event.senderAccountNumber());
        assertEquals("100222222222", event.receiverAccountNumber());
        assertEquals(90000L, event.senderBalanceAfter());
        assertEquals(10000L, event.receiverBalanceAfter());

        // 6) verify No More Interactions

        verifyNoMoreInteractions(
                transactionService,
                eventPublisher
        );

    }

    @Test
    void 송금_실패_잔액부족(){

        // Given
        Long memberId = 1L;

        Member fromMember = new Member(
                "test123",
                "test1231@#",
                "test1",
                "01011111111",
                "test1@example.com"
        );
        Member toMember = new Member(
                "test456",
                "test456$%^",
                "test2",
                "01022222222",
                "test2@example.com"
        );
        ReflectionTestUtils.setField(fromMember, "id", memberId);
        ReflectionTestUtils.setField(toMember, "id", 2L);

        Account fromAccount = new Account(
                fromMember, "100111111111"
        );
        Account toAccount = new Account(
                toMember, "100222222222"
        );
        ReflectionTestUtils.setField(fromAccount, "id", 1L);
        ReflectionTestUtils.setField(toAccount, "id", 2L);
        fromAccount.deposit(9000L);

        TransferRequest request = new TransferRequest(
                "100111111111",
                "100222222222",
                10000L,
                "테스트 송금"
        );

        when(accountRepository.findByAccountNumberAndMemberIdWithLock(
                fromAccount.getAccountNumber(),
                fromMember.getId()
        )).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumberWithLock(
                toAccount.getAccountNumber()
        )).thenReturn(Optional.of(toAccount));


        // When & Then

        assertThrows(
                InsufficientBalanceException.class,
                () -> accountService.transfer(request, memberId)
        );

        assertEquals(9000L, fromAccount.getBalance());
        assertEquals(0L, toAccount.getBalance());

        verify(transactionService, never())
                .saveTransaction(
                        any(Account.class),
                        any(TransactionType.class),
                        anyLong(),
                        anyString(),
                        anyString(),
                        anyString()
                );

        verify(eventPublisher, never())
                .publishEvent(any());

    }
}
