package com.example.cash_ratio_analyzer_test.entity;

import java.math.BigDecimal;

// ドメインモデルとしての財務データ
// TODO 別途データ永続用のエンティティクラスを作成して、データとロジックを分離する
// FinancialDataEntityクラスを作成して、FinancialDataクラスはドメインモデルとしての責務を持たせる
public class FinancialData {
    // TODO FinancialDocumentクラスに紐づく様に修正
//    private String documentId;
    private String accountName;
    private BigDecimal currentValue;
    private BigDecimal previousValue;
    // TODO 通貨はFinancialDocumentクラスで持つ方が良さそう
    private String currency;

}
