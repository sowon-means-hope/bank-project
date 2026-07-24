package com.example.bank.dto.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DepositRequest (
        @NotNull(message = "입금 금액을 입력해주세요.")
        @Positive(message = "입금 금액은 0보다 커야 합니다.")
        Long amount
){
}
