package com.example.bank.repository;

import com.example.bank.entity.Account;
import com.example.bank.entity.Member;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByMember(Member member);

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumberAndMemberId(
            String accountNumber,
            Long memberId
    );

    List<Account> findAll();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select a
    from Account a
    where a.accountNumber = :accountNumber
    """)
    Optional<Account> findByAccountNumberWithLock(String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select a
    from Account a
    where a.accountNumber = :accountNumber
        and a.member.id = :memberId
    """)
    Optional<Account> findByAccountNumberAndMemberIdWithLock(
            String accountNumber,
            Long memberId
    );

}
