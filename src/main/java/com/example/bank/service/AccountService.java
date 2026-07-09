package com.example.bank.service;

import com.example.bank.dto.account.AccountDetailResponse;
import com.example.bank.dto.account.AccountResponse;
import com.example.bank.dto.account.VerifyAccountResponse;
import com.example.bank.entity.Account;
import com.example.bank.entity.Member;
import com.example.bank.exception.account.AccountNotFoundException;
import com.example.bank.generator.AccountNumberGenerator;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    @Transactional
    public AccountDetailResponse openAccount(Long memberId){
        String accountNumber;
        do{
            accountNumber = accountNumberGenerator.generate();
        }while(accountRepository.existsByAccountNumber(accountNumber));

        Member member = memberRepository.getReferenceById(memberId);

        Account account = new Account(
                member,
                accountNumber
        );

        Account savedAccount = accountRepository.save(account);

        return new AccountDetailResponse(savedAccount);

    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccounts(Long memberId){
        Member member = memberRepository.getReferenceById(memberId);

        List<Account> accounts = accountRepository.findByMember(member);

        return accounts.stream()
                .map(AccountResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public AccountDetailResponse getAccountDetail(
            String accountNumber,
            Long memberId
    ){
        Account account = accountRepository.findByAccountNumberAndMemberId(
                accountNumber,
                memberId
        ).orElseThrow(AccountNotFoundException::new);

        return new AccountDetailResponse(account);
    }

    @Transactional
    public AccountDetailResponse closeAccount(
            String accountNumber,
            Long memberId
    ){
        Account account = accountRepository.findByAccountNumberAndMemberId(
                accountNumber,
                memberId
        ).orElseThrow(AccountNotFoundException::new);

        account.deactivate();

        return new AccountDetailResponse(account);
    }

    @Transactional(readOnly = true)
    public VerifyAccountResponse verifyAccount(
            String accountNumber
    ){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(AccountNotFoundException::new);

        String name = account.getMember().getName();

        return new VerifyAccountResponse(name);
    }
}
