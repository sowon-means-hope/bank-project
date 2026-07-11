package com.example.bank.mapper;

import com.example.bank.dto.transaction.RecentTargetResponse;
import com.example.bank.dto.transaction.TransactionResponse;
import com.example.bank.dto.transaction.TransactionSearchCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransactionMapper {
    List<TransactionResponse> findTransactions(
            TransactionSearchCondition condition
    );

    List<RecentTargetResponse> findRecentTargets(
            @Param("accountId") Long accountId
    );
}
