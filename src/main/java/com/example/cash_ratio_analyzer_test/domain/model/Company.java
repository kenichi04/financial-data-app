package com.example.cash_ratio_analyzer_test.domain.model;

import java.util.List;

public class Company {
    private EdinetCode edinetCode;
    private String name;
    /** 証券コード */
    private String securityCode;
    /** 法人番号 */
    private String corporateNumber;
    // TODO Entityでは遅延ロードを使うべき
    private List<FinancialDocument> documents;

    public Company(EdinetCode edinetCode, String name) {
        this.edinetCode = edinetCode;
        this.name = name;
    }

    public EdinetCode getEdinetCode() {
        return edinetCode;
    }

    public void addDocument(FinancialDocument document) {
        documents.add(document);
    }
}
