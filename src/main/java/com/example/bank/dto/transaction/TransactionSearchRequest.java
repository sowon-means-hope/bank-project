package com.example.bank.dto.transaction;

import com.example.bank.enums.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record TransactionSearchRequest (
        @NotBlank(message = "계좌번호는 필수입니다.")
        @Pattern(
                regexp = "^[0-9]{12}$",
                message = "계좌번호는 12자리 숫자여야 합니다."
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
