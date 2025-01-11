package com.example.cash_ratio_analyzer_test.domain.model;

import java.util.List;

public class Company {
    private String companyId;
    private String edinetCode;
//    /** 証券コード */
//    private String securityCode;
//    /** 法人番号 */
//    private String corporateNumber;
    private String name;
    // TODO Entityでは遅延ロードを使うべき
    private List<FinancialDocument> documents;

    public Company(String companyId, String edinetCode, String name) {
        this.companyId = companyId;
        this.edinetCode = edinetCode;
        this.name = name;
    }

    public void addDocument(FinancialDocument document) {
        documents.add(document);
    }
}
