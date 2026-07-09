ALTER TABLE transaction_history
DROP CONSTRAINT chk_transaction_history_opponent_account;

ALTER TABLE transaction_history
ADD CONSTRAINT chk_transaction_history_opponent_account
CHECK (
    (type IN ('DEPOSIT', 'WITHDRAW')
        AND opponent_account IS NULL)
    OR
    (type IN ('TRANSFER_IN', 'TRANSFER_OUT')
        AND opponent_account ~ '^[0-9]{12}$')
);

