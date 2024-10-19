CREATE TABLE IF NOT EXISTS seller_history (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    seller_id BIGINT,
    name VARCHAR(20),
    contact_info VARCHAR(50),
    registration_date TIMESTAMP,
    operation VARCHAR(6),
    change_data TIMESTAMP
);

CREATE TABLE IF NOT EXISTS transactions_history (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    transaction_id BIGINT,
    seller_id BIGINT,
    amount BIGINT,
    payment_type VARCHAR(8),
    transaction_date TIMESTAMP,
    operation VARCHAR(6),
    change_data TIMESTAMP
);
