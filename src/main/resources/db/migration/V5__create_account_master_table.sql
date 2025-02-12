CREATE TABLE account_master (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name_jp VARCHAR(255) NOT NULL,
    name_en VARCHAR(255) NOT NULL,
    balance VARCHAR(50) NOT NULL  -- 貸借区分（借方/貸方/NONE）

);
