ALTER TABLE transaction_history
ADD COLUMN opponent_name VARCHAR(30);

UPDATE transaction_history
SET opponent_name = '';

ALTER TABLE transaction_history
ALTER COLUMN opponent_name SET NOT NULL;