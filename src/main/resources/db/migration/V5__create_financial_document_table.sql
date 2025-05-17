CREATE TABLE financial_document (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_id VARCHAR(255) NOT NULL UNIQUE,
    edinet_code VARCHAR(255) NOT NULL,
    document_type VARCHAR(255) NOT NULL,
    fiscal_year_end_date DATE NOT NULL,
    currency VARCHAR(50) NOT NULL,
    company_id BIGINT,
    FOREIGN KEY (company_id) REFERENCES company(id)
);
