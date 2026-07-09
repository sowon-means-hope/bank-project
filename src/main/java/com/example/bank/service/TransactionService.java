package com.example.bank.service;

import com.example.bank.entity.Account;
import com.example.bank.entity.Transaction;
import com.example.bank.enums.TransactionType;
import com.example.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public Transaction saveTransaction(
        Account account,
        TransactionType type,
        Long amount,
        String opponentAccountNumber,
        String description
    ){
        Transaction transaction =
                new Transaction(
                        account,
                        type,
                        amount,
                        account.getBalance(),
                        opponentAccountNumber,
                        description
                );

        return transactionRepository.save(transaction);
    }
}
