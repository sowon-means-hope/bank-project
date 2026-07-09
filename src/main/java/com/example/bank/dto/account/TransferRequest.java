package com.example.bank.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record TransferRequest (
        @NotBlank(message = "출금 계좌번호는 필수입니다.")
        @Pattern(
                regexp = "^[0-9]{12}$",
                message = "계좌번호는 12자리 숫자여야 합니다."
        )
        String fromAccountNumber,

        @NotBlank(message = "입금 계좌번호는 필수입니다.")
        @Pattern(
                regexp = "^[0-9]{12}$",
                message = "계좌번호는 12자리 숫자여야 합니다."
        )
        String toAccountNumber,

        @Positive(message = "송금 금액은 0보다 커야 합니다.")
        Long amount,

        String description
){
}
