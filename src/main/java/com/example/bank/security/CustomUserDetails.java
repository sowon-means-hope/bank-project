package com.example.bank.security;

import com.example.bank.entity.Member;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NullMarked
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        // 리팩토링(role in Member)
    }

    @Override
    public String getPassword(){
        return member.getPassword();
    }

    @Override
    public String getUsername(){
        return member.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
        // 리팩토링(status in Member)
        // 계정의 사용 기간 만료
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
        //``
        // 비밀번호 5회 오류, 관리자가 이상 로그 감지 등 보안 이슈
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
        //``
        // 비밀번호 90일마다 변경
    }

    @Override
    public boolean isEnabled(){
        return true;
        // ``
        // 계정 탈퇴
    }

    public Long getMemberId(){
        return member.getId();
    }
    public Member getMember(){
        return member;
    }
}
