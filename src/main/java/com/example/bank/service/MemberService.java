package com.example.bank.service;

import com.example.bank.dto.member.MemberSignUpRequest;
import com.example.bank.entity.Member;
import com.example.bank.exception.auth.DuplicateLoginIdException;
import com.example.bank.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(MemberSignUpRequest request){
        // 아이디 중복 검사
        if(memberRepository.existsByLoginId(request.loginId())){
            //throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
            throw new DuplicateLoginIdException();
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());

        // Entity 생성
        Member member =
                new Member(
                        request.loginId(),
                        encodedPassword,
                        request.name(),
                        request.phone(),
                        request.email()
                );

        // Repository 통해 DB에 저장
        memberRepository.save(member);
    }
}
