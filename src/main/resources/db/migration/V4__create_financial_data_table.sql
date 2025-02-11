CREATE TABLE financial_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_id VARCHAR(255) NOT NULL,
    account VARCHAR(255) NOT NULL,
    period_context VARCHAR(50) NOT NULL,
    amount DECIMAL(20, 4) NOT NULL,
    display_scale VARCHAR(50) NOT NULL,
    currency VARCHAR(50) NOT NULL,
    financial_document_id BIGINT,
    FOREIGN KEY (financial_document_id) REFERENCES financial_document(id)
);
