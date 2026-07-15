CREATE TRIGGER trg_notification_audit
    AFTER INSERT
    ON notification
    FOR EACH ROW
    EXECUTE FUNCTION fn_log_audit();