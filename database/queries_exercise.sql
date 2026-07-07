/*
로그인 아이디로 거래내역 조회
* (transaction_history) <- id (account) <- login_id (member)
*/

SELECT th.*
FROM transaction_history th
JOIN account a
ON th.account_id = a.id
JOIN member m
ON a.member_id = m.id
WHERE m.login_id = 'hong123';

-- 입금 알림 확인
UPDATE notification
SET is_read = TRUE
WHERE id = 2;

SELECT *
FROM notification;

-- 확인한 알림 삭제
DELETE FROM notification
WHERE id = 2;

SELECT *
FROM notification;
