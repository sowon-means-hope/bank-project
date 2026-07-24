package com.example.bank.service;

import com.example.bank.dto.account.*;
import com.example.bank.entity.Account;
import com.example.bank.entity.Member;
import com.example.bank.entity.Transaction;
import com.example.bank.enums.AccountStatus;
import com.example.bank.enums.TransactionType;
import com.example.bank.event.TransferSucceedEvent;
import com.example.bank.exception.account.AccountNotFoundException;
import com.example.bank.exception.account.AccountUnavailableException;
import com.example.bank.exception.transfer.SameAccountTransferException;
import com.example.bank.generator.AccountNumberGenerator;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final TransactionService transactionService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public AccountDetailResponse openAccount(Long memberId){
        String accountNumber;
        do{
            accountNumber = accountNumberGenerator.generate();
        }while(accountRepository.existsByAccountNumber(accountNumber));

        Member member = memberRepository.getReferenceById(memberId);

        Account account = new Account(
                member,
                accountNumber
        );

        Account savedAccount = accountRepository.save(account);

        return new AccountDetailResponse(savedAccount);

    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccounts(Long memberId){
        Member member = memberRepository.getReferenceById(memberId);

        List<Account> accounts = accountRepository.findByMember(member);

        return accounts.stream()
                .map(AccountResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public AccountDetailResponse getAccountDetail(
            String accountNumber,
            Long memberId
    ){
        Account account = accountRepository.findByAccountNumberAndMemberId(
                accountNumber,
                memberId
        ).orElseThrow(AccountNotFoundException::new);

        return new AccountDetailResponse(account);
    }

    @Transactional
    public AccountDetailResponse closeAccount(
            String accountNumber,
            Long memberId
    ){
        Account account = accountRepository.findByAccountNumberAndMemberId(
                accountNumber,
                memberId
        ).orElseThrow(AccountNotFoundException::new);

        account.deactivate();

        return new AccountDetailResponse(account);
    }

    @Transactional(readOnly = true)
    public VerifyAccountResponse verifyAccount(
            String accountNumber
    ){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(AccountNotFoundException::new);

        if(account.getStatus() != AccountStatus.ACTIVE){
            throw new AccountUnavailableException();
        }

        String name = account.getMember().getName();

        return new VerifyAccountResponse(name);
    }

    @Transactional
    public TransferResponse transfer(
            TransferRequest request,
            Long memberId
    ){
        String fromAccountNumber = request.fromAccountNumber();
        String toAccountNumber = request.toAccountNumber();

        if(fromAccountNumber.equals(toAccountNumber)){
            throw new SameAccountTransferException();
        }

        Account fromAccount = accountRepository
                .findByAccountNumberAndMemberIdWithLock(
                        fromAccountNumber,
                        memberId
                ).orElseThrow(AccountNotFoundException::new);

        Account toAccount = accountRepository
                .findByAccountNumberWithLock(toAccountNumber)
                .orElseThrow(AccountNotFoundException::new);

        Long amount = request.amount();

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);


        // call TransactionService to save Transaction
        Transaction fromTransaction = transactionService.saveTransaction(
                fromAccount,
                TransactionType.TRANSFER_OUT,
                amount,
                toAccount.getAccountNumber(),
                toAccount.getMember().getName(),
                request.description()
        );

        Transaction toTransaction = transactionService.saveTransaction(
                toAccount,
                TransactionType.TRANSFER_IN,
                amount,
                fromAccount.getAccountNumber(),
                fromAccount.getMember().getName(),
                request.description()
        );

        // publish event for notification
        eventPublisher.publishEvent(
                new TransferSucceedEvent(
                        fromTransaction.getId(),
                        toTransaction.getId(),

                        fromTransaction.getAmount(),

                        fromAccount.getMember().getId(),
                        toAccount.getMember().getId(),

                        fromAccount.getMember().getName(),
                        toAccount.getMember().getName(),
                        fromAccount.getAccountNumber(),
                        toAccount.getAccountNumber(),
                        fromTransaction.getBalanceAfter(),
                        toTransaction.getBalanceAfter()
                )
        );


        return new TransferResponse(fromTransaction);
    }

    @Transactional
    public AccountResponse deposit(
            DepositRequest request,
            String accountNumber,
            Long memberId
    ){
        Account account = accountRepository.findByAccountNumberAndMemberId(accountNumber, memberId)
                .orElseThrow(AccountNotFoundException::new);

        account.deposit(request.amount());

        // save transaction

        // publish notification event

        return new AccountResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getStatus()
        );
    }

}
