CREATE TABLE financial_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    financial_document_id BIGINT NOT NULL,
    account_id VARCHAR(255) NOT NULL,
    period_type VARCHAR(20) NOT NULL,
    period_unit VARCHAR(20) NOT NULL,
    consolidated_type VARCHAR(20) NOT NULL,
    context_ref VARCHAR(255) NOT NULL,
    amount DECIMAL(20, 4) NOT NULL,
    display_scale INT NOT NULL,
    currency VARCHAR(50) NOT NULL,
    FOREIGN KEY (financial_document_id) REFERENCES financial_document(id)
);
