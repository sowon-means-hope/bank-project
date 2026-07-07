package com.example.bank.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AccountNumberGenerator {
    private static final SecureRandom random = new SecureRandom();

    public String generate(){
        StringBuilder accountNumber = new StringBuilder("100");

        for(int i=0; i<9; i++){
            accountNumber.append(random.nextInt(10));
        }

        return accountNumber.toString();
    }
}
