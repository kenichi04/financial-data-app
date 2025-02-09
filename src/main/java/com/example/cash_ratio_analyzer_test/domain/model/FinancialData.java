package com.example.cash_ratio_analyzer_test.domain.model;

import com.example.cash_ratio_analyzer_test.application.service.enums.DisplayScale;
import com.example.cash_ratio_analyzer_test.domain.enums.Currency;

import java.math.BigDecimal;

// ドメインモデルとしての財務データ
// TODO 別途データ永続用のエンティティクラス（FinancialDataEntity）を作成して、データとロジックを分離する
public class FinancialData {
    private Long id;
    // TODO 必要かどうか検討
    private DocumentId documentId;
    private final Account account;
    // 前期、今期の判定
    private final String periodContext;
    private final BigDecimal value;
    private final DisplayScale displayScale;
    private final Currency currency;

    public FinancialData(Account account, String periodContext, BigDecimal value, DisplayScale displayScale, Currency currency) {
        this.account = account;
        this.periodContext = periodContext;
        this.value = value;
        this.displayScale = displayScale;
        this.currency = currency;
    }

    public Account getAccount() {
        return account;
    }

    public String getPeriodContext() {
        return periodContext;
    }

    public BigDecimal getValue() {
        return value;
    }

    public DisplayScale getDisplayScale() {
        return displayScale;
    }

    public Currency getCurrency() {
        return currency;
    }
}
