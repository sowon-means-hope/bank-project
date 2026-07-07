package com.example.bank.repository;

import com.example.bank.entity.Account;
import com.example.bank.entity.Member;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByMember(Member member);

    Optional<Account> findByAccountNumberAndMemberId(
            String accountNumber,
            Long memberId
    );

    List<Account> findAll();
}
