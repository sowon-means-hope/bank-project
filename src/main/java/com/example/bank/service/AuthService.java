package com.example.bank.service;

import com.example.bank.dto.auth.AuthLoginRequest;
import com.example.bank.entity.Member;
import com.example.bank.exception.auth.LoginFailException;
import com.example.bank.repository.MemberRepository;
import com.example.bank.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public String login(AuthLoginRequest request){

        // 아이디 조회
        Member member = memberRepository.findByLoginId(request.loginId())
                .orElseThrow(LoginFailException::new);

        // 비밀번호 확인
        if(!passwordEncoder.matches(request.password(), member.getPassword())){
            throw new LoginFailException();
        }

        // JWT 발급
        return jwtProvider.createToken(member);
    }
}
