-- JOOQ Codegen 用のSQLスクリプト
-- 開発中でH2データベース使用しているDBを見ずに定義を書いておく（インメモリDBのため中身みてくれなかった）
-- 実行コマンド > ./mvnw jooq-codegen:generate@generate-jooq
-- FlywayのV1～V5のテーブル相当
CREATE TABLE document_metadata (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_id VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    edinet_code VARCHAR(255) NOT NULL,
    document_type VARCHAR(255) NOT NULL,
    form_code VARCHAR(50) NOT NULL,
    submission_date DATE NOT NULL,
    processed BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE company (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    edinet_code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    security_code VARCHAR(255) NOT NULL UNIQUE,
    corporate_number VARCHAR(255) NOT NULL UNIQUE
);

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

CREATE TABLE financial_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    financial_document_id BIGINT NOT NULL,
    account_id VARCHAR(255) NOT NULL,
    period_type VARCHAR(20) NOT NULL,
    period_unit VARCHAR(20) NOT NULL,
    consolidated_type VARCHAR(20) NOT NULL,
    context_ref VARCHAR(255) NOT NULL,
    amount DECIMAL(20, 4) NOT NULL,
    display_scale VARCHAR(50) NOT NULL,
    currency VARCHAR(50) NOT NULL,
    FOREIGN KEY (financial_document_id) REFERENCES financial_document(id)
);

CREATE TABLE account_master (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name_jp VARCHAR(255) NOT NULL,
    name_en VARCHAR(255) NOT NULL,
    balance VARCHAR(50) NOT NULL  -- 貸借区分（借方/貸方/NONE）

);
