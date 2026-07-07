package com.example.bank.security;

import com.example.bank.entity.Member;
import com.example.bank.exception.member.MemberNotFoundException;
import com.example.bank.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@NullMarked
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // login
    @Override
    public CustomUserDetails loadUserByUsername(String username){

        Member member = memberRepository.findByLoginId(username)
                .orElseThrow(MemberNotFoundException::new);

        return new CustomUserDetails(member);
    }

    // request after login
    public CustomUserDetails loadUserById(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return new CustomUserDetails(member);
    }
}
