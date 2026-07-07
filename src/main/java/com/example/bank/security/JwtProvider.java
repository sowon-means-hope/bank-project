package com.example.bank.security;

import com.example.bank.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(
                jwtProperties.secret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    public String createToken(Member member){
        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + jwtProperties.expiration()
        );

        return Jwts.builder()
        .subject(member.getId().toString())
        .issuedAt(now)
        .expiration(expiration)
        .signWith(getSigningKey())
        .compact();
    }

    public Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey()) // validate
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /*
    public String getLoginId(String token){
        return getClaims(token).getSubject();
        확장: loginId, memberId, role 로 token 만들때
        .get("memberId", Long.class)
        .get("role", String.class)
    }
    */
}
