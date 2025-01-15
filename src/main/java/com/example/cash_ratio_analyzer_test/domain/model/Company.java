package com.example.cash_ratio_analyzer_test.domain.model;

import java.util.List;

public class Company {
    private long companyId;
    private String name;
    private String edinetCode;
    // TODO 値オブジェクトに変更する
    /** 証券コード */
    private String securityCode;
    /** 法人番号 */
    private String corporateNumber;
    // TODO Entityでは遅延ロードを使うべき
    private List<FinancialDocument> documents;

    public Company(long companyId, String edinetCode, String name) {
        this.companyId = companyId;
        this.edinetCode = edinetCode;
        this.name = name;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void addDocument(FinancialDocument document) {
        documents.add(document);
    }
}
