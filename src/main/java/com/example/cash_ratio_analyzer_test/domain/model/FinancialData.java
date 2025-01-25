package com.example.cash_ratio_analyzer_test.domain.model;

import com.example.cash_ratio_analyzer_test.domain.enums.Currency;

import java.math.BigDecimal;

// ドメインモデルとしての財務データ
// TODO 別途データ永続用のエンティティクラス（FinancialDataEntity）を作成して、データとロジックを分離する
public class FinancialData {
    private DocumentId documentId;
    private Account account;
    // 前期、今期の判定
    private String contextRef;
    private BigDecimal value;
    private Currency currency;

    public FinancialData(Account account, String contextRef, BigDecimal value, Currency currency) {
        this.account = account;
        this.contextRef = contextRef;
        this.value = value;
        this.currency = currency;
    }

    public Account getAccount() {
        return account;
    }

    public String getContextRef() {
        return contextRef;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }
}
