package com.example.bank.controller;

import com.example.bank.dto.ApiResponse;
import com.example.bank.dto.member.MemberSignUpRequest;
import com.example.bank.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> signUp(
            @Valid @RequestBody MemberSignUpRequest request
            ){
        memberService.signUp(request);

        return ResponseEntity.ok(
            ApiResponse.success("회원가입 성공")
        );
    }

}
