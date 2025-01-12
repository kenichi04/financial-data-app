package com.example.cash_ratio_analyzer_test.domain.model;

import java.math.BigDecimal;

// ドメインモデルとしての財務データ
// TODO 別途データ永続用のエンティティクラス（FinancialDataEntity）を作成して、データとロジックを分離する
public class FinancialData {
    private String documentId;
    private String name;
    // 前期、今期の判定
    private String contextRef;
    private String unitRef;
    private BigDecimal value;
    // TODO 通貨はFinancialDocumentクラスで持つ方が良さそう
    private String currency;

    public FinancialData(String name, String contextRef, String unitRef, BigDecimal value) {
        this.name = name;
        this.contextRef = contextRef;
        this.unitRef = unitRef;
        this.value = value;
    }

    public String getName() {
        return name;
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
