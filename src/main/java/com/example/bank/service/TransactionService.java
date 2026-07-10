package com.example.bank.service;

import com.example.bank.dto.transaction.TransactionResponse;
import com.example.bank.dto.transaction.TransactionSearchCondition;
import com.example.bank.dto.transaction.TransactionSearchRequest;
import com.example.bank.entity.Account;
import com.example.bank.entity.Transaction;
import com.example.bank.enums.TransactionType;
import com.example.bank.exception.account.AccountNotFoundException;
import com.example.bank.mapper.TransactionMapper;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    public Transaction saveTransaction(
        Account account,
        TransactionType type,
        Long amount,
        String opponentAccountNumber,
        String opponentName,
        String description
    ){
        Transaction transaction =
                new Transaction(
                        account,
                        type,
                        amount,
                        account.getBalance(),
                        opponentAccountNumber,
                        opponentName,
                        description
                );

        return transactionRepository.save(transaction);
    }

    public List<TransactionResponse> searchTransactions(
            TransactionSearchRequest request,
            Long memberId
    ){
        Account account = accountRepository.findByAccountNumberAndMemberId(
                request.accountNumber(),
                memberId
        ).orElseThrow(AccountNotFoundException::new);

        LocalDateTime startDateTime = request.startDate() == null
                ? null
                : request.startDate().atStartOfDay();
        LocalDateTime endDateTime = request.endDate() == null
                ? null
                : request.endDate().plusDays(1).atStartOfDay();

        int limit = request.size() == null
                ? 20
                : request.size();
        int offset = request.page() == null
                ? 0
                : request.page()* limit;

        TransactionSearchCondition condition =
                new TransactionSearchCondition(
                        account.getId(),
                        request.transactionType(),
                        startDateTime,
                        endDateTime,
                        limit,
                        offset
                );

        return transactionMapper.findTransactions(condition);
    }
}
