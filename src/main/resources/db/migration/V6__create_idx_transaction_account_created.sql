CREATE INDEX idx_transaction_account_created
ON transaction_history(account_id, created_at DESC);