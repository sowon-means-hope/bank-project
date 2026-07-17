ALTER TABLE notification
DROP CONSTRAINT fk_notification_reference_type;

ALTER TABLE notification
ADD CONSTRAINT chk_notification_reference_type
CHECK (
    reference_type IN ('TRANSACTION', 'AUTO_TRANSFER')
);