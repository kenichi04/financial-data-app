-- 財務指標PR（V8）から分離して残っていたスキーマ変更をまとめて適用する。
-- 1. financial_data の行重複防止（日次バッチ導入で同一書類の再取込が起こる前提の下地）
-- 2. financial_document.company_id（未使用FK）を削除し、企業リンクを edinet_code 論理結合に一本化（V8 financial_metrics と統一）

-- 既存の重複行を削除（各グループの最小idを残す）。
-- iXBRLは同一事実を複数箇所に含むため、取込側の重複排除を入れる前のデータには重複が存在しうる。
-- 重複が無いDB（クリーンDB）では何も削除しないため冪等。UNIQUE制約付与の前提として先に実行する。
DELETE FROM financial_data
WHERE id NOT IN (
    SELECT MIN(id) FROM financial_data
    GROUP BY financial_document_id, account_id, context_ref
);

-- 重複防止の複合UNIQUE制約（同一書類・同一勘定科目・同一コンテキストは1行）
ALTER TABLE financial_data
    ADD CONSTRAINT uq_financial_data_doc_account_context
    UNIQUE (financial_document_id, account_id, context_ref);

-- account_id 単独インデックス。
-- financial_document_id 側は上記UNIQUEの複合インデックス先頭カラムでカバーされるため不要。
-- account_id は第2キーで絞り込みに使えないため単独で明示（PostgreSQLはFKに自動でインデックスを張らない）。
CREATE INDEX idx_financial_data_account_id ON financial_data (account_id);

-- 未使用の company_id カラムを削除（FK制約はカラム削除に伴い自動的に落ちる）
ALTER TABLE financial_document DROP COLUMN company_id;

-- edinet_code 論理結合・企業単位参照用インデックス
CREATE INDEX idx_financial_document_edinet_code ON financial_document (edinet_code);
