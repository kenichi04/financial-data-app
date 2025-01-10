package com.example.cash_ratio_analyzer_test.domain.model;

import java.util.List;

public class Company {
    private String companyId;
    private String edinetCode;
    /** 証券コード */
    private String secCode;
    /** 法人番号 */
    private String JCN;
    private String companyName;
    // FinancialDocumentのListよりメモリ節約
    private List<String> documentIds;
}
