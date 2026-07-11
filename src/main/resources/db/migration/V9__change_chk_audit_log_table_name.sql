ALTER TABLE audit_log
DROP CONSTRAINT chk_audit_log_table_name;

ALTER TABLE audit_log
ADD CONSTRAINT chk_audit_log_table_name
CHECK (
    table_name IN ('member', 'account', 'transaction_history', 'notification')
);