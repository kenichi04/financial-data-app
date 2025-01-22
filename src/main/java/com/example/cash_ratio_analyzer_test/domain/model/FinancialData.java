package com.example.cash_ratio_analyzer_test.domain.model;

import java.math.BigDecimal;

// ドメインモデルとしての財務データ
// TODO 別途データ永続用のエンティティクラス（FinancialDataEntity）を作成して、データとロジックを分離する
public class FinancialData {
    private DocumentId documentId;
    private Account account;
    // 前期、今期の判定
    private String contextRef;
    // TODO 通貨は値オブジェクトもしくはenumで持つ方が良い。
    // TODO 変数名もEdinetレスポンスに合わせており分かりにくい？
    private String unitRef;
    private BigDecimal value;
    private String currency;

    public FinancialData(Account account, String contextRef, String unitRef, BigDecimal value) {
        this.account = account;
        this.contextRef = contextRef;
        this.unitRef = unitRef;
        this.value = value;
    }

    public Account getAccount() {
        return account;
    }

    public String getContextRef() {
        return contextRef;
    }

    public String getUnitRef() {
        return unitRef;
    }

    public BigDecimal getValue() {
        return value;
    }
}
