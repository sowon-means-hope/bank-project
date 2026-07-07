package com.example.bank.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthLoginRequest(
        @NotBlank(message = "아이디 입력 필수")
        @Pattern(
                regexp = "^[a-zA-Z0-9]{4,30}$",
                message = "아이디는 영문자,숫자 포함 4~30자"
        )
        String loginId,

        @NotBlank(message = "비밀번호 입력 필수")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
                message = "비밀번호는 영문자,숫자,특수문자(@$!%*#?&) 포함 8~20자"
        )
        String password
){
}
