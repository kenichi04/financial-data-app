CREATE TABLE company (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    edinet_code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    security_code VARCHAR(255) NOT NULL,
    corporate_number VARCHAR(255) NOT NULL
);
