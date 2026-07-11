-- add status to member
ALTER TABLE member
ADD COLUMN status VARCHAR(20)
DEFAULT 'ACTIVE'
NOT NULL
CONSTRAINT chk_member_status
CHECK ( status IN ('ACTIVE', 'DORMANT', 'INACTIVE') );


-- create function, trigger
CREATE OR REPLACE FUNCTION fn_log_audit()
RETURNS TRIGGER
AS $$
BEGIN

    INSERT INTO audit_log (
                          table_name,
                          target_id,
                          action,
                          created_at
    )
    VALUES (
            TG_TABLE_NAME,
            NEW.id,
            TG_OP,
            NOW()
    );

    RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_member_audit
    AFTER INSERT OR UPDATE
                        ON member
                        FOR EACH ROW
                        EXECUTE FUNCTION fn_log_audit();

CREATE TRIGGER trg_account_audit
    AFTER INSERT OR UPDATE
                    ON account
                    FOR EACH ROW
                    EXECUTE FUNCTION fn_log_audit();

CREATE TRIGGER trg_transaction_audit
    AFTER INSERT
                ON transaction_history
                FOR EACH ROW
                EXECUTE FUNCTION fn_log_audit();