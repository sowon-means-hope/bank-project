package com.example.bank.controller;

import com.example.bank.dto.ApiResponse;
import com.example.bank.dto.member.MemberSignUpRequest;
import com.example.bank.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Void> signUp(
            @Valid @RequestBody MemberSignUpRequest request
            ){
        memberService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

}
