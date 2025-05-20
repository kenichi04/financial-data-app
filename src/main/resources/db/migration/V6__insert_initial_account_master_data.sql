-- 勘定科目マスタデータを事前登録
-- period_type: Enum PeriodType の name() と一致
-- balance: Enum Balance の name() と一致
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('CashAndDeposits', '現金預金', 'Cash and deposits', 'INSTANT', 'DEBIT', 4);
