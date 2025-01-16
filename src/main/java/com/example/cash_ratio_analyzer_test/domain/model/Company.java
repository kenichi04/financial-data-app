package com.example.cash_ratio_analyzer_test.domain.model;

import java.util.List;

public class Company {
    private String edinetCode;
    private String name;
    // TODO 値オブジェクトに変更する
    /** 証券コード */
    private String securityCode;
    /** 法人番号 */
    private String corporateNumber;
    // TODO Entityでは遅延ロードを使うべき
    private List<FinancialDocument> documents;

    public Company(String edinetCode, String name) {
        this.edinetCode = edinetCode;
        this.name = name;
    }

    public String getEdinetCode() {
        return edinetCode;
    }

    public void addDocument(FinancialDocument document) {
        documents.add(document);
    }
}
