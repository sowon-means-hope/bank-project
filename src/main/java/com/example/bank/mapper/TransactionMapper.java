package com.example.bank.mapper;

import com.example.bank.dto.transaction.TransactionResponse;
import com.example.bank.dto.transaction.TransactionSearchCondition;

import java.util.List;

public interface TransactionMapper {
    List<TransactionResponse> findTransactions(
            TransactionSearchCondition condition
    );
}
