# 웹 뱅킹 백엔드 프로젝트
회원가입, JWT 기반 로그인, 계좌 개설 및 조회, 송금 기능을 중심으로 트랜잭션 관리, 동시성 제어, 이벤트 기반 알림, Trigger 기반 감사 로그를 적용하여 웹 백엔드 시스템을 설계하고 구현하였습니다.

## 기술 스택

### Backend
- Java 25
- Spring Boot
- Spring Security
- Spring Data JPA
- MyBatis

### Database
- PostgreSQL

### Test
- JUnit5
- Mockito
- Spring Boot Test

### DevOps
- Git / GitHub

### Tool
- IntelliJ IDEA
- pgAdmin
- Postman

## 기능

### 사용자
- 회원가입
- 로그인 (JWT 발급 및 인증)

### 계좌
- 계좌 개설
- 내 계좌 조회 (1:N)
- 계좌 상세 조회
- 계좌 해지 (비활성화)
- 예금주 조회
- 송금
  - @Transactional 기반 원자성 보장
  - Pessimistic Lock 이용한 동시성 제어
  - Event + Listener 통한 입출금 알림 생성

### 거래
- 거래내역 조회 (Mybatis)
- 최근 거래 상대 조회 (Mybatis)

### 알림
- 송금 알림 조회
- 알림 읽음 처리

### 로그
- Trigger 이용한 Audit Log 자동 기록

## ERD
![ERD](images/erd.jpg)

## DB 설계

