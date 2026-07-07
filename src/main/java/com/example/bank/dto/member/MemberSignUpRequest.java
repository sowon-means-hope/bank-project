package com.example.bank.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberSignUpRequest (
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
        String password,

        @NotBlank(message = "이름 입력 필수")
        @Size(max = 30)
        String name,

        @NotBlank(message = "휴대폰 번호 입력 필수")
        @Pattern(
                regexp = "^010\\d{8}$",
                message = "휴대폰 형식 맞지 않음"
        )
        String phone,

        @Email(message = "이메일 형식 맞지 않음")
        @Size(max = 100)
        String email
){

}
