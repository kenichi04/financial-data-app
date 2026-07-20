-- 財務指標テーブル（1書類 = 1指標セット）
-- 指標はバッチで事前計算して保存し、参照系（ランキング・スクリーニング・時系列）はこのテーブルを読む。
-- 企業とのリンクは edinet_code の論理結合（company.edinet_code はUNIQUE）。
-- 各指標は%値（例: 12.3456）。計算に必要な科目が書類に無い場合はNULL。
CREATE TABLE financial_metrics (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    financial_document_id BIGINT NOT NULL UNIQUE,
    edinet_code VARCHAR(255) NOT NULL,
    fiscal_year_end_date DATE NOT NULL,
    consolidated_type VARCHAR(20) NOT NULL,
    roe DECIMAL(10, 4),
    roa DECIMAL(10, 4),
    operating_margin DECIMAL(10, 4),
    equity_ratio DECIMAL(10, 4),
    cash_ratio DECIMAL(10, 4),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY (financial_document_id) REFERENCES financial_document(id)
);

-- 企業単位の時系列参照用
CREATE INDEX idx_financial_metrics_edinet_code ON financial_metrics (edinet_code);
-- ランキング・スクリーニング用（他指標のインデックスはスクリーニング実装時に追加）
CREATE INDEX idx_financial_metrics_cash_ratio ON financial_metrics (cash_ratio);
