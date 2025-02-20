CREATE TABLE financial_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    financial_document_id BIGINT,
    account_id VARCHAR(255) NOT NULL,
    period_context VARCHAR(50) NOT NULL,
    amount DECIMAL(20, 4) NOT NULL,
    display_scale VARCHAR(50) NOT NULL,
    currency VARCHAR(50) NOT NULL,
    FOREIGN KEY (financial_document_id) REFERENCES financial_document(id)
);
