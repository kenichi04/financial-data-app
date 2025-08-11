-- 勘定科目マスタデータを事前登録
-- period_type: Enum PeriodType の name() と一致
-- balance: Enum Balance の name() と一致

-- ===============================
-- BS科目の登録
-- ===============================
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('CashAndDeposits', '現金及び預金', 'Cash and deposits', 'INSTANT', 'DEBIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('NotesAndAccountsReceivableTradeAndContractAssets', '受取手形、売掛金及び契約資産', 'Notes and accounts receivable - trade, and contract assets', 'INSTANT', 'DEBIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('NotesAndAccountsReceivableTrade', '受取手形及び売掛金', 'Notes and accounts receivable - trade', 'INSTANT', 'DEBIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('AccountsReceivableTradeAndContractAssets', '売掛金及び契約資産', 'Accounts receivable - trade, and contract assets', 'INSTANT', 'DEBIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('NotesReceivableTrade', '受取手形', 'Notes receivable - trade', 'INSTANT', 'DEBIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('AccountsReceivableTrade', '売掛金', 'Accounts receivable - trade', 'INSTANT', 'DEBIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('Inventories', '棚卸資産', 'Inventories', 'INSTANT', 'DEBIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('CurrentAssets', '流動資産', 'Current assets', 'INSTANT', 'DEBIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('Land', '土地', 'Land', 'INSTANT', 'DEBIT', 5);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('AccumulatedDepreciationPPEByGroup', '減価償却累計額', 'Accumulated depreciation', 'INSTANT', 'DEBIT', 5);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('PropertyPlantAndEquipment', '有形固定資産', 'Property, plant and equipment', 'INSTANT', 'DEBIT', 5);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('Software', 'ソフトウエア', 'Software', 'INSTANT', 'DEBIT', 5);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('IntangibleAssets', '無形固定資産', 'Intangible assets', 'INSTANT', 'DEBIT', 5);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('InvestmentSecurities', '投資有価証券', 'Investment securities', 'INSTANT', 'DEBIT', 6);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('InvestmentsAndOtherAssets', '投資その他の資産', 'Investments and other assets', 'INSTANT', 'DEBIT', 5);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('NoncurrentAssets', '固定資産', 'Non-current assets', 'INSTANT', 'DEBIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('DeferredAssets', '繰延資産', 'Deferred assets', 'INSTANT', 'DEBIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('Assets', '資産', 'Assets', 'INSTANT', 'DEBIT', 3);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('NotesAndAccountsPayableTrade', '支払手形及び買掛金', 'Notes and accounts payable - trade', 'INSTANT', 'CREDIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('NotesPayableTrade', '支払手形', 'Notes payable - trade', 'INSTANT', 'CREDIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('AccountsPayableTrade', '買掛金', 'Accounts payable - trade', 'INSTANT', 'CREDIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('NotesAndOperatingAccountsPayableTrade', '支払手形及び営業未払金', 'Trade notes and accounts payable', 'INSTANT', 'CREDIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('ShortTermBondsPayable', '短期社債', 'Short-term bonds payable', 'INSTANT', 'CREDIT', 5);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('ShortTermLoansPayable', '短期借入金', 'Short-term borrowings', 'INSTANT', 'CREDIT', 5);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('CurrentPortionOfBonds', '１年内償還予定の社債', 'Current portion of bonds payable', 'INSTANT', 'CREDIT', 5);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('CurrentPortionOfLongTermLoansPayable', '１年内返済予定の長期借入金', 'Current portion of long-term borrowings', 'INSTANT', 'CREDIT', 5);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('CurrentLiabilities', '流動負債', 'Current liabilities', 'INSTANT', 'CREDIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('BondsPayable', '社債', 'Bonds payable', 'INSTANT', 'CREDIT', 5);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('LongTermLoansPayable', '長期借入金', 'Long-term borrowings', 'INSTANT', 'CREDIT', 5);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('NoncurrentLiabilities', '固定負債', 'Non-current liabilities', 'INSTANT', 'CREDIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('Liabilities', '負債', 'Liabilities', 'INSTANT', 'CREDIT', 3);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('CapitalStock', '資本金', 'Share capital', 'INSTANT', 'CREDIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('RetainedEarningsBroughtForward', '繰越利益剰余金', 'Retained earnings brought forward', 'INSTANT', 'CREDIT', 6);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('OtherRetainedEarnings', 'その他利益剰余金', 'Other retained earnings', 'INSTANT', 'CREDIT', 6);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('RetainedEarnings', '利益剰余金', 'Retained earnings', 'INSTANT', 'CREDIT', 5);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('TreasuryStock', '自己株式', 'Treasury shares', 'INSTANT', 'CREDIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('ShareholdersEquity', '株主資本', 'Shareholders'' equity', 'INSTANT', 'CREDIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('NetAssets', '純資産', 'Net assets', 'INSTANT', 'CREDIT', 3);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('LiabilitiesAndNetAssets', '負債純資産', 'Liabilities and net assets', 'INSTANT', 'CREDIT', 2);

-- ===============================
-- PL科目の登録
-- ===============================
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('NetSales', '売上高', 'Net sales', 'DURATION', 'CREDIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('Revenue', '売上収益', 'Revenue', 'DURATION', 'CREDIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('OperatingRevenue1', '営業収益', 'Operating revenue', 'DURATION', 'CREDIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('OperatingRevenue2', '営業収入', 'Operating revenue', 'DURATION', 'CREDIT', 4);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('GrossProfit', '売上総利益又は売上総損失（△）', 'Gross profit (loss)', 'DURATION', 'CREDIT', 2);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('OperatingGrossProfit', '営業総利益又は営業総損失（△）', 'Operating gross profit (loss)', 'DURATION', 'CREDIT', 2);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('OperatingIncome', '営業利益又は営業損失（△）', 'Operating profit (loss)', 'DURATION', 'CREDIT', 2);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('OrdinaryIncome', '経常利益又は経常損失（△）', 'Ordinary profit (loss)', 'DURATION', 'CREDIT', 2);
INSERT INTO account_master (code, name_jp, name_en, period_type, balance, depth) VALUES ('ProfitLoss', '当期純利益又は当期純損失（△）', 'Profit (loss)', 'DURATION', 'CREDIT', 2);
