package com.example.bank.dto.transaction;

import com.example.bank.enums.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record TransactionSearchRequest (
        @NotBlank(message = "계좌를 선택해주세요.")
        @Pattern(
                regexp = "^[0-9]{12}$",
                message = "올바르지 않은 계좌번호 입니다."
        )
        String accountNumber,

        TransactionType transactionType,

        LocalDate startDate,
        LocalDate endDate,

        @Min(0)
        Integer page,

        @Positive
        Integer size
){
}
