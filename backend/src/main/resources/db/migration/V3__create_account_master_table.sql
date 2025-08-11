-- EDINET: 勘定科目リスト.xlsx参照
CREATE TABLE account_master (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name_jp VARCHAR(255) NOT NULL,
    name_en VARCHAR(255) NOT NULL,
    period_type VARCHAR(10) NOT NULL,  -- 'INSTANT' or 'DURATION'
    balance VARCHAR(10) NOT NULL,  -- 貸借区分（借方/貸方/NONE）
    depth INT NOT NULL
);
