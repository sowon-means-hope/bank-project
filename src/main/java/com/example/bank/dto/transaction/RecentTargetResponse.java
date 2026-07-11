package com.example.bank.dto.transaction;

import com.example.bank.enums.AccountStatus;

import java.time.OffsetDateTime;

public record RecentTargetResponse (
        String opponentAccount,
        String opponentName,
        AccountStatus accountStatus,
        OffsetDateTime lastTransferredAt
){
}
