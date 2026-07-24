package com.example.bank.controller;

import com.example.bank.dto.ApiResponse;
import com.example.bank.dto.auth.AuthLoginRequest;
import com.example.bank.dto.auth.AuthLoginResponse;
import com.example.bank.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "로그인",
            description = "회원가입 시 사용한 아이디, 비밀번호로 로그인합니다."
    )
    public ResponseEntity<AuthLoginResponse> login(
            @Valid @RequestBody AuthLoginRequest request
            ){
        String accessToken = authService.login(request);

        return ResponseEntity.ok(new AuthLoginResponse(accessToken));
    }
}
