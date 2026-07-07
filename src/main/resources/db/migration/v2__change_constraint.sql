-- member 휴대폰 번호 제약조건: 글자 수(11) -> 휴대폰 번호 형식
ALTER TABLE member
DROP CONSTRAINT chk_member_phone;

ALTER TABLE member
ADD CONSTRAINT chk_member_phone
CHECK ( phone ~ '^010[0-9]{8}$' );

-- account 계좌번호 제약조건: 글자 수(12) -> 숫자 12자
ALTER TABLE account
DROP CONSTRAINT chk_account_account_number;

ALTER TABLE account
ADD CONSTRAINT chk_account_account_number
CHECK ( account_number ~ '^[0-9]{12}$');

-- transaction_history 상대방 계좌 제약조건: NULL or 글자 수(12) -> NULL or 숫자 12자
ALTER TABLE transaction_history
ADD CONSTRAINT chk_transaction_history_opponent_account
CHECK (
    opponent_account IS NULL
    OR opponent_account ~ '^[0-9]{12}$'
);
